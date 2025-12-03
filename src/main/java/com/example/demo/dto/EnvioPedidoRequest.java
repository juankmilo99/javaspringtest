package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object for order request (JSON format)
 * Maps to the inner object of the request JSON structure
 */
public class EnvioPedidoRequest {
    
    @JsonProperty("numPedido")
    @NotBlank(message = "numPedido is required")
    private String numPedido;
    
    @JsonProperty("cantidadPedido")
    @NotBlank(message = "cantidadPedido is required")
    private String cantidadPedido;
    
    @JsonProperty("codigoEAN")
    @NotBlank(message = "codigoEAN is required")
    private String codigoEAN;
    
    @JsonProperty("nombreProducto")
    @NotBlank(message = "nombreProducto is required")
    private String nombreProducto;
    
    @JsonProperty("numDocumento")
    @NotBlank(message = "numDocumento is required")
    private String numDocumento;
    
    @JsonProperty("direccion")
    @NotBlank(message = "direccion is required")
    private String direccion;

    // Default constructor
    public EnvioPedidoRequest() {}

    // Constructor with all fields
    public EnvioPedidoRequest(String numPedido, String cantidadPedido, String codigoEAN, 
                              String nombreProducto, String numDocumento, String direccion) {
        this.numPedido = numPedido;
        this.cantidadPedido = cantidadPedido;
        this.codigoEAN = codigoEAN;
        this.nombreProducto = nombreProducto;
        this.numDocumento = numDocumento;
        this.direccion = direccion;
    }

    // Getters and setters
    public String getNumPedido() {
        return numPedido;
    }

    public void setNumPedido(String numPedido) {
        this.numPedido = numPedido;
    }

    public String getCantidadPedido() {
        return cantidadPedido;
    }

    public void setCantidadPedido(String cantidadPedido) {
        this.cantidadPedido = cantidadPedido;
    }

    public String getCodigoEAN() {
        return codigoEAN;
    }

    public void setCodigoEAN(String codigoEAN) {
        this.codigoEAN = codigoEAN;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        return "EnvioPedidoRequest{" +
                "numPedido='" + numPedido + '\'' +
                ", cantidadPedido='" + cantidadPedido + '\'' +
                ", codigoEAN='" + codigoEAN + '\'' +
                ", nombreProducto='" + nombreProducto + '\'' +
                ", numDocumento='" + numDocumento + '\'' +
                ", direccion='" + direccion + '\'' +
                '}';
    }
}