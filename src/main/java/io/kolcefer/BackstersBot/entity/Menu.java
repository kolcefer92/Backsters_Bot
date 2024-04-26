package io.kolcefer.BackstersBot.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "TableMenu2")

public class Menu {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @Id
    private String guid_category;
    private String guid_250;
    private int price_250;
    private String guid_350;
    private int price_350;
    private String guid_450;
    private int price_450;

    public Menu(String name, String guid_category, String guid_250, int price_250, String guid_350, int price_350, String guid_450, int price_450) {

        this.name = name;
        this.guid_category = guid_category;
        this.guid_250 = guid_250;
        this.price_250 = price_250;
        this.guid_350 = guid_350;
        this.price_350 = price_350;
        this.guid_450 = guid_450;
        this.price_450 = price_450;
    }

    public Menu(){

    }
}


