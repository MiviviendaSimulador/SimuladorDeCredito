package com.mivivienda.platform.simuladordecredito;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SimuladorDeCreditoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimuladorDeCreditoApplication.class, args);
    }

}
