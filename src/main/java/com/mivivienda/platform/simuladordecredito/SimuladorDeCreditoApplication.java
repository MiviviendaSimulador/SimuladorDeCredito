package com.mivivienda.platform.simuladordecredito;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableJpaAuditing
@SpringBootApplication
public class SimuladorDeCreditoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimuladorDeCreditoApplication.class, args);
    }

}
