package com.baozistore.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ApiController {
    @GetMapping("/")
    public Map<String, Object> inicio() {
        return Map.of(
                "name", "Baozi Store API",
                "status", "online",
                "endpoints", Map.of(
                        "clientes", "/api/clientes",
                        "produtos", "/api/produtos",
                        "pedidos", "/api/pedidos"
                )
        );
    }
}
