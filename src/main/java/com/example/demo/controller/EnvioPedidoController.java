package com.example.demo.controller;

import com.example.demo.dto.EnvioPedidoRequestWrapper;
import com.example.demo.dto.EnvioPedidoResponse;
import com.example.demo.dto.EnvioPedidoResponseWrapper;
import com.example.demo.service.ExternalServiceClient;
import com.example.demo.service.XmlTransformationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * REST Controller for order shipment API
 * Handles JSON requests, transforms to XML, calls external service, and returns JSON response
 */
@RestController
@RequestMapping("/api/v1")
@Validated
@CrossOrigin(origins = "*")
public class EnvioPedidoController {

    private static final Logger logger = LoggerFactory.getLogger(EnvioPedidoController.class);

    private final XmlTransformationService xmlTransformationService;
    private final ExternalServiceClient externalServiceClient;

    public EnvioPedidoController(XmlTransformationService xmlTransformationService, 
                                ExternalServiceClient externalServiceClient) {
        this.xmlTransformationService = xmlTransformationService;
        this.externalServiceClient = externalServiceClient;
    }

    /**
     * GET endpoint for envio-pedido to show usage information
     */
    @GetMapping(value = "/envio-pedido", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<java.util.Map<String, Object>> infoEnvioPedido() {
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        response.put("mensaje", "Endpoint para procesar envío de pedidos");
        response.put("metodo", "POST");
        response.put("contentType", "application/json");
        response.put("url", "/api/v1/envio-pedido");
        response.put("ejemplo", java.util.Map.of(
            "enviarPedido", java.util.Map.of(
                "numPedido", "75630275",
                "cantidadPedido", "1",
                "codigoEAN", "00110000765191002104587",
                "nombreProducto", "Armario INVAL",
                "numDocumento", "1113987400",
                "direccion", "CR 72B 45 12 APT 301"
            )
        ));
        return ResponseEntity.ok(response);
    }

    /**
     * Main endpoint to process order shipment requests
     * Accepts JSON, transforms to XML SOAP, calls external service, and returns JSON response
     */
    @PostMapping("/envio-pedido")
    public ResponseEntity<EnvioPedidoResponseWrapper> procesarEnvioPedido(
            @Valid @RequestBody EnvioPedidoRequestWrapper requestWrapper) {
        
        try {
            logger.info("Processing order shipment request for order: {}", 
                       requestWrapper.getEnviarPedido().getNumPedido());

            // Step 1: Transform JSON to SOAP XML
            String soapRequest = xmlTransformationService.convertToSoapXml(
                requestWrapper.getEnviarPedido()
            );
            
            logger.debug("Generated SOAP request: {}", soapRequest);

            // Step 2: Call external SOAP service
            String soapResponse = externalServiceClient.sendSoapRequest(soapRequest);

            // Step 3: Transform SOAP XML response back to JSON
            EnvioPedidoResponse response = xmlTransformationService.parseFromSoapXml(soapResponse);
            
            EnvioPedidoResponseWrapper responseWrapper = new EnvioPedidoResponseWrapper(response);

            logger.info("Successfully processed order shipment request. Code: {}, Status: {}", 
                       response.getCodigoEnvio(), response.getEstado());

            return ResponseEntity.ok(responseWrapper);

        } catch (Exception e) {
            logger.error("Error processing order shipment request", e);
            
            // Create error response
            EnvioPedidoResponse errorResponse = new EnvioPedidoResponse(
                "ERROR", 
                "Error procesando el pedido: " + e.getMessage()
            );
            EnvioPedidoResponseWrapper errorWrapper = new EnvioPedidoResponseWrapper(errorResponse);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorWrapper);
        }
    }

    /**
     * API welcome endpoint
     */
    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<java.util.Map<String, Object>> welcome() {
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        response.put("mensaje", "API de Envío de Pedidos ACME");
        response.put("version", "1.0.0");
        response.put("endpoints", java.util.Arrays.asList(
            "GET /api/v1/health",
            "GET /api/v1/info", 
            "POST /api/v1/envio-pedido"
        ));
        return ResponseEntity.ok(response);
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("API de Envío de Pedidos ACME - Funcionando correctamente");
    }

    /**
     * Get API information
     */
    @GetMapping(value = "/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<java.util.Map<String, Object>> info() {
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        response.put("service", "API de Envío de Pedidos ACME");
        response.put("version", "1.0.0");
        response.put("description", "Servicio REST para el ciclo de abastecimiento");
        response.put("endpoints", java.util.Arrays.asList(
            "POST /api/v1/envio-pedido - Procesar envío de pedido",
            "GET /api/v1/envio-pedido - Información del endpoint",
            "GET /api/v1/health - Estado del servicio",
            "GET /api/v1/info - Información del servicio"
        ));
        return ResponseEntity.ok(response);
    }
}