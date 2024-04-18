package io.kolcefer.BackstersBot.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigInteger;

@Getter
@Setter
@Entity
@Table(name = "UserTable")

public class Users {

            //  @GeneratedValue(strategy = GenerationType.IDENTITY)
   //private int id;
            @Id
    private long chatID;

    private String name;

    private String surName;

    private String phoneNumber;

public Users(){

}


    public Users( long chatID, String name, String surName,String  phoneNumber) {

        this.chatID = chatID;
        this.name = name;
        this.surName = surName;
        this.phoneNumber = phoneNumber;
    }
}
