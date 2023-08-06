package org.tms.common.auth.configuration.global.core.interrupt_thread;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

/**
 * About volatile:
 *
 * https://www.baeldung.com/java-volatile
 * https://www.baeldung.com/java-stack-heap
 */
public class InterruptByExecutorServiceMain {

    public static void useShutdownNow() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        System.out.println("start");


        Runnable r1 = () -> {for (int i = 0; i < 15; i++) {
            if (i == 9) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    System.out.println("Thread = " + Thread.currentThread().getName() + " -> was interrupted ");
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println(Thread.currentThread().getName() + " -> " + i);
        }};
        executorService.submit(r1);


        Runnable r2 = () -> {for (int i = 0; i < 20; i++) {
            if (i == 5) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    System.out.println("Thread = " + Thread.currentThread().getName() + " -> was interrupted ");
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println(Thread.currentThread().getName() + " -> " + i);
        }};
        executorService.submit(r2);


        Runnable r3 = () -> {for (int i = 0; i < 9; i++) {
            if (i == 3) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    System.out.println("Thread = " + Thread.currentThread().getName() + " -> was interrupted ");
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println(Thread.currentThread().getName() + " -> " + i);
        }};
        executorService.submit(r3);


        System.out.println("Try to shutdown!!!");
        List<Runnable> runnableList = executorService.shutdownNow();
        System.out.println("Amount of Runnable objects weren't executed: " + runnableList.size());


        try {
            executorService.submit(r3);
        } catch (RejectedExecutionException ex) {
            System.out.println("Cannot execute any tasks after 'shutdown' !!!");
        }


        System.out.println("finish");
    }


    public static void useShutdown() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        System.out.println("start");


        Runnable r1 = () -> {for (int i = 0; i < 15; i++) {
            if (i == 9) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    System.out.println("Thread = " + Thread.currentThread().getName() + " -> was interrupted ");
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println(Thread.currentThread().getName() + " -> " + i);
        }};
        executorService.submit(r1);


        Runnable r2 = () -> {for (int i = 0; i < 20; i++) {
            if (i == 5) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    System.out.println("Thread = " + Thread.currentThread().getName() + " -> was interrupted ");
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println(Thread.currentThread().getName() + " -> " + i);
        }};
        executorService.submit(r2);


        Runnable r3 = () -> {for (int i = 0; i < 9; i++) {
            if (i == 3) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    System.out.println("Thread = " + Thread.currentThread().getName() + " -> was interrupted ");
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println(Thread.currentThread().getName() + " -> " + i);
        }};
        executorService.submit(r3);


        System.out.println("Try to shutdown!!!");
        executorService.shutdown();


        try {
            executorService.submit(r3);
        } catch (RejectedExecutionException ex) {
            System.out.println("Cannot execute any tasks after 'shutdown' !!!");
        }


        System.out.println("finish");
    }
}