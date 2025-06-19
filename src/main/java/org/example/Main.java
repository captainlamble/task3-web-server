package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.client.Client;
import org.example.server.WebServer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        int poolSize = 10;
        int numClients = 20;
        int requestsPerClient = 5;

        WebServer server = new WebServer(poolSize);

        try (ExecutorService clientExecutor = Executors.newFixedThreadPool(numClients)) {

            for (int i = 0; i < numClients; i++) {
                Client client = new Client("Client-" + i, server, requestsPerClient);
                clientExecutor.submit(client);
            }

            clientExecutor.shutdown();
            try {
                if (!clientExecutor.awaitTermination(2, TimeUnit.MINUTES)) {
                    logger.warn("force to stop client by timeout");
                    clientExecutor.shutdownNow();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                clientExecutor.shutdownNow();
                logger.error("stopping thread, waiting for client", e);
            }
        }
        server.shutdown();

        logger.info("app is finished");
    }
}