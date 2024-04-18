package io.kolcefer.BackstersBot.apiYtimes;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor


class ClientInfo {
    // Аннотации для Jackson для указания имени полей в JSON
    @JsonProperty("phoneCode")
    private String phoneCode;

    @JsonProperty("phone")
    private String phone;

    public ClientInfo(String phoneCode, String phone) {
        this.phoneCode = phoneCode;
        this.phone = phone;
    }

}