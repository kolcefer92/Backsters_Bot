package io.kolcefer.BackstersBot.apiYtimes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;
import java.util.Map;

@Data
@Component
public class ItemList {

    @JsonProperty("menuItemGuid")
    private String menuItemGuid;
    @JsonProperty("menuTypeGuid")
    private String menuTypeGuid;
    @JsonProperty("supplementList")
    private Map<String,Integer> supplementList;
    @JsonProperty("goodsItemGuid")
    private String goodsItemGuid;
    @JsonProperty("priceWithDiscount")
    private Double priceWithDiscount;
    @JsonProperty("quantity")
    private int quantity;


    public void allFalse(){

        this.menuItemGuid = null;
        this.menuTypeGuid = null;
        this.supplementList = null;
        this.goodsItemGuid = null;
        this.priceWithDiscount = 0.0;
        this.quantity = 0;


    }

//    public ItemList(String menuItemGuid, String menuTypeGuid, List<?> supplementList, String goodsItemGuid, Double priceWithDiscount, int quantity) {
//        this.menuItemGuid = menuItemGuid;
//        this.menuTypeGuid = menuTypeGuid;
//        this.supplementList = supplementList;
//        this.goodsItemGuid = goodsItemGuid;
//        this.priceWithDiscount = priceWithDiscount;
//        this.quantity = quantity;
//    }


}


//
//
//    var supplementCaramel = new InlineKeyboardButton();
//    String settext1 = "Карамель";
//        supplementCaramel.setText(settext1);
//                supplementCaramel.setCallbackData("supplementCaramel");
//                rowInline1.add(supplementCaramel);
//                rowsInline.add(rowInline1);
//
//                var supplementVanil = new InlineKeyboardButton();
//                String settext2 = "Ваниль";
//                supplementVanil.setText(settext2);
//                supplementVanil.setCallbackData("supplementVanil");
//                rowInline2.add(supplementVanil);
//                rowsInline.add(rowInline2);
//
//                var supplementLesnoyOreh = new InlineKeyboardButton();
//                String settext3 = "Лесной орех";
//                supplementLesnoyOreh.setText(settext3);
//                supplementLesnoyOreh.setCallbackData("supplementLesnoyOreh");
//                rowInline3.add(supplementLesnoyOreh);
//                rowsInline.add(rowInline3);
//
//                var supplementKokos = new InlineKeyboardButton();
//                String settext4 = "Кокос";
//                supplementKokos.setText(settext4);
//                supplementKokos.setCallbackData("supplementKokos");
//                rowInline4.add(supplementKokos);
//                rowsInline.add(rowInline4);
//
//                var supplementBanan = new InlineKeyboardButton();
//                String settext5 = "Банан Уайт";
//                supplementBanan.setText(settext5);
//                supplementBanan.setCallbackData("supplementBanan");
//                rowInline5.add(supplementBanan);
//                rowsInline.add(rowInline5);
//
//                var supplementShokCoocies = new InlineKeyboardButton();
//                String settext6 = "Шоколадное печенье";
//                supplementShokCoocies.setText(settext6);
//                supplementShokCoocies.setCallbackData("supplementShokCoocies");
//                rowInline6.add(supplementShokCoocies);
//                rowsInline.add(rowInline6);
//
//                var supplementMindal = new InlineKeyboardButton();
//                String settext7 = "Миндаль";
//                supplementMindal.setText(settext7);
//                supplementMindal.setCallbackData("supplementMindal");
//                rowInline7.add(supplementMindal);
//                rowsInline.add(rowInline7);
//
//
//                var supplementSaltedCaramel = new InlineKeyboardButton();
//                String settext8 = "Сол. Карамель";
//                supplementSaltedCaramel.setText(settext8);
//                supplementSaltedCaramel.setCallbackData("supplementSaltedCaramel");
//                rowInline8.add(supplementSaltedCaramel);
//                rowsInline.add(rowInline8);