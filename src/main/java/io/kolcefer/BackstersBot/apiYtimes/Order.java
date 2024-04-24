package io.kolcefer.BackstersBot.apiYtimes;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.client.RestTemplate;

import java.util.List;

public class Order {

    private String guid;
    private String status;
    @JsonProperty("shopGuid")
    private String shopGuid;
    private String type;
    private Client client;
    @JsonProperty("itemList")
    private List<ItemList> itemList;
    private String comment;
    private Double paidValue;
    private boolean printFiscalCheck;
    private String printFiscalCheckEmail;


    public String sendOrder(Order order1, Client client, List<ItemList> list){
        System.out.println("попали в sendOrder");


        // Создаем объект RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // URL для отправки POST-запроса
        String url = "https://api.ytimes.ru/ex/order/save/";



        try {
            // Преобразуем объект в JSON
            ObjectMapper objectMapper = new ObjectMapper();


           // Order order1 = new Order(null,null, "a4c346cd-7ad0-425b-abbb-b950f83ac653","TOGO", client,list,null,null,false,null);
            String requestBody = objectMapper.writeValueAsString(order1);

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
//            JsonNode rootNode = objectMapper.readTree(response.getBody());
//
//// Извлечение данных из JSON
//            String name = rootNode.get("rows").get(0).get("name").asText();
//            String surname = rootNode.get("rows").get(0).get("surname").asText();
//            float points = (float) rootNode.get("rows").get(0).get("points").asDouble();
//
//            // Создание объекта ClientData и заполнение его данными
//            // ClientData clientData = new ClientData();
//
//            clientData.setName(name);
//            clientData.setSurname(surname);
//            clientData.setPoints(points);
//
//
//// Вывод данных
//            System.out.println("Name: " + name);
//            System.out.println("Points: " + points);

            return response.getBody();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public Order(String guid, String status, String shopGuid, String type, Client client, List<ItemList> itemList, String comment, Double paidValue, boolean printFiscalCheck, String printFiscalCheckEmail) {
        this.guid = guid;
        this.status = status;
        this.shopGuid = shopGuid;
        this.type = type;
        this.client = client;
        this.itemList = itemList;
        this.comment = comment;
        this.paidValue = paidValue;
        this.printFiscalCheck = printFiscalCheck;
        this.printFiscalCheckEmail = printFiscalCheckEmail;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShopGuid() {
        return shopGuid;
    }

    public void setShopGuid(String shopGuid) {
        this.shopGuid = shopGuid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<ItemList> getItemList() {
        return itemList;
    }

    public void setItemList(List<ItemList> itemList) {
        this.itemList = itemList;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Double getPaidValue() {
        return paidValue;
    }

    public void setPaidValue(Double paidValue) {
        this.paidValue = paidValue;
    }

    public boolean isPrintFiscalCheck() {
        return printFiscalCheck;
    }

    public void setPrintFiscalCheck(boolean printFiscalCheck) {
        this.printFiscalCheck = printFiscalCheck;
    }

    public String getPrintFiscalCheckEmail() {
        return printFiscalCheckEmail;
    }

    public void setPrintFiscalCheckEmail(String printFiscalCheckEmail) {
        this.printFiscalCheckEmail = printFiscalCheckEmail;
    }
}

