package io.kolcefer.BackstersBot.apiYtimes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
@Data
@Component
public class ItemList {

    @JsonProperty("menuItemGuid")
    private String menuItemGuid;
    @JsonProperty("menuTypeGuid")
    private String menuTypeGuid;
    @JsonProperty("supplementList")
    private List<?> supplementList;
    @JsonProperty("goodsItemGuid")
    private String goodsItemGuid;
    @JsonProperty("priceWithDiscount")
    private Double priceWithDiscount;
    @JsonProperty("quantity")
    private int quantity;

//    public ItemList(String menuItemGuid, String menuTypeGuid, List<?> supplementList, String goodsItemGuid, Double priceWithDiscount, int quantity) {
//        this.menuItemGuid = menuItemGuid;
//        this.menuTypeGuid = menuTypeGuid;
//        this.supplementList = supplementList;
//        this.goodsItemGuid = goodsItemGuid;
//        this.priceWithDiscount = priceWithDiscount;
//        this.quantity = quantity;
//    }


}
