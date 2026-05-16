package com.uade.supermercado.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    private static volatile AppConfig instancia;

    public static synchronized AppConfig getInstance() {
        if (instancia == null) {
            instancia = new AppConfig();
        }
        return instancia;
    }
}
