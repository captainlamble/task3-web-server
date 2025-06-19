package org.example.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.server.WebServer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Client implements Runnable {
    private static final Logger logger = LogManager.getLogger(Client.class);
    private final String clientId;
    private final WebServer server;
    private final int numRequests;

    public Client(String clientId, WebServer server, int numRequests) {
        this.clientId = clientId;
        this.server = server;
        this.numRequests = numRequests;
        logger.info("Client {} created", clientId);
    }

    @Override
    public void run() {
        List<Future<String>> responses = new ArrayList<>();

        for (int i = 0; i < numRequests; i++) {
            String request = "request-" + i + " from " + clientId;
            logger.info("client {} sending request: {}", clientId, request);

            Future<String> future = server.handleRequest(clientId, request);
            if (future != null) {
                responses.add(future);
            }
            try {
                TimeUnit.MILLISECONDS.sleep(200 + (int) (Math.random() * 300));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        for (Future<String> future : responses) {
            try {
                String response = future.get();
                logger.info("client {} got response: {}", clientId, response);
            } catch (Exception e) {
                logger.error("client " + clientId + " got an error while processing the response", e);
            }
        }

        logger.info("client {} stopped", clientId);
    }
}