package com.javaasync;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    public static Future<Integer> calcRate(){
        CompletableFuture<Integer> completableFuture = new CompletableFuture<>();

        Executors.newCachedThreadPool().submit(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            completableFuture.complete(10);
        });

        return completableFuture;
    }

    public static CompletableFuture<Integer> responseSupplyAsync(){
        int value = 10;
        return CompletableFuture.supplyAsync(() -> value)
                .thenApplyAsync(result -> result * 2)
                .thenApplyAsync(result -> result + 5);
    }

    public static Future<String> responseAsync() throws InterruptedException {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        Executors.newCachedThreadPool().submit(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            completableFuture.complete("Hello");
        });

        return completableFuture;
    }


    public static Future<String> messageAsync() throws InterruptedException {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        Executors.newCachedThreadPool().submit(() -> {
            try {
                Thread.sleep(7000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            completableFuture.complete("message");
        });

        return completableFuture;
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        Future<String> responseAsyncFuture = responseAsync();
        Future<String> messageAsyncFuture = messageAsync();
        CompletableFuture<Integer> messageSupplyAsyncFuture = responseSupplyAsync();
        CompletableFuture<Integer> calcRateFuture = (CompletableFuture<Integer>) calcRate();

        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(
                (CompletableFuture<?>) responseAsyncFuture, 
                (CompletableFuture<?>) messageAsyncFuture,
                messageSupplyAsyncFuture,
                calcRateFuture
                );

        CompletableFuture<List<Object>> allResults = combinedFuture.thenApply(v ->
                Arrays.asList(
                        ((CompletableFuture<String>) responseAsyncFuture).join(),
                        ((CompletableFuture<String>) messageAsyncFuture).join(),
                        messageSupplyAsyncFuture.join(),
                        (calcRateFuture.join())));

        try {
            List<Object> results = allResults.get();
            System.out.println(combinedFuture);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}