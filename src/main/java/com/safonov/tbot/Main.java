package com.safonov.tbot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
//        PomodoroBot bot = new PomodoroBot();
        EchoBot bot = new EchoBot();
        telegramBotsApi.registerBot(bot);
//        new Thread(() -> {
//            try {
//                bot.checkTimer();
//            } catch (InterruptedException e) {
//                System.out.println("Уппс");
//            }
//        }).run();
    }
}
