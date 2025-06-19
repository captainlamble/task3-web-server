package org.example.threadpool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.threadpool.state.RunningState;
import org.example.threadpool.state.ThreadPoolState;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ThreadPool {
    private static final Logger logger = LogManager.getLogger(ThreadPool.class);
    private final BlockingQueue<Runnable> taskQueue;
    private final List<WorkerThread> workers;
    private ThreadPoolState state;

    public ThreadPool(int poolSize) {
        this.taskQueue = new LinkedBlockingQueue<>();
        this.workers = new ArrayList<>(poolSize);
        this.state = new RunningState(this);

        for (int i = 0; i < poolSize; i++) {
            WorkerThread worker = new WorkerThread(this, "Worker-" + i);
            workers.add(worker);
            worker.start();
        }

        logger.info("thread pool initialized with {} worker threads", poolSize);
    }

    public void execute(Runnable task) {
        state.execute(task);
    }

    public <T> Future<T> submit(Callable<T> task) {
        FutureTask<T> futureTask = new FutureTask<>(task);
        execute(futureTask);
        return futureTask;
    }

    public void shutdown() {
        logger.info("shutting down thread pool");
        state.shutdown();

        boolean terminated = false;
        try {
            for (int i = 0; i < 10 && !terminated; i++) {
                terminated = state.isTerminated();
                if (!terminated) {
                    TimeUnit.MILLISECONDS.sleep(100);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        logger.info("thread pool shutdown");
    }

    public void setState(ThreadPoolState state) {
        this.state = state;
    }

    public BlockingQueue<Runnable> getTaskQueue() {
        return taskQueue;
    }

    public void stopWorkers() {
        logger.info("stopping workers");
        for (WorkerThread worker : workers) {
            worker.stopWorker();
        }
    }

    public boolean areAllWorkersTerminated() {
        for (WorkerThread worker : workers) {
            if (worker.isAlive()) {
                return false;
            }
        }
        return true;
    }
}