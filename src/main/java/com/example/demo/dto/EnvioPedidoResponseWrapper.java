package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Wrapper class for the order response JSON structure
 */
public class EnvioPedidoResponseWrapper {
    
    @JsonProperty("enviarPedidoRespuesta")
    private EnvioPedidoResponse enviarPedidoRespuesta;

    public EnvioPedidoResponseWrapper() {}

    public EnvioPedidoResponseWrapper(EnvioPedidoResponse enviarPedidoRespuesta) {
        this.enviarPedidoRespuesta = enviarPedidoRespuesta;
    }

    public EnvioPedidoResponse getEnviarPedidoRespuesta() {
        return enviarPedidoRespuesta;
    }

    public void setEnviarPedidoRespuesta(EnvioPedidoResponse enviarPedidoRespuesta) {
        this.enviarPedidoRespuesta = enviarPedidoRespuesta;
    }
}