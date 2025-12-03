package com.example.demo.service;

import com.example.demo.dto.EnvioPedidoRequest;
import com.example.demo.dto.EnvioPedidoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

/**
 * Service for transforming between JSON DTOs and SOAP XML format
 */
@Service
public class XmlTransformationService {

    private static final Logger logger = LoggerFactory.getLogger(XmlTransformationService.class);

    private final DocumentBuilderFactory documentBuilderFactory;

    public XmlTransformationService() {
        this.documentBuilderFactory = DocumentBuilderFactory.newInstance();
        this.documentBuilderFactory.setNamespaceAware(true);
    }

    /**
     * Converts JSON DTO to SOAP XML format for the external service
     */
    public String convertToSoapXml(EnvioPedidoRequest request) {
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            // Create SOAP Envelope
            Element soapEnvelope = document.createElement("soapenv:Envelope");
            soapEnvelope.setAttribute("xmlns:soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
            soapEnvelope.setAttribute("xmlns:env", "http://WSDLs/EnvioPedidos/EnvioPedidosAcme");
            document.appendChild(soapEnvelope);

            // Create SOAP Header
            Element soapHeader = document.createElement("soapenv:Header");
            soapEnvelope.appendChild(soapHeader);

            // Create SOAP Body
            Element soapBody = document.createElement("soapenv:Body");
            soapEnvelope.appendChild(soapBody);

            // Create EnvioPedidoAcme element
            Element envioPedidoAcme = document.createElement("env:EnvioPedidoAcme");
            soapBody.appendChild(envioPedidoAcme);

            // Create EnvioPedidoRequest element
            Element envioPedidoRequestElement = document.createElement("EnvioPedidoRequest");
            envioPedidoAcme.appendChild(envioPedidoRequestElement);

            // Add child elements with mapped field names
            addElement(document, envioPedidoRequestElement, "pedido", request.getNumPedido());
            addElement(document, envioPedidoRequestElement, "Cantidad", request.getCantidadPedido());
            addElement(document, envioPedidoRequestElement, "EAN", request.getCodigoEAN());
            addElement(document, envioPedidoRequestElement, "Producto", request.getNombreProducto());
            addElement(document, envioPedidoRequestElement, "Cedula", request.getNumDocumento());
            addElement(document, envioPedidoRequestElement, "Direccion", request.getDireccion());

            // Convert Document to String
            return documentToString(document);

        } catch (Exception e) {
            logger.error("Error converting to SOAP XML", e);
            throw new RuntimeException("Error converting to SOAP XML", e);
        }
    }

    /**
     * Parses SOAP XML response and converts to JSON DTO
     */
    public EnvioPedidoResponse parseFromSoapXml(String soapXml) {
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(soapXml.getBytes(StandardCharsets.UTF_8));
            Document document = documentBuilder.parse(inputStream);

            // Navigate through the SOAP structure
            NodeList responseNodes = document.getElementsByTagName("EnvioPedidoResponse");
            if (responseNodes.getLength() > 0) {
                Element responseElement = (Element) responseNodes.item(0);
                
                String codigo = getElementValue(responseElement, "Codigo");
                String mensaje = getElementValue(responseElement, "Mensaje");

                return new EnvioPedidoResponse(codigo, mensaje);
            } else {
                logger.warn("No EnvioPedidoResponse element found in SOAP response");
                throw new RuntimeException("Invalid SOAP response format");
            }

        } catch (Exception e) {
            logger.error("Error parsing SOAP XML response", e);
            throw new RuntimeException("Error parsing SOAP XML response", e);
        }
    }

    /**
     * Helper method to add an element with text content
     */
    private void addElement(Document document, Element parent, String tagName, String textContent) {
        Element element = document.createElement(tagName);
        element.setTextContent(textContent);
        parent.appendChild(element);
    }

    /**
     * Helper method to get element value by tag name
     */
    private String getElementValue(Element parent, String tagName) {
        NodeList nodeList = parent.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            return node.getTextContent();
        }
        return null;
    }

    /**
     * Converts Document to formatted XML String
     */
    private String documentToString(Document document) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");

        StringWriter stringWriter = new StringWriter();
        StreamResult streamResult = new StreamResult(stringWriter);
        DOMSource domSource = new DOMSource(document);
        
        transformer.transform(domSource, streamResult);
        return stringWriter.toString();
    }
}