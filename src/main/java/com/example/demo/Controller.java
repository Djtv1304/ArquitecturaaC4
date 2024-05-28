package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.HashMap;

@RestController
public class Controller {

    @GetMapping("/verificar-cedula/{cedula}")
    public Map<String, Object> verificarCedula(@PathVariable String cedula) {

        RestTemplate restTemplate = new RestTemplate();
        String url = "https://srienlinea.sri.gob.ec/sri-catastro-sujeto-servicio-internet" +
                "/rest/ConsolidadoContribuyente/existePorNumeroRuc?numeroRuc=" + cedula;

        String response = null;

        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
            response = responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            response = e.getResponseBodyAsString();
        }

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> responseJson = new HashMap<>();

        try {
            Object responseObject = mapper.readValue(response, Object.class);

            if (responseObject instanceof Boolean) {
                responseJson.put("isCedulaExistente", (Boolean) responseObject);
            } else if (responseObject instanceof Map) {
                responseJson = (Map<String, Object>) responseObject;
                if (responseJson.containsKey("mensaje")) {
                    responseJson.put("isCedulaExistente", false);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return responseJson;
    }
}
