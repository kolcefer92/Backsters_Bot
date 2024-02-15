package io.kolcefer.BackstersBot.service;


import io.kolcefer.BackstersBot.apiYtimes.Cardinfo;
import io.kolcefer.BackstersBot.config.BotConfig;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.SpringVersion;
import org.springframework.expression.spel.ast.NullLiteral;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@Data

public class TelegramBot  extends TelegramLongPollingBot {

    final BotConfig config;

    public TelegramBot(BotConfig config){
        this.config = config;
    }
    Cardinfo card = new Cardinfo();
    private Map<Long, Integer> userStates = new HashMap<>();
    @Override

    public void onUpdateReceived(Update update) {



        if(update.hasMessage()  && update.getMessage().hasText()){
            String messageText =  update.getMessage().getText();
            long chatId = update.getMessage().getChatId();




            switch ( messageText){
                case "/start":



                        startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                        break;

                case "/balance":
                    // Устанавливаем состояние ожидания номера телефона для пользователя
                    userStates.put(chatId, 1);
                    sendMessage(chatId, "Пожалуйста, введите ваш номер телефона:");
                    // Завершаем выполнение onUpdateReceived, чтобы не выполнялась
                    // логика обработки сообщений дальше до ввода номера
                    return;
                //break;


                default:
                   // String nameuser = card.user(11296);
                    //card.getInfo();
                   // String answ = card.getPoints() + " бонусов\n" + " "+card.getName();

                   // sendMessage(chatId, answ);
                    //sendMessage(chatId, card.getName());

                    System.out.println("Sorry, command was not recognized");

            }

            // Если состояние диалога с пользователем не определено, просто игнорируем сообщение
            if (!userStates.containsKey(chatId)) {
                return;
            }

            int state = userStates.get(chatId);
            switch (state) {
                case 1:
                    // Обрабатываем введенный пользователем номер телефона
                    String phoneNumber = messageText;
                    // Здесь можно добавить логику обработки номера телефона
                    // Например, сохранить его в базе данных или использовать для каких-то операций
                    // После обработки номера можно удалить состояние ожидания для этого пользователя
                    userStates.remove(chatId);
                    sendMessage(chatId, "Спасибо! Номер телефона сохранен.");
                    //sendMessage(chatId,phoneNumber);
                    card.getInfo(phoneNumber);
                    sendMessage(chatId, card.getPoints());


                    break;

                default:
                    // Если состояние не определено, просто отправляем сообщение об ошибке
                    sendMessage(chatId, "Произошла ошибка.");
                    break;
            }



        }

    }


    private void startCommandReceived(long chatId, String name){


        String answer = "Hi, " + name+ ", nice to meet you";
        //log.info("Replied to user " + name);

        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String textToSend){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try{
            execute(message);
        }
        catch (TelegramApiException e){
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
