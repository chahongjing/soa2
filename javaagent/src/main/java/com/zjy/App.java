package com.zjy;

import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        int i = 0;
        while (i < 10) {
            testPrint();
            i++;
        }
    }

    private static void testPrint() {
        System.out.println("this is my " + (System.currentTimeMillis() / 1000) + " miss you");
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
        }
    }
}
