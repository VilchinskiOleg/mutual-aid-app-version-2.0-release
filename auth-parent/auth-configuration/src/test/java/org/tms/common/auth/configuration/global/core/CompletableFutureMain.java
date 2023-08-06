package org.tms.common.auth.configuration.global.core;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class CompletableFutureMain {

    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(3);

    public static void main(String[] args) {
        List<CompletableFuture<String>> futures = Stream.generate(() -> (int) (Math.random()*10))
                .limit(15)
                .map(CompletableFutureMain::createFuture)
                .collect(toList());

        CompletableFuture[] futuresArray = futures.toArray(new CompletableFuture[futures.size()]);
        List<String> result = CompletableFuture
                .allOf(futuresArray)
                .thenApply(allOfResult -> futures.stream()
                        .map(CompletableFuture::join)
                        .collect(toList())
                )
                .join();

        System.out.println(result);
    }

    private static CompletableFuture<String> createFuture(Integer number) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("Execute task by Thread = " + Thread.currentThread().getName());
            return "Task #" + number + " was completed!";
        }, EXECUTOR);
    }
}