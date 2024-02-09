package main.propertease.server;

import main.propertease.server.strategy.AppointmentClientStrategy;
import main.propertease.server.strategy.HandlerClientStrategy;
import main.propertease.server.strategy.PosterClientStrategy;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class StartServer {
    public static void main(String[] args) throws Exception {
        try (final var forkPool = ForkJoinPool.commonPool()) {
            final var servers = new Server[]{
                new Server(1928),
                new Server(1927),
                new Server(1926),
            };
            forkPool.execute(() -> servers[0].start(AppointmentClientStrategy::new));
            forkPool.execute(() -> servers[1].start(PosterClientStrategy::new));
            forkPool.execute(() -> servers[2].start(HandlerClientStrategy::new));
            while (!forkPool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS)) {
                Thread.sleep(1000);
            }
        }
    }
}
