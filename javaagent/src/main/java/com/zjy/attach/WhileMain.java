package com.zjy.attach;


public class WhileMain {
    public static void main(String[] args) throws InterruptedException {
        System.out.println(new Dog().say());
        int count = 0;
        while (true) {
            // 等待0.5秒
            Thread.sleep(500);
            count++;
            String say = new Dog().say();
            // 输出内容和次数
            System.out.println(say + count);
            // 内容不对或者次数达到1000次以上输出
            if (!"dog".equals(say) || count >= 1000) {
                System.out.println("有人偷了我的狗！");
                //break;
            }
        }
    }
}
