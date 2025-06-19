package org.example.threadpool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorkerThread extends Thread {
    private static final Logger logger = LogManager.getLogger(WorkerThread.class);
    private final ThreadPool threadPool;
    private volatile boolean running;

    public WorkerThread(ThreadPool threadPool, String name) {
        super(name);
        this.threadPool = threadPool;
        this.running = true;
    }

    @Override
    public void run() {
        logger.info("worker thread started: {}", getName());

        while (running && !Thread.currentThread().isInterrupted()) {
            try {
                Runnable task = threadPool.getTaskQueue().take();
                logger.debug("thread {} got a task", getName());

                try {
                    task.run();
                } catch (Exception e) {
                    logger.error("error in thread {}", getName(), e);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.info("thread {} interrupted", getName());
                break;
            }
        }

        logger.info("worker thread finished: {}", getName());
    }

    public void stopWorker() {
        running = false;
        interrupt();
    }
}