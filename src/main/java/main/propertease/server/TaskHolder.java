package main.propertease.server;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class TaskHolder {
    public TaskHolder() {
        isRunning = new AtomicBoolean(true);
        tasks = new LinkedBlockingQueue<>();
        pollThread = new Thread(() -> {
            while (isRunning.get()) {
                try {
                    tasks.take().get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            while (!tasks.isEmpty()) {
                try {
                    tasks.take().get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        pollThread.start();
    }

    public void stop() {
        isRunning.set(false);
        try {
            pollThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void add(Future<?> task) {
        tasks.add(task);
    }

    private final Thread pollThread;
    private final AtomicBoolean isRunning;
    private final BlockingQueue<Future<?>> tasks;
}
