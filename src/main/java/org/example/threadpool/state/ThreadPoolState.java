package org.example.threadpool.state;

public interface ThreadPoolState {
    void execute(Runnable task);
    void shutdown();
    boolean isTerminated();
}