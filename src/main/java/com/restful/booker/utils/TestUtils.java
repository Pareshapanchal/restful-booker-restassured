package com.restful.booker.utils;

import java.util.Random;

public class TestUtils {
    public static String getRandomValue(){
        Random random = new Random();
        int randomInt = random.nextInt(100000);
        return Integer.toString(randomInt);
    }
    public static String randomStr(int length) {
        String result           = " ";
        String characters       = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        int charactersLength = characters.length();
        for ( int i = 0; i < length; i++ ) {
            result += characters.charAt((int) Math.floor(Math.random() * charactersLength));
        }
        return result;
    }
}
