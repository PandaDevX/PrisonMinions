package com.lifeonblack.prisonminions.lib.workload;

import com.google.common.collect.Queues;
import java.util.ArrayDeque;

public class WorkThread implements Runnable {

    private final ArrayDeque<Task> taskQueue;
    public WorkThread() {
        this.taskQueue = Queues.newArrayDeque();
    }

    public void addTask(Task task) {
        taskQueue.add(task);
    }


    @Override
    public void run() {
        while(!taskQueue.isEmpty()) {
            taskQueue.poll().performTask();
        }
    }
}
