package io.kolcefer.BackstersBot.service;

import io.kolcefer.BackstersBot.apiYtimes.Cardinfo;
import io.kolcefer.BackstersBot.entity.Users;
import io.kolcefer.BackstersBot.repository.UserRepo;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;


@Component
public class Registration {
    @Autowired
    UserRepo userRepo;
    @Autowired
    Cardinfo card;
    @Autowired
    TelegramBot telegramBot;

    public void registrationUser(long chatId,String messageText, Map<Long,Integer> map){


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
                telegramBot.sendMessage(chatId, "Спасибо! Номер телефона сохранен.");


                //sendMessage(chatId,phoneNumber);
                card.getInfo(phoneNumber);
                String msg = String.valueOf( card.getPoints());
                telegramBot.sendMessage(chatId, msg);

                String s = card.getName();

//                    Users users  = new Users(chatId, s,"ss", phoneNumber);
                userRepo.save(new Users(chatId, s,"ss", phoneNumber));



                break;

            default:
                // Если состояние не определено, просто отправляем сообщение об ошибке
               telegramBot.sendMessage(chatId, "Произошла ошибка.");
                break;
        }


    }
}
