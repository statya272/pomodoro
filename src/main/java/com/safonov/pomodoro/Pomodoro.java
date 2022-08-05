package com.safonov.pomodoro;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Pomodoro {

    static boolean isTest = false;

    /*
    -w 1 -b 1 -count 1
    split()
    [-w, 1, -b, 1, -count, 1]
     */
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Привет! Напиши пожалуйста команду");
        // считываем пользовательский ввод
        String[] cmd = new Scanner(System.in).nextLine().split(" ");

        // время работы
        int workMin = 25;
        // время отдыха
        int breakMin = 5;
        // кол-во подходов
        int count = 1;

        // длина рисунка progress bar
        int sizePrint = 30;

        boolean isCallHelp = false;

        for (int i=0; i < cmd.length; i++) {
            switch (cmd[i]) {
                case "-help" -> {
                    printHelpMsg();
                    isCallHelp = true;
                }
                case "-w" -> workMin = Integer.parseInt(cmd[++i]);
                case "-b" -> breakMin = Integer.parseInt(cmd[++i]);
                case "-count" -> count = Integer.parseInt(cmd[++i]);
                case "-t" -> isTest = true;
            }
        }

        if (!isCallHelp) {
            System.out.printf("Работаем %d min, " +
                    "отдывахем %d min, кол-во подходов %d\n", workMin, breakMin, count);
            long startTime = System.currentTimeMillis();
            for (int i = 1; i <= count; i++) {
                timer(workMin, breakMin, sizePrint);
            }
            long endTime = System.currentTimeMillis();
            System.out.println("Pomodoro таймер истек: " + (endTime - startTime)/(1000 * 60) + " min");
        }
    }

    private static void timer(int workTime, int breakTime, int sizeProgressBar) throws InterruptedException {
        printProgress("Время вкалывать:: ", workTime, sizeProgressBar);

        printProgress("Время отдыхать:: ", breakTime, sizeProgressBar);
    }

    private static void printProgress(String process, int time, int size) throws InterruptedException {
        int length;
        int rep;
        length = 60* time / size;
        rep = 60* time /length;
        int stretchb = size /(3* time);
        for(int i=1; i <= rep; i++){
            double x = i;
            x = 1.0/3.0 *x;
            x *= 10;
            x = Math.round(x);
            x /= 10;
            double w = time *stretchb;
            double percent = (x/w) *1000;
            x /=stretchb;
            x *= 10;
            x = Math.round(x);
            x /= 10;
            percent = Math.round(percent);
            percent /= 10;
            System.out.print(process + percent+"% " + (" ").repeat(5 - (String.valueOf(percent).length())) +"[" + ("#").repeat(i) + ("-").repeat(rep - i)+"]    ( " + x +"min / " + time +"min )"+  "\r");
            if(!isTest){
                TimeUnit.SECONDS.sleep(length);
            }
        }
        System.out.println();
    }

    private static void printHelpMsg() {
        System.out.println(
                "\n\nPomodoro - сделай свое время более эффективным\n");
        System.out.println(
                "	-w <time>: время работы, сколько хочешь работать.\n");
        System.out.println(
                "	-b <time>: время отдыха, сколько хочешь отдыхать.\n");
        System.out.println(
                "	-count <count>: количество итераций.\n");
        System.out.println(
                "	-help: меню помощи.\n");
    }
}