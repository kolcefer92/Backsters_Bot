package io.kolcefer.BackstersBot.apiYtimes;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ItemList {

    @JsonProperty("menuItemGuid")
    private String menuItemGuid;
    @JsonProperty("menuTypeGuid")
    private String menuTypeGuid;
    private List<?> supplementList;
    @JsonProperty("goodsItemGuid")
    private String goodsItemGuid;
    @JsonProperty("priceWithDiscount")
    private Double priceWithDiscount;
    private int quantity;

    public ItemList(String menuItemGuid, String menuTypeGuid, List<?> supplementList, String goodsItemGuid, Double priceWithDiscount, int quantity) {
        this.menuItemGuid = menuItemGuid;
        this.menuTypeGuid = menuTypeGuid;
        this.supplementList = supplementList;
        this.goodsItemGuid = goodsItemGuid;
        this.priceWithDiscount = priceWithDiscount;
        this.quantity = quantity;
    }


}
