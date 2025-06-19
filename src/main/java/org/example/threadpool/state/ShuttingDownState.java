package org.example.threadpool.state;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.threadpool.ThreadPool;

public class ShuttingDownState implements ThreadPoolState {
    private static final Logger logger = LogManager.getLogger(ShuttingDownState.class);
    private final ThreadPool threadPool;

    public ShuttingDownState(ThreadPool threadPool) {
        this.threadPool = threadPool;
        threadPool.stopWorkers();
    }

    @Override
    public void execute(Runnable task) {
        throw new IllegalStateException("thread pool is shutting down, cannot accept new tasks");
    }

    @Override
    public void shutdown() {
        logger.info("thread pool in shutting down state");
    }

    @Override
    public boolean isTerminated() {
        boolean terminated = threadPool.areAllWorkersTerminated();
        if (terminated) {
            logger.info("transferring to state terminated");
            threadPool.setState(new TerminatedState());
        }
        return terminated;
    }
}