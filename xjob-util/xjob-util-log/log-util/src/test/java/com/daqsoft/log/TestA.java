package com.daqsoft.log;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class TestA {
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        IntStream.range(0, 5).parallel().forEach(e -> System.out.println(atomicInteger.getAndIncrement()));

//        for (int i = 0; i < 5; i++) {
//            new Thread(() -> System.out.println(atomicInteger.getAndIncrement())).start();
//        }
    }
}
