package com.skyregister;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Punto de entrada de SkyRegister. Spring Boot configura la aplicación web. */
@SpringBootApplication
public class SkyRegisterApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkyRegisterApplication.class, args);
    }
}
