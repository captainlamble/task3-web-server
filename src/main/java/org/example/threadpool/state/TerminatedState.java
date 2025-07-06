package org.example.threadpool.state;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TerminatedState implements ThreadPoolState {
    private static final Logger logger = LogManager.getLogger(TerminatedState.class);

    private static final TerminatedState INSTANCE = new TerminatedState();

    private TerminatedState() {
        logger.info("thread pool in terminated state");
    }

    public static TerminatedState getInstance() {
        return INSTANCE;
    }

    @Override
    public void execute(Runnable task) {
        if (task == null) {
            throw new NullPointerException("Task cannot be null");
        }
        throw new IllegalStateException("thread pool is terminated, cannot accept new tasks");
    }

    @Override
    public void shutdown() {
        logger.debug("shutdown() called on already terminated pool - no action needed");
    }

    @Override
    public boolean isTerminated() {
        return true;
    }
}