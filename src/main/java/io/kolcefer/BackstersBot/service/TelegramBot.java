package io.kolcefer.BackstersBot.service;


import io.kolcefer.BackstersBot.apiYtimes.CardInfoGetBalance;
import io.kolcefer.BackstersBot.apiYtimes.Cardinfo;
import io.kolcefer.BackstersBot.apiYtimes.ClientData;
import io.kolcefer.BackstersBot.apiYtimes.OrderList;
import io.kolcefer.BackstersBot.config.BotConfig;

import io.kolcefer.BackstersBot.entity.Users;
import io.kolcefer.BackstersBot.repository.UserRepo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringVersion;
import org.springframework.expression.spel.ast.NullLiteral;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.swing.*;
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

    Cardinfo card = new Cardinfo();
    OrderList list1 = new OrderList();
    @Autowired
    ClientData clientData;

    @Autowired
    CardInfoGetBalance cardInfoGetBalance;

    @Autowired
    private UserRepo userRepo;


    public  Map<Long, Integer> userStates = new HashMap<>();

    @Override

    public void onUpdateReceived(Update update) {


        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

//            System.out.println(chatId);
//
//            Users users1  = new Users(chatId, "ssss","ss", 222221501);
//            userRepo.save(users1);


            switch (messageText) {
                case "/start":
                    //  list1.getOrderInfo();

                    String msg = "Добро пожаловть в бот Backster`s Coffee";
                    //startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    if (userRepo.findById(chatId).isEmpty()){

                        sendMessageNonRegistred(chatId, msg);
                    } else {
                        sendMessageRegistred(chatId,msg);

                    }

                    break;

                case "/balance":
//                    // Устанавливаем состояние ожидания номера телефона для пользователя
//                    userStates.put(chatId, 1);
//                    sendMessage(chatId, "Пожалуйста, введите ваш номер телефона:");
//
//
//                    // Завершаем выполнение onUpdateReceived, чтобы не выполнялась
//                    // логика обработки сообщений дальше до ввода номера
                   //long x = chatId;

                   // cardInfoGetBalance.getBalance(userRepo.findById(chatId).get().getPhoneNumber());

                   // card.getInfo(userRepo.findById(chatId).get().getPhoneNumber());
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

    }

    public void registrationUser(long chatId, String messageText, Map<Long, Integer> map) {


        if (!map.containsKey(chatId)) {
            return;
        }

        int state = map.get(chatId);
        switch (state) {
            case 1:
                // Обрабатываем введенный пользователем номер телефона
                String phoneNumber = messageText;


                // Здесь можно добавить логику обработки номера телефона
                // Например, сохранить его в базе данных или использовать для каких-то операций
                // После обработки номера можно удалить состояние ожидания для этого пользователя
                map.remove(chatId);
                sendMessage(chatId, "Спасибо! Номер телефона сохранен.");


                //sendMessage(chatId,phoneNumber);
                card.getInfo(phoneNumber);
                String msg = String.valueOf(card.getPoints());
                sendMessage(chatId, msg);

                String s = card.getName();


                cardInfoGetBalance.getBalance(phoneNumber).getName();


//                    Users users  = new Users(chatId, s,"ss", phoneNumber);
                userRepo.save(new Users(chatId, cardInfoGetBalance.getBalance(phoneNumber).getName(),
                        cardInfoGetBalance.getBalance(phoneNumber).getSurname(),
                        phoneNumber));


                break;

            default:
                // Если состояние не определено, просто отправляем сообщение об ошибке
                sendMessage(chatId, "Произошла ошибка.");
                break;
        }


    }


    private void startCommandReceived(long chatId, String name) {


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
