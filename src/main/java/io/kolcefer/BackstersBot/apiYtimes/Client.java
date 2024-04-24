package io.kolcefer.BackstersBot.apiYtimes;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Client {
    @JsonProperty("name")
    private String name;
    @JsonProperty("cardNumber")
    private String cardNumber;
    @JsonProperty("phoneCode")
    private String phoneCode;
    @JsonProperty("phone")
    private String phone;
    private String email;

    public Client(String name, String cardNumber, String phoneCode, String phone, String email) {
        this.name = name;
        this.cardNumber = cardNumber;
        this.phoneCode = phoneCode;
        this.phone = phone;
        this.email = email;
    }
}
