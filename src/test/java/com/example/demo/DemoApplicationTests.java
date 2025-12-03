package com.example.demo;

import com.example.demo.dto.EnvioPedidoRequest;
import com.example.demo.service.XmlTransformationService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private XmlTransformationService xmlTransformationService;

    @Test
    void contextLoads() {
        // Test básico para verificar que el contexto de Spring se carga correctamente
        assertNotNull(xmlTransformationService);
    }

    @Test
    void testXmlTransformation() {
        // Test de transformación JSON a XML
        EnvioPedidoRequest request = new EnvioPedidoRequest(
            "75630275",
            "1", 
            "00110000765191002104587",
            "Armario INVAL",
            "1113987400",
            "CR 72B 45 12 APT 301"
        );

        String xml = xmlTransformationService.convertToSoapXml(request);
        
        assertNotNull(xml);
        assertTrue(xml.contains("pedido"));
        assertTrue(xml.contains("75630275"));
        assertTrue(xml.contains("Cantidad"));
        assertTrue(xml.contains("EAN"));
        assertTrue(xml.contains("Producto"));
        assertTrue(xml.contains("Cedula"));
        assertTrue(xml.contains("Direccion"));
        assertTrue(xml.contains("soapenv:Envelope"));
    }
}