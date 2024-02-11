package main.propertease.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * A simple thread pool that can be used to execute tasks.
 */
public class ThreadPool {
    /**
     * Creates a new thread pool with a size equal to the number of available processors.
     */
    public ThreadPool() {
        this(Runtime.getRuntime().availableProcessors());
    }

    /**
     * Creates a new thread pool with a specified number of worker threads.
     *
     * @param size The size of the thread pool.
     */
    public ThreadPool(int size) {
        executor = Executors.newFixedThreadPool(size);
    }

    /**
     * Adds a task to the thread pool.
     *
     * @param task The task to add.
     * @return A future representing the result of the task.
     */
    public Future<?> add(Runnable task) {
        return executor.submit(task);
    }

    /**
     * Stops the thread pool, waiting for all tasks to complete before returning.
     */
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
