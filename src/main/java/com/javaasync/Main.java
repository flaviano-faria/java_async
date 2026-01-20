package com.javaasync;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    public static Future<String> calculateAsync() throws InterruptedException {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        Executors.newCachedThreadPool().submit(() -> {
            Thread.sleep(5000);
            completableFuture.complete("Hello");
            return null;
        });

        return completableFuture;
    }
    public static void main(String[] args) throws InterruptedException, ExecutionException {

        Future<String> completableFuture = calculateAsync();
        String response = completableFuture.get();
        System.out.println(response);
    }
}