package io.kolcefer.BackstersBot.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "SupplementList")

public class Supplement {
    @Id
    private String name;

    private String guidSupplement;

    public Supplement(String name, String guidSupplement) {
        this.name = name;
        this.guidSupplement = guidSupplement;
    }

    public Supplement(){

    }
}
