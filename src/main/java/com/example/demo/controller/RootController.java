package com.example.demo.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Root controller for the application
 */
@RestController
@RequestMapping("/")
public class RootController {

    /**
     * Root endpoint that redirects to API information
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<java.util.Map<String, Object>> root() {
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        response.put("aplicacion", "API de Envío de Pedidos ACME");
        response.put("estado", "Funcionando correctamente ✅");
        response.put("version", "1.0.0");
        response.put("documentacion", "Visita /api/v1/info para más información");
        
        return ResponseEntity.ok(response);
    }
}