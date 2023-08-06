package org.tms.common.auth.configuration.global.core.throw_exception_async;

import java.util.concurrent.*;

public class ThrowingExceptionInExecutorServiceMain {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        System.out.println("start");


        Runnable r1 = () -> {
            for (int i = 0; i < 15; i++) {
                if (i == 5) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        System.out.println("Thread = " + Thread.currentThread().getName() + " -> was interrupted ");
                        throw new RuntimeException("My Ex. with Runnable!");
                    }
                }
                System.out.println(Thread.currentThread().getName() + " -> " + i);
            }
        };
        Future<?> res1 = executorService.submit(r1);


        Callable<String> c2 = () -> {
            for (int i = 0; i < 300; i++) {
                if (i == 12) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        System.out.println("Thread = " + Thread.currentThread().getName() + " -> was interrupted ");
                        throw new RuntimeException("My Ex. with Callable!");
                    }
                }
                System.out.println(Thread.currentThread().getName() + " -> " + i);
            }
            return "OK";
        };
        Future<String> res2 = executorService.submit(c2);

        boolean complete = new CompletableFuture<String>().complete("Hello!");

        System.out.println("Try to shutdown!!!");
        executorService.shutdownNow();


        try {
            Object answer1 = res1.get();
        } catch (Exception ex) {
            System.out.println("Ex. from chaild Thread was throwed: " + ex);
        }
        try {
            String answer2 = res2.get();
        } catch (Exception ex) {
            System.out.println("Ex. from chaild Thread was throwed: " + ex);
        }


        System.out.println("finish");
    }
}