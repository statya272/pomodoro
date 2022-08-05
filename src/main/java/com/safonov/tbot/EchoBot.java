package com.safonov.tbot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class EchoBot extends TelegramLongPollingBot {

    private final String TOKEN = "";

    @Override
    public String getBotUsername() {
        return "Попугай bot";
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }

    /**
     * Обработка входящих сообщений
     */
    @Override
    public void onUpdateReceived(Update update) {
        int userCount = 0;
        if (update.hasMessage() && update.getMessage().hasText()) {
            if (update.getMessage().getText().equals("/start")) {
                userCount+=1;
                System.out.println("Новый пользователь " + userCount);
                // приветсвие
                String s = new String("Я попугай бот. Напиши мне что-нибудь.".getBytes(), StandardCharsets.UTF_8);
                sendMsg(s, update.getMessage().getChatId().toString());
            } else {
                System.out.println("Обработка сообщений");
                sendMsg(update.getMessage().getText().toUpperCase(Locale.ROOT),
                        update.getMessage().getChatId().toString());
            }
        }
    }

    private void sendMsg(String text, String chatId) {
        SendMessage msg = new SendMessage();
        // пользователь чата
        msg.setChatId(chatId);
        msg.setText(text);

        try {
            execute(msg);
        } catch (TelegramApiException e) {
            System.out.println("Уппс");
        }
    }
}
