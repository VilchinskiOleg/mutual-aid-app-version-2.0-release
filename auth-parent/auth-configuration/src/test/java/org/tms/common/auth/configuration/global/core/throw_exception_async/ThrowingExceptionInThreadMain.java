package org.tms.common.auth.configuration.global.core.throw_exception_async;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThrowingExceptionInThreadMain {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        System.out.println("start");

        try {
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
            var thread0 = new Thread(r1);
            thread0.start();
            thread0.interrupt();


            Callable<String> c2 = () -> {
                for (int i = 0; i < 300; i++) {
                    if (i == 49) {
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


            System.out.println("Try to shutdown!!!");
            executorService.shutdownNow();

            try {
                String answer2 = res2.get();
            } catch (Exception ex) {
                System.out.println("Ex. from chaild Thread was throwed: " + ex);
            }

            System.out.println("finish");

        } catch (Exception ex) {
            System.out.println("Ex. from Thread0 was processed by try/catch block in Main thread");
        }
    }
}