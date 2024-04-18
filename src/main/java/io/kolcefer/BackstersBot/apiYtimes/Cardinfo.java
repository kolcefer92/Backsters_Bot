package io.kolcefer.BackstersBot.apiYtimes;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component

public class Cardinfo {

    private String name;

    private String points;

    public String getName() {
        return name;
    }

    public String getPoints() {

        return points;
    }

   


    public void getInfo(String phone) {

        String url = "https://api.ytimes.ru/ex/client/loadClientInfo";
        String token = "070950ad-b669-4633-9055-6b583abf8d91-1706029627735";


        // Создаем объект HttpClient
        HttpClient httpClient = HttpClients.createDefault();

        // Создаем объект HttpPost с URL
        HttpPost httpPost = new HttpPost(url);


        // Добавляем заголовки с токеном и указываем формат JSON
        httpPost.addHeader("Authorization", token);
        httpPost.addHeader("Accept", "application/json;charset=UTF-8");
        httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");

        int balance1 = 0;
        String balans1 = null;
        int result = 0;

        String username = null;
        try {
            // Добавляем тело запроса в формате JSON
            String jsonInput = "{\"cardNumber\": " + phone + "}";
            StringEntity entity = new StringEntity(jsonInput);
            httpPost.setEntity(entity);

            // Отправляем запрос и получаем ответ
            HttpResponse response = httpClient.execute(httpPost);

            // Печатаем статус код ответа
            System.out.println("Response Code: " + response.getStatusLine().getStatusCode());

            // Получаем содержимое ответа в виде строки
            String responseBody = EntityUtils.toString(response.getEntity());
            // System.out.println("Response Body: " + responseBody);


            JSONObject jsonResponse = new JSONObject(responseBody);

// Проверка успешности запроса
            boolean success = jsonResponse.getBoolean("success");
            if (success) {
                // Получение массива точек
                JSONArray rows = jsonResponse.getJSONArray("rows");

                for (int i = 0; i < rows.length(); i++) {
                    JSONObject point = rows.getJSONObject(i);
                    String name = point.getString("name");
                    username = name;
                    float points;
                    points = point.getFloat("points");
                    result = (int) points;

                    // String type = point.getString("type");
                    // String cityName = point.optString("cityName", ""); // Может быть null
                    // String address = point.optString("address", ""); // Может быть null
                    // String phone = point.optString("phone", ""); // Может быть null

                    // Дальнейшие действия с полученной информацией о точке
                    //  System.out.println("Point: " + name + " (Type: " + type + ")");
                    //   System.out.println("GUID: " + guid);
                    //  System.out.println("City: " + cityName);
                    //  System.out.println("Address: " + address);
                    //  System.out.println("Phone: " + phone);
                    // System.out.println();
                }
            } else {
                // Обработка ошибки, если необходимо
                String error = jsonResponse.optString("error", "Unknown error");
                System.out.println("Error: " + error);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // String balance1 = points;


        }

        String str = String.valueOf(result);
        this.name = username;
        this.points = str;


    }
}


/*
import com.fasterxml.jackson.annotation.JsonProperty;

public class ClientData {
    @JsonProperty("name")
    private String name;

    @JsonProperty("points")
    private float points;

    // Геттеры и сеттеры
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float points() {
        return points;
    }

    public void setPoints(float points) {
        this.points = pointspoints;
    }
}

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class Main {
    public static void main(String[] args) {
        // Создаем объект RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // URL для отправки POST-запроса
        String url = "https://api.ytimes.ru/ex/client/loadClientInfo";

        // Создаем объект с данными для отправки
        ClientInfo clientInfo = new ClientInfo("7", "9001112233");

        try {
            // Преобразуем объект в JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(clientInfo);

            // Задаем заголовки запроса
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Создаем объект запроса
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

            // Отправляем запрос и получаем ответ
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            // Выводим результат
            System.out.println("Response status: " + response.getStatusCode());
            System.out.println("Response body: " + response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// Используем Lombok для автоматической генерации геттеров, сеттеров, конструкторов и toString
@Data
@NoArgsConstructor
@AllArgsConstructor
class ClientInfo {
    // Аннотации для Jackson для указания имени полей в JSON
    @JsonProperty("phoneCode")
    private String phoneCode;

    @JsonProperty("phone")
    private String phone;
}



// Отправляем запрос и получаем ответ
ResponseEntity<ClientData> response = restTemplate.postForEntity(url, request, ClientData.class);

// Получаем объект ClientData из ответа
ClientData clientData = response.getBody();

// Используем данные из объекта ClientData
String name = clientData.getName();
float points = clientData.getPoints();

// Теперь вы можете использовать переменные name и points в вашей программе

 */