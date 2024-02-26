package main.propertease.server;

import main.propertease.server.strategy.AppointmentClientStrategy;
import main.propertease.server.strategy.HandlerClientStrategy;
import main.propertease.server.strategy.PosterClientStrategy;

public class StartServer {
    public static void main(String[] args) throws Exception {
        final var servers = new Server[]{
            new Server(1928),
            new Server(1927),
            new Server(1926),
        };
        final var threads = new Thread[] {
            Thread.ofPlatform().start(() -> servers[0].start(AppointmentClientStrategy::new)),
            Thread.ofPlatform().start(() -> servers[1].start(PosterClientStrategy::new)),
            Thread.ofPlatform().start(() -> servers[2].start(HandlerClientStrategy::new)),
        };
        for (final var thread : threads) {
            thread.join();
        }
    }
}
