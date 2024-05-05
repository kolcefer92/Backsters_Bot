package io.kolcefer.BackstersBot.service;



import io.kolcefer.BackstersBot.Tools.SupplementLight;
import io.kolcefer.BackstersBot.apiYtimes.*;
import io.kolcefer.BackstersBot.config.BotConfig;

import io.kolcefer.BackstersBot.entity.Menu;
import io.kolcefer.BackstersBot.entity.Users;
import io.kolcefer.BackstersBot.repository.MenuRepo;
import io.kolcefer.BackstersBot.repository.UserRepo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringVersion;
import org.springframework.expression.spel.ast.NullLiteral;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScope;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Data

public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig config;

    public TelegramBot(BotConfig config) {
        this.config = config;
        List<BotCommand> listOfStart = new ArrayList<>();
        listOfStart.add(new BotCommand("/start","Нажмите для входа в главное меню"));
        try {
            execute(new SetMyCommands(listOfStart, new BotCommandScopeDefault(), null));
        }
        catch (TelegramApiException e){
            System.out.println(e);
        }
    }

    @Autowired
    ClientData clientData;

    @Autowired
    CardInfoGetBalance cardInfoGetBalance;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MenuRepo menuRepo;

    @Autowired
    Order order;

    @Autowired
    ItemList itemList;

    @Autowired
    Client client;

    @Autowired
    SupplementLight supplementLight;






    public  Map<Long, Integer> userStates = new HashMap<>();
    public  Map<String, Integer> supplements = new HashMap<>();
    public  Map<String, Integer> altMilkAndSugar = new HashMap<>();
    public int costSupplements = 0;
    List<ItemList> items = new ArrayList<>();
    public static String supplementText = "Ваш заказ - ";

    @Override

    public void onUpdateReceived(Update update) {


        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();



            switch (messageText) {
                case "/start":


                    String msg = "Добро пожаловть в бот Backster`s Coffee";
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    if (userRepo.findById(chatId).isEmpty()){

                        sendMessageNonRegistred(chatId, msg);
                    } else {
                        sendMessageRegistred(chatId,msg);

                    }

                    break;



                case "/balance":

                    sendMessageRegistred(chatId,String.valueOf(cardInfoGetBalance.getBalance(userRepo.findById(chatId)
                            .get()
                            .getPhoneNumber()).
                            getPoints()));


                    break;



                case "/logout":
                    userRepo.deleteById(chatId);
                    sendMessageNonRegistred(chatId,"Вы разлогинились!");

                    break;

                case "/registration":

                    userStates.put(chatId, 1);
                    sendMessage(chatId, "Пожалуйста, введите ваш номер телефона:");

                    return;

                case "/newOrder":
                 //   Order order = new Order(null,null,null,null,null,null,null,0.0,false,null);
                  //  Order order1 = new Order(null,null, "a4c346cd-7ad0-425b-abbb-b950f83ac653","TOGO", client,item1,null,null,false,null);
                   // order.setGuid(null);
                   // order.setStatus(null);
                    order.setShopGuid("a4c346cd-7ad0-425b-abbb-b950f83ac653");
                    order.setType("TOGO");

                    client.setPhoneCode("+7");
                    client.setPhone(userRepo.findById(chatId).get().getPhoneNumber());
                    client.setCardNumber(userRepo.findById(chatId).get().getPhoneNumber());
                    client.setName(userRepo.findById(chatId).get().getName());

                    order.setClient(client);





                    System.out.println("neworder");
//                   Users user =  userRepo.findById(chatId).get();
//
//                    Client client = new Client(user.getName(),user.getPhoneNumber(),"+7",user.getPhoneNumber(),null);
//                    System.out.println(user.getName()+user.getPhoneNumber()+"+7"+user.getPhoneNumber());
//
//                    ItemList itemList = new ItemList("2ee40cbd-9d1e-463b-b61c-9731579e1034","4475cf55-0dfb-4fb4-a110-fa61f7c87b81",null,null,100.0,3);
//                    List<ItemList> item1 = new ArrayList<>();
//                    item1.add(itemList);
//                    Order order1 = new Order(null,null, "a4c346cd-7ad0-425b-abbb-b950f83ac653","TOGO", client,item1,null,null,false,null);
//
//                  String str1 = order1.sendOrder(order1, client,item1);
//                    System.out.println(str1);

                    createOrder(chatId);

                    break;

                default:

                    System.out.println("Sorry, command was not recognized");
            }

            if (!userStates.containsKey(chatId)) {
                return;
            }

            int state = userStates.get(chatId);
            switch (state) {
                case 1:
                    String phoneNumber = messageText;

//                    // Здесь можно добавить логику обработки номера телефона
//                    // Например, сохранить его в базе данных или использовать для каких-то операций
//                    // После обработки номера можно удалить состояние ожидания для этого пользователя
                    userStates.remove(chatId);
                    sendMessageRegistred(chatId, "Спасибо! Номер телефона сохранен.");

//                    card.getInfo(phoneNumber);
//                    sendMessage(chatId, card.getPoints());
//                    String s = card.getName();
                    clientData =  cardInfoGetBalance.getBalance(phoneNumber);

                    userRepo.save(new Users(chatId, clientData.getName(), clientData.getSurname(), phoneNumber));

                    break;

                default:
                    // Если состояние не определено, просто отправляем сообщение об ошибке
                    sendMessage(chatId, "Произошла ошибка.");
                    break;
            }


        }
        else if (update.hasCallbackQuery()) {
            System.out.println("hasCallBack");
            String callbackData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            switch (callbackData){
                case ("classicMenu"):
                    String text = "Выберите напиток";
                    classiCoffeeEditText(text,chatId,messageId);

                    break;
                case ("Kapuchino"):
                    itemList.setMenuItemGuid("2ee40cbd-9d1e-463b-b61c-9731579e1034");
                    //  System.out.println(itemList.getMenuTypeGuid());
                    String textValuesKapuchino = "Выберите объем";


                    valuesDrink(textValuesKapuchino,chatId,messageId);

                    break;

                case ("FletUayt"):
                    itemList.setMenuItemGuid("cba009e4-655a-4951-9d57-86058a6dec3b");
                    String textValuesFletUayt = "Выберите объем";


                    valuesDrink(textValuesFletUayt,chatId,messageId);

                    break;
                case ("Raf"):
                    String textValuesRaf = "Выберите объем";
                    itemList.setMenuItemGuid("930c7711-4667-405d-b36f-5d7ee5e291df");


                    valuesDrink(textValuesRaf,chatId,messageId);

                    break;
                case ("Mokko"):
                    String textValuesMokko = "Выберите объем";
                    itemList.setMenuItemGuid("23aa31da-c475-4140-8e0c-707729f41ce1");


                    valuesDrink(textValuesMokko,chatId,messageId);

                    break;
                case ("Latte"):
                    String textValuesLatte = "Выберите объем";
                    itemList.setMenuItemGuid("7ce68078-b651-41a1-8060-6129c6957543");


                    valuesDrink(textValuesLatte,chatId,messageId);

                    break;
                case ("Americano"):
                    String textValuesAmericano = "Выберите объем";
                    itemList.setMenuItemGuid("bde9729c-d876-4df8-bf5a-74ffe0025f6f");


                    valuesDrink(textValuesAmericano,chatId,messageId);

                    break;


                case ("smallValues"):

                    itemList.setMenuTypeGuid(menuRepo.findById(itemList.getMenuItemGuid()).get().getGuid_250());
                    itemList.setPriceWithDiscount((double)menuRepo.findById(itemList.getMenuItemGuid()).get().getPrice_250());
                    order.setComment("250мл");


//                    // itemList.setQuantity(1);
//                    List<ItemList> items = new ArrayList<>();
//                    items.add(itemList);
//                    order.setItemList(items);

                    String textsmallValues = "Выберите сироп для "+menuRepo.findById(itemList.getMenuItemGuid()).get().getName();
                    supplementList(textsmallValues,chatId,messageId);

                    break;

                case ("mediumValues"):




                    itemList.setMenuTypeGuid(menuRepo.findById(itemList.getMenuItemGuid()).get().getGuid_350());
                    itemList.setPriceWithDiscount((double)menuRepo.findById(itemList.getMenuItemGuid()).get().getPrice_350());
                    order.setComment("350мл");
                    String textmediumValues = "Выберите сироп для "+menuRepo.findById(itemList.getMenuItemGuid()).get().getName();


                    supplementList(textmediumValues,chatId,messageId);

                    break;

                case ("largelValues"):



                    itemList.setMenuTypeGuid(menuRepo.findById(itemList.getMenuItemGuid()).get().getGuid_450());
                    itemList.setPriceWithDiscount((double)menuRepo.findById(itemList.getMenuItemGuid()).get().getPrice_450());
                    order.setComment("450мл");
                    String textlargelValues  = "Выберите сироп для "+menuRepo.findById(itemList.getMenuItemGuid()).get().getName();
                    supplementList(textlargelValues,chatId,messageId);

                    break;



                case ("supplementCaramel"):
                    System.out.println("попали в обработчик карамели");


                    if(supplements.containsKey("051e3f31-c789-4ae5-9f52-d406282549bd")){
                        //int i = supplements.get("051e3f31-c789-4ae5-9f52-d406282549bd") + 1;
                        supplementLight.allFalse();
                        supplements.remove("051e3f31-c789-4ae5-9f52-d406282549bd");
                    //    itemList.setPriceWithDiscount(itemList.getPriceWithDiscount()-30);
                       // supplements.put("051e3f31-c789-4ae5-9f52-d406282549bd",i);

                    }
                    else {
                        supplementLight.allFalse();
                        supplements.clear();
                        supplements.put("051e3f31-c789-4ae5-9f52-d406282549bd", 30);
                      //  itemList.setPriceWithDiscount(itemList.getPriceWithDiscount()+30);
                        supplementLight.setSupplementCaramel(true);


                    }
                    //если лампочка не горит, запускаем метод выкл все, потом вкл карамель лампу.
                    //если лампа горит, то просто выкл все
                    //тут же очищаем мапу
                    //а если не горит, очищаем мапу и добавляем карамель

                    //String supplement1 = supplementText;
                    String supplementText = "Выберите добавки для "+menuRepo.findById(itemList.getMenuItemGuid()).get().getName();

                    supplementList(supplementText,chatId,messageId);
                    //System.out.println(supplements.get("051e3f31-c789-4ae5-9f52-d406282549bd"));

                    break;


                case ("supplementVanila"):
                    if(supplements.containsKey("ebfd782f-bbe7-4a4b-9926-3fc5dc8c574b")){
                        supplementLight.allFalse();
                        supplements.remove("ebfd782f-bbe7-4a4b-9926-3fc5dc8c574b");
                        //itemList.setPriceWithDiscount(itemList.getPriceWithDiscount()-30);

                    }
                    else {
                        supplementLight.allFalse();
                        supplements.clear();
                        supplements.put("ebfd782f-bbe7-4a4b-9926-3fc5dc8c574b", 30);
                       // itemList.setPriceWithDiscount(itemList.getPriceWithDiscount()+30);
                        supplementLight.setSupplementVanila(true);

                    }
                    String supplementText1 = "Выберите добавки для "+menuRepo.findById(itemList.getMenuItemGuid()).get().getName();

                    supplementList(supplementText1,chatId,messageId);


                    break;

                case ("supplementLesnoyOreh"):
                    if(supplements.containsKey("f9f16b54-a3b3-401c-bd61-e7c4bbe7f090")){
                        supplementLight.allFalse();
                        supplements.remove("f9f16b54-a3b3-401c-bd61-e7c4bbe7f090");
                        //itemList.setPriceWithDiscount(itemList.getPriceWithDiscount()-30);

                    }
                    else {
                        supplementLight.allFalse();
                        supplements.clear();
                        supplements.put("f9f16b54-a3b3-401c-bd61-e7c4bbe7f090", 30);
                       // itemList.setPriceWithDiscount(itemList.getPriceWithDiscount()+30);
                        supplementLight.setSupplementLesnoyOreh(true);

                    }
                    String supplementText2 = "Выберите добавки для "+menuRepo.findById(itemList.getMenuItemGuid()).get().getName();

                    supplementList(supplementText2,chatId,messageId);


                    break;

                case ("supplementKokos"):
                    if(supplements.containsKey("a030d6cf-41d4-4b78-85ca-cc17d9327d66")){
                        supplementLight.allFalse();
                        //itemList.setPriceWithDiscount(itemList.getPriceWithDiscount()-30);
                        supplements.remove("a030d6cf-41d4-4b78-85ca-cc17d9327d66");

                    }
                    else {
                        supplementLight.allFalse();
                        supplements.clear();
                        supplements.put("a030d6cf-41d4-4b78-85ca-cc17d9327d66", 30);
                      //  itemList.setPriceWithDiscount(itemList.getPriceWithDiscount()+30);
                        supplementLight.setSupplementKokos(true);

                    }
                    String supplementText3 = "Выберите добавки для "+menuRepo.findById(itemList.getMenuItemGuid()).get().getName();

                    supplementList(supplementText3,chatId,messageId);


                    break;

                case ("supplementBanan"):
                    if(supplements.containsKey("4bfc298c-3bcc-4f34-9b97-5364d051c000")){
                      // itemList.setPriceWithDiscount(itemList.getPriceWithDiscount()-30);
                        supplementLight.allFalse();
                        supplements.remove("4bfc298c-3bcc-4f34-9b97-5364d051c000");

                    }
                    else {
                        supplementLight.allFalse();
                        supplements.clear();
                        supplements.put("4bfc298c-3bcc-4f34-9b97-5364d051c000", 30);
                       // itemList.setPriceWithDiscount(itemList.getPriceWithDiscount()+30);
                        supplementLight.setSupplementBanan(true);

                    }
                    String supplementText4 = "Выберите добавки для "+menuRepo.findById(itemList.getMenuItemGuid()).get().getName();

                    supplementList(supplementText4,chatId,messageId);


                    break;


                case ("supplementShokCoocies"):
                    if(supplements.containsKey("e2a6237e-6c7f-4306-9f41-fbdcf5ebe0ea")){
                        supplementLight.allFalse();
                        //itemList.setPriceWithDiscount(itemList.getPriceWithDiscount()-30);
                        supplements.remove("e2a6237e-6c7f-4306-9f41-fbdcf5ebe0ea");

                    }
                    else {
                        supplementLight.allFalse();
                        supplements.clear();
                        supplements.put("e2a6237e-6c7f-4306-9f41-fbdcf5ebe0ea", 30);
                       // itemList.setPriceWithDiscount(itemList.getPriceWithDiscount()+30);
                        supplementLight.setSupplementShokCoocies(true);

                    }
                    String supplementText5 = "Выберите добавки для "+menuRepo.findById(itemList.getMenuItemGuid()).get().getName();

                    supplementList(supplementText5,chatId,messageId);


                    break;

                case ("supplementMindal"):
                    if(supplements.containsKey("d33f05a1-1dbe-4e1e-874b-1bcf5d42b851")){
                        supplementLight.allFalse();
                       // itemList.setPriceWithDiscount(itemList.getPriceWithDiscount()-30);
                        supplements.remove("d33f05a1-1dbe-4e1e-874b-1bcf5d42b851");

                    }
                    else {
                        supplementLight.allFalse();
                        supplements.clear();
                        supplements.put("d33f05a1-1dbe-4e1e-874b-1bcf5d42b851", 30);
                       // itemList.setPriceWithDiscount(itemList.getPriceWithDiscount()+30);
                        supplementLight.setSupplementMindal(true);

                    }
                    String supplementText6 = "Выберите добавки для "+menuRepo.findById(itemList.getMenuItemGuid()).get().getName();

                    supplementList(supplementText6,chatId,messageId);


                    break;

                case ("supplementSaltedCaramel"):
                    if(supplements.containsKey("1aa3d91c-84cc-4b24-ba08-773f7ee0ea22")){
                        supplementLight.allFalse();
                        //itemList.setPriceWithDiscount(itemList.getPriceWithDiscount()-30);
                        supplements.remove("1aa3d91c-84cc-4b24-ba08-773f7ee0ea22");

                    }
                    else {
                        supplementLight.allFalse();
                        supplements.clear();
                        supplements.put("1aa3d91c-84cc-4b24-ba08-773f7ee0ea22", 30);
                        //itemList.setPriceWithDiscount(itemList.getPriceWithDiscount()+30);
                        supplementLight.setSupplementSaltedCaramel(true);

                    }
                    String supplementText7 = "Выберите добавки для "+menuRepo.findById(itemList.getMenuItemGuid()).get().getName();

                    supplementList(supplementText7,chatId,messageId);


                    break;

                case ("supplementejevika"):
                    if(supplements.containsKey("64f5aa42-1fd7-45f4-ab3c-2bbaf56bbe41")){
                        supplementLight.allFalse();
                       // itemList.setPriceWithDiscount(itemList.getPriceWithDiscount()-30);
                        supplements.remove("64f5aa42-1fd7-45f4-ab3c-2bbaf56bbe41");

                    }
                    else {
                        supplementLight.allFalse();
                        supplements.clear();
                        supplements.put("64f5aa42-1fd7-45f4-ab3c-2bbaf56bbe41", 30);
                       // itemList.setPriceWithDiscount(itemList.getPriceWithDiscount()+30);
                        supplementLight.setSupplementejevika(true);

                    }
                    String supplementText8 = "Выберите добавки для "+menuRepo.findById(itemList.getMenuItemGuid()).get().getName();

                    supplementList(supplementText8,chatId,messageId);


                    break;

                case ("supplementamaretto"):
                    if(supplements.containsKey("e40ec0d7-dc1d-441c-8fda-9dff8f6b9c87")){
                        supplementLight.allFalse();
                        //itemList.setPriceWithDiscount(itemList.getPriceWithDiscount()-30);
                        supplements.remove("e40ec0d7-dc1d-441c-8fda-9dff8f6b9c87");

                    }
                    else {
                        supplementLight.allFalse();
                        supplements.clear();
                        supplements.put("e40ec0d7-dc1d-441c-8fda-9dff8f6b9c87", 30);
                        //itemList.setPriceWithDiscount(itemList.getPriceWithDiscount()+30);
                        supplementLight.setSupplementamaretto(true);

                    }
                    String supplementText9 = "Выберите добавки для "+menuRepo.findById(itemList.getMenuItemGuid()).get().getName();

                    supplementList(supplementText9,chatId,messageId);


                    break;



                case ("supplementCoconutMilk"):
                    if(altMilkAndSugar.containsKey("059013f4-04ab-4008-9858-cfe3792d23e1")){
                        supplementLight.setSupplementcoconuMilk(false);
                       // itemList.setPriceWithDiscount(itemList.getPriceWithDiscount()-90);
                        altMilkAndSugar.remove("059013f4-04ab-4008-9858-cfe3792d23e1");

                    }
                    else {
                        supplementLight.milkAllFalse();
                        if(altMilkAndSugar.containsKey("1a7a6f45-96ca-4a2a-a545-3e543e477593")) {
                            altMilkAndSugar.remove("1a7a6f45-96ca-4a2a-a545-3e543e477593");
                            altMilkAndSugar.put("059013f4-04ab-4008-9858-cfe3792d23e1", 90);
                            // itemList.setPriceWithDiscount(itemList.getPriceWithDiscount()+90);
                            supplementLight.setSupplementcoconuMilk(true);
                        }else {

                            altMilkAndSugar.put("059013f4-04ab-4008-9858-cfe3792d23e1", 90);
                            // itemList.setPriceWithDiscount(itemList.getPriceWithDiscount()+90);
                            supplementLight.setSupplementcoconuMilk(true);
                        }

                    }
                    String supplementText10 = "Выберите добавки для "+menuRepo.findById(itemList.getMenuItemGuid()).get().getName();

                    supplementList(supplementText10,chatId,messageId);


                    break;


                case ("supplementHazelnutMilk"):
                    if(altMilkAndSugar.containsKey("1a7a6f45-96ca-4a2a-a545-3e543e477593")){
                        supplementLight.setSupplementhazelnutMilk(false);
                        //itemList.setPriceWithDiscount(itemList.getPriceWithDiscount()-90);
                        altMilkAndSugar.remove("1a7a6f45-96ca-4a2a-a545-3e543e477593");

                    }
                    else {
                        supplementLight.milkAllFalse();
                        if(altMilkAndSugar.containsKey("059013f4-04ab-4008-9858-cfe3792d23e1")) {
                            altMilkAndSugar.remove("059013f4-04ab-4008-9858-cfe3792d23e1");
                            altMilkAndSugar.put("1a7a6f45-96ca-4a2a-a545-3e543e477593", 90);
                            //itemList.setPriceWithDiscount(itemList.getPriceWithDiscount()+90);
                            supplementLight.setSupplementhazelnutMilk(true);
                        }else {
                            altMilkAndSugar.put("1a7a6f45-96ca-4a2a-a545-3e543e477593", 90);
                            //itemList.setPriceWithDiscount(itemList.getPriceWithDiscount()+90);
                            supplementLight.setSupplementhazelnutMilk(true);
                        }

                    }
                    String supplementText11 = "Выберите добавки для "+menuRepo.findById(itemList.getMenuItemGuid()).get().getName();

                    supplementList(supplementText11,chatId,messageId);


                    break;


                case ("supplementShugar"):
                    if(altMilkAndSugar.containsKey("45c3df30-e87c-4124-a52c-0ba27e97b596")){
                        supplementLight.setSupplementSugar(false);
                        //itemList.setPriceWithDiscount(itemList.getPriceWithDiscount()-90);
                        altMilkAndSugar.remove("45c3df30-e87c-4124-a52c-0ba27e97b596");

                    }
                    else {
                        altMilkAndSugar.put("45c3df30-e87c-4124-a52c-0ba27e97b596", 0);
                      //  itemList.setPriceWithDiscount(itemList.getPriceWithDiscount()+90);
                        supplementLight.setSupplementSugar(true);

                    }
                    String supplementText12 = "Выберите добавки для "+menuRepo.findById(itemList.getMenuItemGuid()).get().getName();

                    supplementList(supplementText12,chatId,messageId);


                    break;

                case ("supplementKorica"):
                    if(altMilkAndSugar.containsKey("08686be7-eaa4-491a-a25e-0df546565cb9")){
                        supplementLight.setSupplementCinamon(false);
                        //itemList.setPriceWithDiscount(itemList.getPriceWithDiscount()-90);
                        altMilkAndSugar.remove("08686be7-eaa4-491a-a25e-0df546565cb9");

                    }
                    else {
                        altMilkAndSugar.put("08686be7-eaa4-491a-a25e-0df546565cb9", 0);
                        //  itemList.setPriceWithDiscount(itemList.getPriceWithDiscount()+90);
                        supplementLight.setSupplementCinamon(true);

                    }
                    String supplementText13 = "Выберите добавки для "+menuRepo.findById(itemList.getMenuItemGuid()).get().getName();

                    supplementList(supplementText13,chatId,messageId);


                    break;







                case ("dalee"):
//                    if(!supplements.isEmpty()){
//                        itemList.setPriceWithDiscount(itemList.getPriceWithDiscount()+30);
//                        System.out.println(supplements);
//                        System.out.println(itemList.getPriceWithDiscount());
//                    }


                    for (Map.Entry<String,Integer> map1 : supplements.entrySet()){
                        costSupplements = costSupplements + map1.getValue();

                    }
                    for (Map.Entry<String,Integer> map2 : altMilkAndSugar.entrySet()){
                        costSupplements = costSupplements + map2.getValue();

                    }
                    //itemList.setPriceWithDiscount(itemList.getPriceWithDiscount()+costSupplements);
                    //double j = itemList.getPriceWithDiscount();






                    countCup(chatId,messageId,costSupplements);


                    break;


                case ("back_supplements"):
                    String supplementText14 = "Выберите добавки для "+menuRepo.findById(itemList.getMenuItemGuid()).get().getName();
                   supplementLight.allFalse();
                   supplements.clear();
                   altMilkAndSugar.clear();
                   supplementLight.altAllFalse();
                   costSupplements = 0;

                    supplementList(supplementText14,chatId,messageId);

                    break;


                case ("ONE_CUP"):

                    itemList.setPriceWithDiscount(itemList.getPriceWithDiscount()+costSupplements);
                    itemList.setQuantity(1);
                    Map<String,Integer> map = new HashMap<>();

                    for(Map.Entry<String,Integer> map1:supplements.entrySet()){
                        map.put(map1.getKey(),2);
                    }

                    for(Map.Entry<String,Integer> map2:altMilkAndSugar.entrySet()){
                        map.put(map2.getKey(),1);
                    }
                    itemList.setSupplementList(map);


                    items.add(itemList);
                    order.setItemList(items);

                    aproveSend(chatId,messageId);




                    break;

                case ("TWO_CUP"):

                    itemList.setPriceWithDiscount(itemList.getPriceWithDiscount()+costSupplements);
                    itemList.setQuantity(2);
                    Map<String,Integer> map2 = new HashMap<>();

                    for(Map.Entry<String,Integer> map1:supplements.entrySet()){
                        map2.put(map1.getKey(),2);
                    }

                    for(Map.Entry<String,Integer> map3:altMilkAndSugar.entrySet()){
                        map2.put(map3.getKey(),1);
                    }
                    itemList.setSupplementList(map2);


                    items.add(itemList);
                    order.setItemList(items);

                    aproveSend(chatId,messageId);


                    break;


                case ("THREE_CUP"):

                    itemList.setPriceWithDiscount(itemList.getPriceWithDiscount()+costSupplements);
                    itemList.setQuantity(3);
                    Map<String,Integer> map4 = new HashMap<>();

                    for(Map.Entry<String,Integer> map5:supplements.entrySet()){
                        map4.put(map5.getKey(),2);
                    }

                    for(Map.Entry<String,Integer> map6:altMilkAndSugar.entrySet()){
                        map4.put(map6.getKey(),1);
                    }
                    itemList.setSupplementList(map4);


                    items.add(itemList);
                    order.setItemList(items);

                    aproveSend(chatId,messageId);


                    break;

                case ("Yes_send"):

                    order.sendOrder(order);

                    supplementLight.allFalse();
                    supplements.clear();
                    altMilkAndSugar.clear();
                    supplementLight.altAllFalse();
                    costSupplements = 0;
                    items.clear();
                    itemList.allFalse();

                    String textToSend = "Ваш заказ отпарвлен в кофейню";

                    editMessageText(chatId,messageId,textToSend);



                    break;


                case ("No_send"):

                    //order.sendOrder(order);

                    supplementLight.allFalse();
                    supplements.clear();
                    altMilkAndSugar.clear();
                    supplementLight.altAllFalse();
                    costSupplements = 0;
                    items.clear();
                    itemList.allFalse();
                    //order = null;
                    System.out.println(order.getComment());

                    String textToSendNo = "Ваш заказ отменен";

                    editMessageText(chatId,messageId,textToSendNo);




                    break;





            }

//            if(callbackData.equals("classicMenu")){
//                //new order(null,null,null,null,null,null,null,0.0,false,null);
//
//                System.out.println("equals кофе");
//                String text = "Выберите напиток";
//
//                try {
//                    executeEditMessageText(text, chatId, messageId);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            else if(callbackData.equals("authorMenu")){
//                order.setGuid("sss");
//                System.out.println(order.getGuid());
//                String text = "You pressed NO button";
//                executeEditMessageText(text, chatId, messageId);
//            }
//            else if(callbackData.equals("Не кофе")){
//
//            }
        }


    }

    public void executeMessage(SendMessage message){
        try {
            execute(message);
        } catch (TelegramApiException e) {
            // log.error(ERROR_TEXT + e.getMessage());
        }
    }

    private void executeEditMessageText(String text, long chatId, long messageId){
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setMessageId((int) messageId);


        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();


        var CLASSIC_MENU = new InlineKeyboardButton();
        String settext1 = "Кофе";
        CLASSIC_MENU.setText(settext1);
        CLASSIC_MENU.setCallbackData("classicMenu");
        rowInline1.add(CLASSIC_MENU);

        rowsInline.add(rowInline1);


        keyboardMarkup.setKeyboard(rowsInline);

        message.setReplyMarkup(keyboardMarkup);




        try {
            execute(message);
        } catch (TelegramApiException e) {
            //log.error(ERROR_TEXT + e.getMessage());
        }
    }


    private void classiCoffeeEditText(String text, long chatId, long messageId){
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setMessageId((int) messageId);

        // Капучино Латте Раф Мокко Флет Уайт Американо supplement

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline3 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline4 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline5 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline6 = new ArrayList<>();


        var Kapuchino = new InlineKeyboardButton();
        String settext1 = "Капучино";
        Kapuchino.setText(settext1);
        Kapuchino.setCallbackData("Kapuchino");
        rowInline1.add(Kapuchino);
        rowsInline.add(rowInline1);

        var Latte = new InlineKeyboardButton();
        String settext2 = "Латте";
        Latte.setText(settext2);
        Latte.setCallbackData("Latte");
        rowInline2.add(Latte);
        rowsInline.add(rowInline2);

        var Raf = new InlineKeyboardButton();
        String settext3 = "Раф";
        Raf.setText(settext3);
        Raf.setCallbackData("Raf");
        rowInline3.add(Raf);
        rowsInline.add(rowInline3);

        var Mokko = new InlineKeyboardButton();
        String settext4 = "Мокко";
        Mokko.setText(settext4);
        Mokko.setCallbackData("Mokko");
        rowInline4.add(Mokko);
        rowsInline.add(rowInline4);

        var FletUayt = new InlineKeyboardButton();
        String settext5 = "Флет Уайт";
        FletUayt.setText(settext5);
        FletUayt.setCallbackData("FletUayt");
        rowInline5.add(FletUayt);
        rowsInline.add(rowInline5);

        var Americano = new InlineKeyboardButton();
        String settext6 = "Американо";
        Americano.setText(settext6);
        Americano.setCallbackData("Americano");
        rowInline6.add(Americano);
        rowsInline.add(rowInline6);



        keyboardMarkup.setKeyboard(rowsInline);
        message.setReplyMarkup(keyboardMarkup);




        try {
            execute(message);
        } catch (TelegramApiException e) {
            //log.error(ERROR_TEXT + e.getMessage());
        }
    }



    private void valuesDrink(String text, long chatId, long messageId){
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setMessageId((int) messageId);


        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline3 = new ArrayList<>();


        var smallValues = new InlineKeyboardButton();
        String settext1 = "250 мл";
        smallValues.setText(settext1);
        smallValues.setCallbackData("smallValues");
        rowInline1.add(smallValues);
        rowsInline.add(rowInline1);

        var mediumValues = new InlineKeyboardButton();
        String settext2 = "350 мл";
        mediumValues.setText(settext2);
        mediumValues.setCallbackData("mediumValues");
        rowInline2.add(mediumValues);
        rowsInline.add(rowInline2);

        var largeValues = new InlineKeyboardButton();
        String settext3 = "450 мл";
        largeValues.setText(settext3);
        largeValues.setCallbackData("largeValues");
        rowInline3.add(largeValues);
        rowsInline.add(rowInline3);

        keyboardMarkup.setKeyboard(rowsInline);
        message.setReplyMarkup(keyboardMarkup);


        try {
            execute(message);
        } catch (TelegramApiException e) {
            //log.error(ERROR_TEXT + e.getMessage());
        }
    }


    private void supplementList(String text, long chatId, long messageId){
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setMessageId((int) messageId);

        // Капучино Латте Раф Мокко Флет Уайт Американо supplement

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        List<InlineKeyboardButton> delitel = new ArrayList<>();
        List<InlineKeyboardButton> rowInline3 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline4 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline5 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline6 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline7 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline8 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline9 = new ArrayList<>();


        var supplementCaramel = new InlineKeyboardButton();
        //🟢
        String settext1;
        if(supplementLight.isSupplementCaramel()) {
            settext1 = "Карамель\uD83D\uDFE2";
        }
        else
             settext1 = "Карамель";
        supplementCaramel.setText(settext1);
        supplementCaramel.setCallbackData("supplementCaramel");
        rowInline1.add(supplementCaramel);
        //rowsInline.add(rowInline1);

        var supplementVanil = new InlineKeyboardButton();
        String settext2;
        if(supplementLight.isSupplementVanila()) {
            settext2 = "Ваниль\uD83D\uDFE2";
        }
        else
            settext2 = "Ваниль";
        supplementVanil.setText(settext2);
        supplementVanil.setCallbackData("supplementVanila");
        rowInline1.add(supplementVanil);
        rowsInline.add(rowInline1);

        var supplementLesnoyOreh = new InlineKeyboardButton();
        String settext3 ;
        if(supplementLight.isSupplementLesnoyOreh()) {
            settext3 = "Лесной орех\uD83D\uDFE2";
        }
        else
            settext3 = "Лесной орех";
        supplementLesnoyOreh.setText(settext3);
        supplementLesnoyOreh.setCallbackData("supplementLesnoyOreh");
        rowInline3.add(supplementLesnoyOreh);
        //rowsInline.add(rowInline3);

        var supplementKokos = new InlineKeyboardButton();

        String settext4;
        if(supplementLight.isSupplementKokos()) {
            settext4 = "Кокос\uD83D\uDFE2";
        }
        else
            settext4 = "Кокос";
        supplementKokos.setText(settext4);
        supplementKokos.setCallbackData("supplementKokos");
        rowInline3.add(supplementKokos);
        rowsInline.add(rowInline3);

        var supplementBanan = new InlineKeyboardButton();
        String settext5;
        if(supplementLight.isSupplementBanan()) {
            settext5 = "Банан\uD83D\uDFE2";
        }
        else
            settext5 = "Банан";
        supplementBanan.setText(settext5);
        supplementBanan.setCallbackData("supplementBanan");
        rowInline6.add(supplementBanan);
       // rowsInline.add(rowInline5);

        var supplementShokCoocies = new InlineKeyboardButton();
        String settext6;
        if(supplementLight.isSupplementShokCoocies()) {
            settext6 = "Шоколадное печенье\uD83D\uDFE2";
        }
        else
            settext6 = "Шоколадное печенье";
        supplementShokCoocies.setText(settext6);
        supplementShokCoocies.setCallbackData("supplementShokCoocies");
        rowInline6.add(supplementShokCoocies);
        rowsInline.add(rowInline6);

        var supplementMindal = new InlineKeyboardButton();
        String settext7;
        if(supplementLight.isSupplementMindal()) {
            settext7 = "Миндаль\uD83D\uDFE2";
        }
        else
            settext7 = "Миндаль";
        supplementMindal.setText(settext7);
        supplementMindal.setCallbackData("supplementMindal");
        rowInline7.add(supplementMindal);
        //rowsInline.add(rowInline7);


        var supplementSaltedCaramel = new InlineKeyboardButton();
        String settext8 ;
        if(supplementLight.isSupplementSaltedCaramel()) {
            settext8 = "Сол. Карамель\uD83D\uDFE2";
        }
        else
            settext8 = "Сол. Карамель";
        supplementSaltedCaramel.setText(settext8);
        supplementSaltedCaramel.setCallbackData("supplementSaltedCaramel");
        rowInline7.add(supplementSaltedCaramel);
        rowsInline.add(rowInline7);


        var supplementejevika = new InlineKeyboardButton();
        String settext9;
        if(supplementLight.isSupplementejevika()) {
            settext9 = "Ежевика\uD83D\uDFE2";
        }
        else
            settext9 = "Ежевика";
        supplementejevika.setText(settext9);
        supplementejevika.setCallbackData("supplementejevika");
        rowInline8.add(supplementejevika);
       // rowsInline.add(rowInline8);


        var supplementamaretto = new InlineKeyboardButton();
        String settext10;
        if(supplementLight.isSupplementamaretto()) {
            settext10 = "Амаретто\uD83D\uDFE2";
        }
        else
            settext10 = "Амаретто";
        supplementamaretto.setText(settext10);
        supplementamaretto.setCallbackData("supplementamaretto");
        rowInline8.add(supplementamaretto);
        rowsInline.add(rowInline8);


        var delitel1 = new InlineKeyboardButton();
        String settext11 = "------------------";
        delitel1.setText(settext11);
        delitel1.setCallbackData("dsdfsdfsdf");
        delitel.add(delitel1);
        rowsInline.add(delitel);




        //нужно сделать отдельные мапы для альтер молока и сахара с корицей, в конце смержим все мапы в одну и отправим заказ
        var supplementCoconutMilk = new InlineKeyboardButton();
        String settext12;
        if(supplementLight.isSupplementcoconuMilk()) {
            settext12 = "Кокосовое молоко 90р\uD83D\uDFE2";
        }
        else
            settext12 = "Кокосовое молоко 90р";
        supplementCoconutMilk.setText(settext12);
        supplementCoconutMilk.setCallbackData("supplementCoconutMilk");
        rowInline4.add(supplementCoconutMilk);
        // rowsInline.add(rowInline4);


        var supplementHazelnutMilk = new InlineKeyboardButton();
        String settext13;
        if(supplementLight.isSupplementhazelnutMilk()) {
            settext13 = "Фундучное молоко 90р\uD83D\uDFE2";
        }
        else
            settext13 = "Фундучное молоко 90р";
        supplementHazelnutMilk.setText(settext13);
        supplementHazelnutMilk.setCallbackData("supplementHazelnutMilk");
        rowInline4.add(supplementHazelnutMilk);
        rowsInline.add(rowInline4);



        var supplementShugar = new InlineKeyboardButton();
        String settext14;
        if(supplementLight.isSupplementSugar()) {
            settext14 = "Сахар\uD83D\uDFE2";
        }
        else
            settext14 = "Сахар";
        supplementShugar.setText(settext14);
        supplementShugar.setCallbackData("supplementShugar");
        rowInline5.add(supplementShugar);
        //rowsInline.add(rowInline4);

        //sdsdsdstestgit



        var supplementKorica = new InlineKeyboardButton();
        String settext15;
        if(supplementLight.isSupplementCinamon()) {
            settext15 = "Корица\uD83D\uDFE2";
        }
        else
            settext15 = "корица";
        supplementKorica.setText(settext15);
        supplementKorica.setCallbackData("supplementKorica");
        rowInline5.add(supplementKorica);
        rowsInline.add(rowInline5);






        var dalee = new InlineKeyboardButton();
        String settext20 = "далее";
        dalee.setText(settext20);
        dalee.setCallbackData("dalee");
        rowInline9.add(dalee);
        rowsInline.add(rowInline9);



        keyboardMarkup.setKeyboard(rowsInline);
        message.setReplyMarkup(keyboardMarkup);




        try {
            execute(message);
        } catch (TelegramApiException e) {
            //log.error(ERROR_TEXT + e.getMessage());
        }
    }

    public void countCup(long chatid, long messageId, int price){


        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatid));
        message.setText("Сколько одинаковых напитков добавить в заказ?");
        message.setMessageId((int) messageId);

       // message.setChatId(String.valueOf(chatId));
       // message.setText(text);


        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline3 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline4 = new ArrayList<>();

        String text1 = "Один "+ menuRepo.findById(itemList.getMenuItemGuid()).get().getName() + " "+ order.getComment() + " = " + (price+itemList.getPriceWithDiscount())+"р";
        var ONE_CUP = new InlineKeyboardButton();
        ONE_CUP.setText(text1);
        ONE_CUP.setCallbackData("ONE_CUP");
        rowInline1.add(ONE_CUP);


        String text2 = "Два "+ menuRepo.findById(itemList.getMenuItemGuid()).  get().getName() + " "+ order.getComment() + " = " + (price+itemList.getPriceWithDiscount())*2+"р";
        var TWO_CUP = new InlineKeyboardButton();
        TWO_CUP.setText(text2);
        TWO_CUP.setCallbackData("TWO_CUP");
        rowInline2.add(TWO_CUP);

        String text3 = "Три "+ menuRepo.findById(itemList.getMenuItemGuid()).get().getName() + " "+ order.getComment() + " = " + (price+itemList.getPriceWithDiscount())*3+"р";

        var THREE_CUP = new InlineKeyboardButton();
        THREE_CUP.setText(text3);
        THREE_CUP.setCallbackData("THREE_CUP");
        rowInline3.add(THREE_CUP);

        String text4 = "Назад";
        var back_supplements = new InlineKeyboardButton();
        back_supplements.setText(text4);
        back_supplements.setCallbackData("back_supplements");
        rowInline4.add(back_supplements);

        rowsInline.add(rowInline1);
        rowsInline.add(rowInline2);
        rowsInline.add(rowInline3);
        rowsInline.add(rowInline4);

        keyboardMarkup.setKeyboard(rowsInline);

        message.setReplyMarkup(keyboardMarkup);
      //  System.out.println("done");

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("error occurred "+e.getMessage());


        }

    }


    public void aproveSend(long chatid, long messageId){


        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatid));
        message.setText("Отправить ваш заказ в кофейню?");
        message.setMessageId((int) messageId);

        // message.setChatId(String.valueOf(chatId));
        // message.setText(text);


        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();


        String text1 = "Да";
        var Yes_send = new InlineKeyboardButton();
        Yes_send.setText(text1);
        Yes_send.setCallbackData("Yes_send");
        rowInline1.add(Yes_send);

        String text2 = "Нет";
        var No_send = new InlineKeyboardButton();
        No_send.setText(text2);
        No_send.setCallbackData("No_send");
        rowInline1.add(No_send);




        rowsInline.add(rowInline1);


        keyboardMarkup.setKeyboard(rowsInline);

        message.setReplyMarkup(keyboardMarkup);
        //  System.out.println("done");

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("error occurred "+e.getMessage());


        }

    }


    public void editMessageText(long chatid, long messageId,String text){


        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatid));
        message.setText(text);
        message.setMessageId((int) messageId);


        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("error occurred "+e.getMessage());


        }

    }





    public void createOrder(long chatid){


        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatid));
        message.setText("Выберите категорию меню");

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline3 = new ArrayList<>();

        var CLASSIC_MENU = new InlineKeyboardButton();
        CLASSIC_MENU.setText("Кофе");
        CLASSIC_MENU.setCallbackData("classicMenu");
        rowInline1.add(CLASSIC_MENU);

        var AUTHOR_MENU = new InlineKeyboardButton();
        AUTHOR_MENU.setText("Авторское меню");
        AUTHOR_MENU.setCallbackData("authorMenu");
        rowInline2.add(AUTHOR_MENU);

        var NOTE_COFFEE = new InlineKeyboardButton();
        NOTE_COFFEE.setText("Не кофе");
        NOTE_COFFEE.setCallbackData("noteCoffee");
        rowInline3.add(NOTE_COFFEE);

        rowsInline.add(rowInline1);
        rowsInline.add(rowInline2);
        rowsInline.add(rowInline3);

        keyboardMarkup.setKeyboard(rowsInline);

        message.setReplyMarkup(keyboardMarkup);
        System.out.println("done");

        try {
            System.out.println("try");
            execute(message);
        } catch (TelegramApiException e) {
            log.error("error occurred "+e.getMessage());


        }

    }



    private void startCommandReceived(long chatId, String name) {
       // MenuRepo menuRepo = new("Флэт уайт", "cba009e4-655a-4951-9d57-86058a6dec3b", "cda491f9-470c-4e41-95dd-0ca0e4ba9eb1", 180, "7149fc5e-1973-4624-afb6-7454c600640a", 260, "7149fc5e-1973-4624-afb6-7454c600640a", 350);

//        menuRepo.save(new Menu("Флэт уайт","cba009e4-655a-4951-9d57-86058a6dec3b", "cda491f9-470c-4e41-95dd-0ca0e4ba9eb1", 180,"7149fc5e-1973-4624-afb6-7454c600640a",260,"7149fc5e-1973-4624-afb6-7454c600640a",350));
//        String s = menuRepo.findById(5).get().getName();


        String answer = "Hi, " + name + ", nice to meet you";
        //log.info("Replied to user " + name);

        sendMessage(chatId, answer);
    }

    void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);


//        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
//        List<KeyboardRow> keyboardRows = new ArrayList<>();
//        KeyboardRow keyboardRow = new KeyboardRow();
//
//        keyboardRow.add("/registration");
//        keyboardRow.add("/balance");
//
//        keyboardRows.add(keyboardRow);
//
//        replyKeyboardMarkup.setKeyboard(keyboardRows);
//
//        message.setReplyMarkup(replyKeyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            // log.error("error occurred "+e.getMessage());


        }

    }

    void sendMessageNonRegistred(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);


        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();

        keyboardRow.add("/registration");
       // keyboardRow.add("/balance");

        keyboardRows.add(keyboardRow);

        replyKeyboardMarkup.setKeyboard(keyboardRows);

        message.setReplyMarkup(replyKeyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            // log.error("error occurred "+e.getMessage());


        }

    }


    void sendMessageRegistred(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);


        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();

        keyboardRow.add("/balance");
        keyboardRow.add("/logout");
        keyboardRow.add("/newOrder");

        keyboardRows.add(keyboardRow);

        replyKeyboardMarkup.setKeyboard(keyboardRows);

        message.setReplyMarkup(replyKeyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            // log.error("error occurred "+e.getMessage());


        }

    }


    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }


}
