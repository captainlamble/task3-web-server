package org.example.threadpool.state;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TerminatedState implements ThreadPoolState {
    private static final Logger logger = LogManager.getLogger(TerminatedState.class);

    public TerminatedState() {
        logger.info("thread pool in terminated state");
    }

    @Override
    public void execute(Runnable task) {
        throw new IllegalStateException("thread pool is terminated, cannot accept new tasks");
    }

    @Override
    public void shutdown() {
    }

    @Override
    public boolean isTerminated() {
        return true;
    }
}