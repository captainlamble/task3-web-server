package org.example.threadpool.state;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.threadpool.ThreadPool;

public class RunningState implements ThreadPoolState {
    private static final Logger logger = LogManager.getLogger(RunningState.class);
    private final ThreadPool threadPool;

    public RunningState(ThreadPool threadPool) {
        this.threadPool = threadPool;
    }

    @Override
    public void execute(Runnable task) {
        if (task != null) {
            boolean added = threadPool.getTaskQueue().offer(task);
            if (added) {
                logger.debug("task is added to queue");
            } else {
                logger.warn("cannot add task to queue");
            }
        }
    }

    @Override
    public void shutdown() {
        logger.info("transitioning to state shutting down");
        threadPool.setState(new ShuttingDownState(threadPool));
    }

    @Override
    public boolean isTerminated() {
        return false;
    }
}