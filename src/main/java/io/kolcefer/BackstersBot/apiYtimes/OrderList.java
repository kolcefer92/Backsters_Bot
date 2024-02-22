package io.kolcefer.BackstersBot.apiYtimes;

import lombok.Data;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
@Data

public class OrderList {


    private String name;

    private String points;

    public String getName() {
        return name;
    }

    public String getPoints() {

        return points;
    }




    public void getOrderInfo() {

        String url = "https://api.ytimes.ru/ex/order/list";
        String token = "070950ad-b669-4633-9055-6b583abf8d91-1706029627735";


        // Создаем объект HttpClient
        HttpClient httpClient = HttpClients.createDefault();

        // Создаем объект HttpPost с URL
        HttpPost httpPost = new HttpPost(url);


        // Добавляем заголовки с токеном и указываем формат JSN
        httpPost.addHeader("Authorization", token);
        httpPost.addHeader("Accept", "application/json;charset=UTF-8");
        httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");

//        int balance1 = 0;
//        String balans1 = null;
//        int result = 0;
//
//        String username = null;
        try {
            // Добавляем тело запроса в формате JSON
            String jsonInput = "{\"shopGuid\": \"c219c74e-7026-4f11-b8a8-e63643febe42\"," +
                    "\"dateFrom\": \"22.02.2024\"," +
                    "\"dateTo\": \"22.02.2024\"}";
           //"{\"cardNumber\": " + phone + "}";
            StringEntity entity = new StringEntity(jsonInput);
            httpPost.setEntity(entity);

            // Отправляем запрос и получаем ответ
            HttpResponse response = httpClient.execute(httpPost);

            // Печатаем статус код ответа
            System.out.println("Response Code: " + response.getStatusLine().getStatusCode());

            // Получаем содержимое ответа в виде строки
            String responseBody = EntityUtils.toString(response.getEntity());
             System.out.println("Response Body: " + responseBody);


//            JSONObject jsonResponse = new JSONObject(responseBody);
//
//// Проверка успешности запроса
//            boolean success = jsonResponse.getBoolean("success");
//            if (success) {
//                // Получение массива точек
//                JSONArray rows = jsonResponse.getJSONArray("rows");
//
//                for (int i = 0; i < rows.length(); i++) {
//                    JSONObject point = rows.getJSONObject(i);
//                    String name = point.getString("name");
//                    username = name;
//                    float points;
//                    points = point.getFloat("points");
//                    result = (int) points;
//
//                    // String type = point.getString("type");
//                    // String cityName = point.optString("cityName", ""); // Может быть null
//                    // String address = point.optString("address", ""); // Может быть null
//                    // String phone = point.optString("phone", ""); // Может быть null
//
//                    // Дальнейшие действия с полученной информацией о точке
//                    //  System.out.println("Point: " + name + " (Type: " + type + ")");
//                    //   System.out.println("GUID: " + guid);
//                    //  System.out.println("City: " + cityName);
//                    //  System.out.println("Address: " + address);
//                    //  System.out.println("Phone: " + phone);
//                    // System.out.println();
//                }
//            } else {
//                // Обработка ошибки, если необходимо
//                String error = jsonResponse.optString("error", "Unknown error");
//                System.out.println("Error: " + error);
//            }
       } catch (Exception e) {
            e.printStackTrace();
             String balance1 = points;
//
//
        }

//        String str = String.valueOf(result);
//        this.name = username;
//        this.points = str;


    }



    }



