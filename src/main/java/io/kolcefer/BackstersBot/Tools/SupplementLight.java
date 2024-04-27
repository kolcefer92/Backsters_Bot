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

    public void allFalse(){
        this.supplementCaramel = false;
    }




}
