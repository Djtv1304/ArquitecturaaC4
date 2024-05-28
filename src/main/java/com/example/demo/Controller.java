package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Map;
import java.util.HashMap;

@RestController
public class Controller {

    @GetMapping("/verificar-cedula/{cedula}")
    public Map<String, Object> verificarCedula(@PathVariable String cedula) {

        RestTemplate restTemplate = new RestTemplate();
        String url = "https://srienlinea.sri.gob.ec/sri-catastro-sujeto-servicio-internet" +
                "/rest/ConsolidadoContribuyente/existePorNumeroRuc?numeroRuc=" + cedula;

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        String response = responseEntity.getBody();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> responseJson = new HashMap<>();

        try {
            responseJson = mapper.readValue(response, Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        if (responseJson.containsKey("mensaje")) {
            responseJson.put("isCedulaExistente", false);
        } else {
            responseJson.put("isCedulaExistente", true);
        }
        System.out.println("SUPUESTO JSON:\n" + response);
        return responseJson;
    }
}
