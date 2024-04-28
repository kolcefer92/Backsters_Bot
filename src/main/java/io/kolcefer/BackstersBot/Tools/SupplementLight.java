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

    public void allFalse(){
        this.supplementCaramel = false;
        this.supplementVanila = false;
        this.supplementLesnoyOreh =false;
    }




}
