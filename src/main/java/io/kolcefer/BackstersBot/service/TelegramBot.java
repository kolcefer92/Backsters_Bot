package io.kolcefer.BackstersBot.service;



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
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
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






    public  Map<Long, Integer> userStates = new HashMap<>();

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
                    String textValuesKapuchino = "Выберите объем";


                    valuesDrink(textValuesKapuchino,chatId,messageId);

                    break;

                case ("FletUayt"):
                    String textValuesFletUayt = "Выберите объем";


                    valuesDrink(textValuesFletUayt,chatId,messageId);

                    break;
                case ("Raf"):
                    String textValuesRaf = "Выберите объем";


                    valuesDrink(textValuesRaf,chatId,messageId);

                    break;
                case ("Mokko"):
                    String textValuesMokko = "Выберите объем";


                    valuesDrink(textValuesMokko,chatId,messageId);

                    break;
                case ("Latte"):
                    String textValuesLatte = "Выберите объем";


                    valuesDrink(textValuesLatte,chatId,messageId);

                    break;
                case ("Americano"):
                    String textValuesAmericano = "Выберите объем";


                    valuesDrink(textValuesAmericano,chatId,messageId);

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
        String settext1 = menuRepo.findById(1).get().getName();
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

        // Капучино Латте Раф Мокко Флет Уайт Американо

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
        keyboardRow.add("/balance");

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
