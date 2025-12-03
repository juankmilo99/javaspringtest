package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * Wrapper class for the order request JSON structure
 */
public class EnvioPedidoRequestWrapper {
    
    @JsonProperty("enviarPedido")
    @Valid
    @NotNull(message = "enviarPedido is required")
    private EnvioPedidoRequest enviarPedido;

    public EnvioPedidoRequestWrapper() {}

    public EnvioPedidoRequestWrapper(EnvioPedidoRequest enviarPedido) {
        this.enviarPedido = enviarPedido;
    }

    public EnvioPedidoRequest getEnviarPedido() {
        return enviarPedido;
    }

    public void setEnviarPedido(EnvioPedidoRequest enviarPedido) {
        this.enviarPedido = enviarPedido;
    }
}