package Main.java;

import Main.java.cache.SecondLvl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Alekseenko Yuri 2018. For Weliy company.
 */
public class Main {
    private static int maxfirstLvl;
    private static int numberReq;
    private static int numberReqCallVoidRecache;

    public static void main(String[] args) {
        System.out.println("Weliy");
        System.out.println("Настройки кэширования");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String s;
            int i;
            System.out.println("Введите данные:");
            while (true) {
                System.out.println("Максимальное количество записей в кэше оперативной памяти ");
                s = reader.readLine();
                if (checkWithRegExp(s)) {
                    i = Integer.parseInt(s);
                    setMaxfirstLvl(i);
                    break;
                }
            }
            while (true) {
                System.out.println("Количество запросов к кэшу после последнего рекэширования ");
                s = reader.readLine();
                if (checkWithRegExp(s)) {
                    i = Integer.parseInt(s);
                    setNumberReq(i);
                    break;
                }
            }
            while (true) {
                System.out.println("Количество запросов, необходимое для рекэширования ");
                s = reader.readLine();
                if (checkWithRegExp(s)) {
                    i = Integer.parseInt(s);
                    setNumberReqCallVoidRecache(i);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Main().run();
    }

    public static int getMaxfirstLvl() {
        return maxfirstLvl;
    }

    public static void setMaxfirstLvl(int maxfirstLvl) {
        maxfirstLvl = maxfirstLvl;
    }

    public static int getNumberReq() {
        return numberReq;
    }

    public static void setNumberReq(int numberReq) {
        numberReq = numberReq;
    }

    public static int getNumberReqCallVoidRecache() {
        return numberReqCallVoidRecache;
    }

    public static void setNumberReqCallVoidRecache(int numberReqCallVoidRecache) {
        numberReqCallVoidRecache = numberReqCallVoidRecache;
    }

    public static void run() {
        SecondLvl secondLvl =
                new SecondLvl(getMaxfirstLvl(), getNumberReqCallVoidRecache(), getNumberReq());
    }

    public static boolean checkWithRegExp(String userNameString) {
        Pattern p = Pattern.compile("[0-9]+");
        Matcher m = p.matcher(userNameString);
        return m.matches();
    }
}
