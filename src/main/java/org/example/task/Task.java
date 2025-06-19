package org.example.task;

import java.util.concurrent.Callable;

public interface Task<T> extends Callable<T> {

}