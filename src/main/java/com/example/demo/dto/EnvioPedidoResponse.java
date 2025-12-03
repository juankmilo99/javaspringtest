package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data Transfer Object for order response (JSON format)
 * Maps to the inner object of the response JSON structure
 */
public class EnvioPedidoResponse {
    
    @JsonProperty("codigoEnvio")
    private String codigoEnvio;
    
    @JsonProperty("estado")
    private String estado;

    // Default constructor
    public EnvioPedidoResponse() {}

    // Constructor with all fields
    public EnvioPedidoResponse(String codigoEnvio, String estado) {
        this.codigoEnvio = codigoEnvio;
        this.estado = estado;
    }

    // Getters and setters
    public String getCodigoEnvio() {
        return codigoEnvio;
    }

    public void setCodigoEnvio(String codigoEnvio) {
        this.codigoEnvio = codigoEnvio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "EnvioPedidoResponse{" +
                "codigoEnvio='" + codigoEnvio + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}