package com.safonov.tbot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ConcurrentHashMap;

class PomodoroBot extends TelegramLongPollingBot {

    private final ConcurrentHashMap<Timer, Long> userTimers = new ConcurrentHashMap();
    private final String TOKEN = "";

    enum TimerType {
        WORK,
        BREAK
    }

    static record Timer(Instant time, TimerType timerType) {
    }

    ;

    @Override
    public String getBotUsername() {
        return "Pomodoro bot";
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            if (update.getMessage().getText().equals("/start")) {
                sendMsg("""
                        Pomodoro - сделай свое время более эффективным.
                        Задай мне время работы и отдыха через пробел. Например, '1 1'.
                        PS Я работаю пока в минутах
                        """, chatId.toString());
            } else {
                var args = update.getMessage().getText().split(" ");
                if (args.length >= 1) {
                    var workTime = Instant.now().plus(Long.parseLong(args[0]), ChronoUnit.MINUTES);
                    userTimers.put(new Timer(workTime, TimerType.WORK), chatId);
                    sendMsg("Давай работай!", chatId.toString());
                    if (args.length >= 2) {
                        var breakTime = workTime.plus(Long.parseLong(args[1]), ChronoUnit.MINUTES);
                        userTimers.put(new Timer(breakTime, TimerType.BREAK), chatId);
                    }
                }
            }
        }
    }

    public void checkTimer() throws InterruptedException {
        while(true) {
            System.out.println("Количество таймеров пользователей " + userTimers.size());
            userTimers.forEach((timer, userId) -> {
                System.out.printf("Проверка userId = %d, server_time = %s, user_timer = %s\n",
                        userId, Instant.now().toString(), timer.time.toString());
                if (Instant.now().isAfter(timer.time)) {
                    userTimers.remove(timer);
                    switch (timer.timerType) {
                        case WORK -> sendMsg("Пора отдыхать", userId.toString());
                        case BREAK -> sendMsg("Таймер завершил свою работу", userId.toString());
                    }
                }
            });
            Thread.sleep(1000);
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


//package com.safonov.tbot;
//
//import org.telegram.telegrambots.bots.TelegramLongPollingBot;
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import org.telegram.telegrambots.meta.api.objects.Update;
//import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
//
//import java.time.Instant;
//import java.time.temporal.ChronoUnit;
//import java.util.concurrent.ConcurrentHashMap;
//
//public class PomodoroBot extends TelegramLongPollingBot {
//
//    public PomodoroBot(){
//        super();
//    }
//
//    private final ConcurrentHashMap<UserTimer, Long> userTimerRepository = new ConcurrentHashMap<>();
//
//    enum TimerType {
//        WORK,
//        BREAK
//    }
//
//    record UserTimer(Instant userTimer, TimerType timerType) {
//    }
//
//    @Override
//    public String getBotUsername() {
//        return "PomodoroStatya272Bot";
//    }
//
//    @Override
//    public String getBotToken() {
//        return "5532877934:AAEnP9mI-tjwuk_n0x45DEYXVsn5LYxFiBQ";
//    }
//
//    @Override
//    public void onUpdateReceived(Update update) {
//        if (!update.hasMessage() || !update.getMessage().hasText()) {
//            return;
//        }
//
//        String[] textParts = update.getMessage().getText().split(" ");
//        Instant workTime = Instant.now().plus(Long.parseLong(textParts[0]), ChronoUnit.MINUTES);
//        Instant breakTime = workTime.plus(Long.parseLong(textParts[1]), ChronoUnit.MINUTES);
//        userTimerRepository.put(new UserTimer(workTime, TimerType.WORK), update.getMessage().getChatId());
//        userTimerRepository.put(new UserTimer(breakTime, TimerType.BREAK), update.getMessage().getChatId());
//    }
//
//    public void checkTimer() throws InterruptedException {
//        while (true) {
//            System.out.println("Количество таймеров пользователей " + userTimerRepository.size());
//            userTimerRepository.forEach((timer, userID) -> {
//                if (Instant.now().isAfter(timer.userTimer)) {
//                    switch (timer.timerType) {
//                        case WORK -> sendMsg(userID, "Пора отдыхать");
//                        case BREAK -> sendMsg(userID, "Таймер завершил работу");
//                    }
//                }
//                userTimerRepository.remove(timer);
//            });
//            Thread.sleep(1000);
//        }
//    }
//
//    public void sendMsg(Long chatId, String text) {
//
//        SendMessage msg = new SendMessage();
//        msg.setChatId(chatId);
//        msg.setText(text);
//        try {
//            execute(msg);
//        } catch (TelegramApiException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
