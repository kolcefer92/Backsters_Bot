package io.kolcefer.BackstersBot.apiYtimes;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CardInfoGetBalance {
    @Autowired
    ClientData clientData;


    public ClientData getBalance(String phoneID) {

        // Создаем объект RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // URL для отправки POST-запроса
        String url = "https://api.ytimes.ru/ex/client/loadClientInfo";

        // Создаем объект с данными для отправки
        ClientInfo clientInfo = new ClientInfo("7", phoneID);

        try {
            // Преобразуем объект в JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(clientInfo);

            // Задаем заголовки запроса
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "070950ad-b669-4633-9055-6b583abf8d91-1706029627735");


            // Создаем объект запроса
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);


            // Отправляем запрос и получаем ответ
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            // Выводим результат
            System.out.println("Response status: " + response.getStatusCode());
            System.out.println("Response body: " + response.getBody());


// Парсинг JSON-ответа
            JsonNode rootNode = objectMapper.readTree(response.getBody());

// Извлечение данных из JSON
            String name = rootNode.get("rows").get(0).get("name").asText();
            String surname = rootNode.get("rows").get(0).get("surname").asText();
            float points = (float) rootNode.get("rows").get(0).get("points").asDouble();

            // Создание объекта ClientData и заполнение его данными
           // ClientData clientData = new ClientData();

            clientData.setName(name);
            clientData.setSurname(surname);
            clientData.setPoints(points);


// Вывод данных
            System.out.println("Name: " + name);
            System.out.println("Points: " + points);

            return clientData;


        } catch (Exception e) {
            e.printStackTrace();
        }

    return null;
    }
}




