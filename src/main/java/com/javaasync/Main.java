package com.javaasync;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    public static Future<String> responseAsync() throws InterruptedException {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        Executors.newCachedThreadPool().submit(() -> {
            Thread.sleep(5000);
            completableFuture.complete("Hello");
            return null;
        });

        return completableFuture;
    }


    public static Future<String> messageAsync() throws InterruptedException {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        Executors.newCachedThreadPool().submit(() -> {
            Thread.sleep(7000);
            completableFuture.complete("message");
            return null;
        });

        return completableFuture;
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        Future<String> responseAsyncFuture = responseAsync();
        Future<String> messageAsyncFuture = messageAsync();

        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(
                (CompletableFuture<?>) responseAsyncFuture, (CompletableFuture<?>) messageAsyncFuture);

        CompletableFuture<List<String>> allResults = combinedFuture.thenApply(v ->
                Arrays.asList(((CompletableFuture<String>) responseAsyncFuture).join(), ((CompletableFuture<String>) messageAsyncFuture).join())
        );

        try {
            List<String> results = allResults.get();
            System.out.println(results);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}