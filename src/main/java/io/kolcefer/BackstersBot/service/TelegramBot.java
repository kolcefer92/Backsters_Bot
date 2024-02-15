package io.kolcefer.BackstersBot.service;


import io.kolcefer.BackstersBot.apiYtimes.Cardinfo;
import io.kolcefer.BackstersBot.config.BotConfig;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.SpringVersion;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.swing.*;
@Slf4j
@Component
public class TelegramBot  extends TelegramLongPollingBot {

    final BotConfig config;

    public TelegramBot(BotConfig config){
        this.config = config;
    }
    Cardinfo card = new Cardinfo();

    @Override

    public void onUpdateReceived(Update update) {
        if(update.hasMessage()  && update.getMessage().hasText()){
            String messageText =  update.getMessage().getText();
            long chatId = update.getMessage().getChatId();


            switch ( messageText){
                case "/start":



                        startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                        break;
                default:
                   // String nameuser = card.user(11296);
                    card.getInfo();
                    String answ = card.getPoints() + " бонусов\n" + " "+card.getName();

                    sendMessage(chatId, answ);
                    //sendMessage(chatId, card.getName());

//"Sorry, command was not recognized"

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
