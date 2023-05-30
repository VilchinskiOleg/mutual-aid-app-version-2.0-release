package org.tms.common.auth.configuration.global.core.interrupt_thread;

import org.junit.jupiter.api.Test;

public class InterruptThreadDirectlyTest {

    @Test
    void thread_interrupt_test() {
        System.out.println("start");
        var t1 = new Thread(() -> {
            for (int n = 0; n < 10; n++) {
                if (n == 5) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        System.out.println("Current thread is interrupted: " + Thread.currentThread().isInterrupted());
                        Thread.currentThread().interrupt();
                        System.out.println("Current thread is interrupted: " + Thread.currentThread().isInterrupted());
                        System.out.println("Current thread is interrupted: " + Thread.currentThread().isInterrupted());
                    }
                }
                System.out.println("Current number is: " + n);
            }
        });
        t1.start();
        t1.interrupt();
        System.out.println("finish");
    }
}