package org.example.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.task.RequestTask;
import org.example.threadpool.ThreadPool;

import java.util.concurrent.Future;

public class WebServer {
    private static final Logger logger = LogManager.getLogger(WebServer.class);
    private final ThreadPool threadPool;
    private volatile boolean running;

    public WebServer(int poolSize) {
        this.threadPool = new ThreadPool(poolSize);
        this.running = true;
        logger.info("Web server initialized with threefold of size {}", poolSize);
    }

    public Future<String> handleRequest(String clientId, String request) {
        if (!running) {
            logger.warn("server is stopped, clients request {} rejected", clientId);
            return null;
        }

        logger.info("got a request from client {}: {}", clientId, request);
        RequestTask task = new RequestTask(clientId, request);
        return threadPool.submit(task);
    }

    public void shutdown() {
        running = false;
        logger.info("stopping web-server...");
        threadPool.shutdown();
        logger.info("web server is stopped");
    }
}