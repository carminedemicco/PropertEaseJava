package main.propertease.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ThreadPool {
    public ThreadPool() {
        this(Runtime.getRuntime().availableProcessors());
    }

    public ThreadPool(int size) {
        executor = Executors.newFixedThreadPool(size);
    }

    public Future<?> add(Runnable task) {
        return executor.submit(task);
    }

    public void stop() {
        try {
            executor.shutdown();
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    System.err.println("thread pool did not terminate");
                }
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }

    private final ExecutorService executor;
}
