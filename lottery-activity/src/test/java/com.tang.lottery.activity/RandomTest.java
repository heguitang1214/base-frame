package com.tang.lottery.activity;

import java.util.Random;

public class RandomTest {
    public static void main(String[] args) {


        Random r1 = new Random();
        Random r2 = new Random();
        // 有参构造使用的是参数作为种子数
        Random r3 = new Random(100);
        Random r4 = new Random(100);
        // 产生随机数调用nextXXX()方法
        r1.nextGaussian();
        System.out.println(r1.nextInt(10));
        System.out.println(r1.nextInt(10));
        System.out.println(r2.nextInt(10));
        System.out.println(r2.nextInt(10));
        System.out.println("-----------------");
        System.out.println(r3.nextInt(10));
        System.out.println(r3.nextInt(10));
        System.out.println(r4.nextInt(10));
        System.out.println(r4.nextInt(10));
    }
}
