package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;

/**
 * Service for calling external SOAP service
 */
@Service
public class ExternalServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(ExternalServiceClient.class);
    
    private final WebClient webClient;
    private static final String EXTERNAL_SERVICE_URL = "https://run.mocky.io/v3/19217075-6d4e-4818-98bc-416d1feb7b84";
    
    public ExternalServiceClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl(EXTERNAL_SERVICE_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE)
                .build();
    }

    /**
     * Sends SOAP XML request to external service and returns the XML response
     */
    public String sendSoapRequest(String soapXml) {
        try {
            logger.info("Sending SOAP request to external service");
            logger.debug("SOAP Request: {}", soapXml);

            String response = webClient.post()
                    .contentType(MediaType.APPLICATION_XML)
                    .bodyValue(soapXml)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(30))
                    .block();

            logger.info("Received response from external service");
            logger.debug("SOAP Response: {}", response);
            
            return response;

        } catch (WebClientResponseException e) {
            logger.error("Error calling external service. Status: {}, Body: {}", 
                        e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Error calling external service: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error calling external service", e);
            throw new RuntimeException("Unexpected error calling external service", e);
        }
    }
}