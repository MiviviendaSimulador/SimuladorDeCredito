package com.mivivienda.platform.simuladordecredito.core.infrastructure.reports;

import com.mivivienda.platform.simuladordecredito.core.domain.model.aggregates.Simulation;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
public class SimulationReportGenerator {

    public Optional<byte[]> generate(Simulation simulation, String format) {
        try {
            if ("csv".equalsIgnoreCase(format)) {
                return Optional.of(generateCsv(simulation).getBytes(StandardCharsets.UTF_8));
            } else if ("pdf".equalsIgnoreCase(format)) {
                return Optional.of(generatePdf(simulation));
            }
            return Optional.empty();
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private String generateCsv(Simulation simulation) {
        StringBuilder sb = new StringBuilder();
        sb.append("Mes,Saldo Inicial,Cuota Base,Interés,Capital,Seguro Desgravamen,Seguro Riesgo,Otros Cargos,Cuota Total,Saldo Final\n");
        simulation.getLines().forEach(line -> sb.append(String.format("%d,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f\n",
                line.getNumeroMes(),
                line.getSaldoInicial(),
                line.getCuotaBase(),
                line.getInteres(),
                line.getCapital(),
                line.getSeguroDesgravamen(),
                line.getSeguroRiesgo(),
                line.getOtrosCargos(),
                line.getCuotaTotal(),
                line.getSaldoFinal()
        )));
        return sb.toString();
    }

    private byte[] generatePdf(Simulation simulation) {
        // (simplificado) — puedes usar iText o Apache PDFBox
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            String header = "Reporte de Simulación - ID " + simulation.getId() + "\n\n";
            out.write(header.getBytes(StandardCharsets.UTF_8));
            for (var line : simulation.getLines()) {
                String row = String.format("Mes %d: Cuota %.2f - Saldo %.2f\n",
                        line.getNumeroMes(),
                        line.getCuotaTotal(),
                        line.getSaldoFinal());
                out.write(row.getBytes(StandardCharsets.UTF_8));
            }
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar PDF", e);
        }
    }
}
