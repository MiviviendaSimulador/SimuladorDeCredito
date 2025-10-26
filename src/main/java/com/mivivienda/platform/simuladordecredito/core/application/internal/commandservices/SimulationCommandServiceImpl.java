package com.mivivienda.platform.simuladordecredito.core.application.internal.commandservices;

import com.mivivienda.platform.simuladordecredito.core.domain.model.aggregates.Simulation;
import com.mivivienda.platform.simuladordecredito.core.domain.model.aggregates.SimulationLine;
import com.mivivienda.platform.simuladordecredito.core.domain.model.commands.CreateSimulationCommand;
import com.mivivienda.platform.simuladordecredito.core.domain.model.valueobjects.RateType;
import com.mivivienda.platform.simuladordecredito.core.domain.services.SimulationCommandService;
import com.mivivienda.platform.simuladordecredito.core.infrastructure.persistence.jpa.repositories.BankRepository;
import com.mivivienda.platform.simuladordecredito.core.infrastructure.persistence.jpa.repositories.SimulationRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SimulationCommandServiceImpl implements SimulationCommandService {
    private final SimulationRepository simulationRepository;
    private final BankRepository bankRepository;

    public SimulationCommandServiceImpl(SimulationRepository simulationRepository, BankRepository bankRepository) {
        this.simulationRepository = simulationRepository;
        this.bankRepository = bankRepository;
    }

    @Override
    public Optional<Simulation> handle(CreateSimulationCommand command) {
        // 1. Crear simulation con datos de entrada
        var simulation = new Simulation(command);

        // 2. Cargar Bank si existe
        if (command.bankId() != null) {
            var bank = this.bankRepository.findById(command.bankId());
            if (bank.isEmpty()){
                throw new IllegalArgumentException("El banco con id " + command.bankId() + " no existe");
            }
            simulation.setBank(bank.get());
        }

        // 3. Calcular BBP (Bono Buen Pagador) - Por ahora 0, después se añade lógica
        BigDecimal bbp = BigDecimal.ZERO;
        simulation.setBbp(bbp);

        // 4. Calcular Monto a Financiar
        BigDecimal cuotaInicial = command.precioVivienda()
                .multiply(command.cuotaInicialPct())
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        BigDecimal montoFinanciar = command.precioVivienda()
                .subtract(cuotaInicial)
                .subtract(bbp);
        simulation.setMontoFinanciar(montoFinanciar);

        // 5. Calcular TEM (Tasa Efectiva Mensual)
        BigDecimal tem = calcularTEM(command.tasaTipo(), command.tasaValor(), command.capitalizacionM());
        simulation.setTem(tem);

        // 6. Calcular Cuota Base (Método Francés)
        BigDecimal cuotaBase = calcularCuotaBase(montoFinanciar, tem, command.plazoMeses());
        simulation.setCuotaBase(cuotaBase);

        // 7. Generar Cronograma (las 60 líneas)
        List<SimulationLine> lines = generarCronograma(simulation, montoFinanciar, tem, cuotaBase, command);
        simulation.setLines(lines);

        // 8. Calcular Cuota Total Promedio
        BigDecimal promedio = lines.stream()
                .map(SimulationLine::getCuotaTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(new BigDecimal(lines.size()), 2, RoundingMode.HALF_UP);
        simulation.setCuotaTotalProm(promedio);

        // 9. Calcular Indicadores Financieros
        BigDecimal tir = calcularTIRSeguro(lines, montoFinanciar);
        simulation.setTir(tir);

        // TCEA = (1 + TIR)^12 - 1 (donde TIR ya incluye todos los costos)
        double tceaDouble = Math.pow(1 + tir.doubleValue(), 12) - 1;
        BigDecimal tcea = BigDecimal.valueOf(tceaDouble).setScale(8, RoundingMode.HALF_UP);
        simulation.setTcea(tcea);

        // VAN = Monto recibido - Total de pagos descontados usando TEM
        BigDecimal van = calcularVANCorrecto(lines, montoFinanciar, tem);
        simulation.setVan(van);

        // 10. Guardar todo
        var savedSimulation = this.simulationRepository.save(simulation);
        return Optional.of(savedSimulation);
    }

    // ==================== MÉTODOS DE CÁLCULO ====================

    /**
     * Calcula la Tasa Efectiva Mensual (TEM)
     * Si es EA: TEM = (1 + EA)^(1/12) - 1
     * Si es TNA: TEM = TNA / (12 * capitalizacionM)
     */
    private BigDecimal calcularTEM(RateType tipo, BigDecimal tasa, Integer capitalizacionM) {
        MathContext mc = new MathContext(10, RoundingMode.HALF_UP);

        if (tipo == RateType.EA) {
            // EA: TEM = (1 + tasa)^(1/12) - 1
            BigDecimal unoMasTasa = BigDecimal.ONE.add(tasa);
            BigDecimal exponente = new BigDecimal("1").divide(new BigDecimal("12"), mc);
            double resultado = Math.pow(unoMasTasa.doubleValue(), exponente.doubleValue());
            return BigDecimal.valueOf(resultado).subtract(BigDecimal.ONE).setScale(10, RoundingMode.HALF_UP);
        } else {
            // TNA: TEM = TNA / (12 * capitalizacionM)
            BigDecimal denominador = new BigDecimal("12").multiply(new BigDecimal(capitalizacionM));
            return tasa.divide(denominador, mc);
        }
    }

    /**
     * Calcula la Cuota Base usando Método Francés
     * Cuota = PV * [TEM * (1+TEM)^n] / [(1+TEM)^n - 1]
     */
    private BigDecimal calcularCuotaBase(BigDecimal montoFinanciar, BigDecimal tem, Integer plazoMeses) {
        MathContext mc = new MathContext(10, RoundingMode.HALF_UP);

        BigDecimal unoMasTem = BigDecimal.ONE.add(tem);
        double unoMasTemDouble = unoMasTem.doubleValue();
        double potencia = Math.pow(unoMasTemDouble, plazoMeses);

        BigDecimal numerador = tem.multiply(BigDecimal.valueOf(potencia), mc);
        BigDecimal denominador = BigDecimal.valueOf(potencia).subtract(BigDecimal.ONE);
        BigDecimal factor = numerador.divide(denominador, mc);

        return montoFinanciar.multiply(factor, mc).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Genera el cronograma completo (60 líneas)
     */
    private List<SimulationLine> generarCronograma(
            Simulation simulation,
            BigDecimal montoFinanciar,
            BigDecimal tem,
            BigDecimal cuotaBase,
            CreateSimulationCommand command
    ) {
        List<SimulationLine> lines = new ArrayList<>();
        BigDecimal saldoActual = montoFinanciar;

        for (int mes = 1; mes <= command.plazoMeses(); mes++) {
            SimulationLine line = new SimulationLine();
            line.setSimulation(simulation);
            line.setNumeroMes(mes);
            line.setSaldoInicial(saldoActual);

            // Calcular interés del mes
            BigDecimal interes = saldoActual.multiply(tem).setScale(2, RoundingMode.HALF_UP);
            line.setInteres(interes);

            // Calcular capital (amortización)
            BigDecimal capital = cuotaBase.subtract(interes);
            line.setCapital(capital);
            line.setCuotaBase(cuotaBase);

            // Seguros
            BigDecimal seguroDesgravamen = saldoActual.multiply(command.seguroDesgravamenPct())
                    .setScale(2, RoundingMode.HALF_UP);
            line.setSeguroDesgravamen(seguroDesgravamen);

            // Seguro riesgo es fijo (ya viene en el comando)
            BigDecimal seguroRiesgo = command.seguroRiesgoPct().multiply(cuotaBase).setScale(2, RoundingMode.HALF_UP);
            line.setSeguroRiesgo(seguroRiesgo);

            // Otros cargos
            line.setOtrosCargos(command.otrosCargosFijos());

            // Cuota total
            BigDecimal cuotaTotal = cuotaBase
                    .add(seguroDesgravamen)
                    .add(seguroRiesgo)
                    .add(command.otrosCargosFijos());
            line.setCuotaTotal(cuotaTotal);

            // Saldo final
            saldoActual = saldoActual.subtract(capital);
            if (mes == command.plazoMeses()) {
                // Ajuste final para el último mes
                saldoActual = BigDecimal.ZERO;
            }
            line.setSaldoFinal(saldoActual);

            lines.add(line);
        }

        return lines;
    }


    private BigDecimal calcularValorNeto(List<SimulationLine> lines, BigDecimal entrada, BigDecimal tasa) {
        BigDecimal vn = entrada; // Entrada positiva

        for (int mes = 0; mes < lines.size(); mes++) {
            BigDecimal factor = BigDecimal.ONE.add(tasa).pow(mes + 1, MathContext.DECIMAL64);
            BigDecimal descontado = lines.get(mes).getCuotaTotal().negate().divide(factor, 10, RoundingMode.HALF_UP);
            vn = vn.add(descontado);
        }

        return vn;
    }

    private BigDecimal calcularDerivada(List<SimulationLine> lines, BigDecimal tasa) {
        BigDecimal derivada = BigDecimal.ZERO;

        for (int mes = 0; mes < lines.size(); mes++) {
            int periodo = mes + 1;
            BigDecimal factor2 = BigDecimal.ONE.add(tasa).pow(periodo + 1, MathContext.DECIMAL64);
            BigDecimal termino = new BigDecimal(periodo).multiply(lines.get(mes).getCuotaTotal().negate())
                    .divide(factor2, 10, RoundingMode.HALF_UP);
            derivada = derivada.add(termino);
        }

        return derivada;
    }

    /**
     * Calcula TIR con validaciones para evitar overflow
     */
    private BigDecimal calcularTIRSeguro(List<SimulationLine> lines, BigDecimal entrada) {
        try {
            BigDecimal tasaInicial = new BigDecimal("0.01");
            BigDecimal precision = new BigDecimal("0.000001"); // Menos precisión pero más seguro
            BigDecimal tasa = tasaInicial;
            
            for (int i = 0; i < 50; i++) {
                BigDecimal f = calcularValorNeto(lines, entrada, tasa);
                
                // Evitar división por cero
                if (f.abs().compareTo(precision) < 0) {
                    return tasa.setScale(8, RoundingMode.HALF_UP);
                }
                
                BigDecimal fDerivada = calcularDerivada(lines, tasa);
                if (fDerivada.abs().compareTo(new BigDecimal("0.0001")) < 0) {
                    break;
                }
                
                BigDecimal nuevaTasa = tasa.subtract(f.divide(fDerivada, 10, RoundingMode.HALF_UP));
                
                // Validar que no sea negativa o muy grande
                if (nuevaTasa.compareTo(BigDecimal.ZERO) < 0 || nuevaTasa.compareTo(new BigDecimal("2")) > 0) {
                    return tasa.setScale(8, RoundingMode.HALF_UP);
                }
                
                if (nuevaTasa.subtract(tasa).abs().compareTo(precision) < 0) {
                    return tasa.setScale(8, RoundingMode.HALF_UP);
                }
                
                tasa = nuevaTasa;
            }
            
            return tasa.setScale(8, RoundingMode.HALF_UP);
        } catch (Exception e) {
            // Si falla, retorna una aproximación basada en el TEM
            return new BigDecimal("0.01");
        }
    }
    
    /**
     * Calcula VAN con descuento temporal usando la tasa proporcionada
     * VAN = Entrada - Σ(Pagos / (1+tasa)^periodo)
     */
    private BigDecimal calcularVANCorrecto(List<SimulationLine> lines, BigDecimal entrada, BigDecimal tasaDescuento) {
        BigDecimal van = entrada; // Entrada es positiva (lo que recibes)
        
        try {
            // Descontar cada pago a valor presente
            for (int mes = 0; mes < lines.size(); mes++) {
                // Factor de descuento: (1 + tasa)^(mes+1)
                BigDecimal unoMasTasa = BigDecimal.ONE.add(tasaDescuento);
                
                // Usar Math.pow para evitar overflow con BigDecimal.pow
                double unoMasTasaDouble = unoMasTasa.doubleValue();
                double factorDouble = Math.pow(unoMasTasaDouble, mes + 1);
                BigDecimal factor = BigDecimal.valueOf(factorDouble);
                
                // Pago descontado = Pago / Factor
                BigDecimal pagoDescontado = lines.get(mes).getCuotaTotal()
                        .divide(factor, 10, RoundingMode.HALF_UP);
                
                van = van.subtract(pagoDescontado); // Restamos los pagos descontados
            }
        } catch (Exception e) {
            // Si hay overflow, usar método simplificado
            BigDecimal totalPagado = lines.stream()
                    .map(SimulationLine::getCuotaTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            return entrada.subtract(totalPagado).setScale(2, RoundingMode.HALF_UP);
        }
        
        return van.setScale(2, RoundingMode.HALF_UP);
    }
}
