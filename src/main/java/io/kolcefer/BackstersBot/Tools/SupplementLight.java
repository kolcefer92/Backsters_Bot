package io.kolcefer.BackstersBot.Tools;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SupplementLight {
    boolean supplementCaramel = false;
    boolean supplementVanila = false;
    boolean supplementLesnoyOreh =false;
    boolean supplementKokos = false;
    boolean supplementBanan = false;
    boolean supplementShokCoocies = false;
    boolean supplementMindal = false;
    boolean supplementSaltedCaramel = false;
    boolean supplementejevika = false;
    boolean supplementamaretto = false;

    boolean supplementcoconuMilk = false;
    boolean supplementhazelnutMilk = false;
    boolean supplementSugar = false;
    boolean supplementCinamon = false;

    public void allFalse(){
        this.supplementCaramel = false;
        this.supplementVanila = false;
        this.supplementLesnoyOreh =false;
        this.supplementKokos = false;
        this.supplementBanan = false;
        this.supplementShokCoocies = false;
        this.supplementMindal = false;
        this.supplementSaltedCaramel = false;
        this.supplementejevika = false;
        this.supplementamaretto = false;
    }

    public  void altAllFalse(){

        this.supplementcoconuMilk = false;
        this.supplementhazelnutMilk = false;
        this.supplementSugar = false;
        this.supplementCinamon = false;

    }

    public void milkAllFalse(){
        this.supplementcoconuMilk = false;
        this.supplementhazelnutMilk = false;

    }




}
