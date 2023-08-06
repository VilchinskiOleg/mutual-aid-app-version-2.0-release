package org.tms.common.auth.configuration.global.core.fork_join_pool_example;

import java.util.Arrays;
import java.util.Collection;
import java.util.Spliterator;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicInteger;

public class MyUpgratedRecursiveTask extends RecursiveTask<Integer> {

    Spliterator<Integer> spl;
    int sizePerOneTask;

    public MyUpgratedRecursiveTask(Spliterator<Integer> spl, int sizePerOneTask) {
        this.spl = spl;
        this.sizePerOneTask = sizePerOneTask;
    }

    @Override
    protected Integer compute() {
        if (spl.estimateSize() > sizePerOneTask) {
            System.out.println("process task by Thread = " + Thread.currentThread().getName());
            return ForkJoinTask.invokeAll(splitTask(spl)).stream()
                    .mapToInt(ForkJoinTask::join)
                    .sum();
        } else {
            AtomicInteger result = new AtomicInteger();
            spl.forEachRemaining(result::addAndGet);
            return result.get();
        }
    }

    private Collection<MyUpgratedRecursiveTask> splitTask(Spliterator<Integer> spl) {
        var secondSpl = spl.trySplit();

        MyUpgratedRecursiveTask [] tasks = {
                new MyUpgratedRecursiveTask(spl, sizePerOneTask),
                new MyUpgratedRecursiveTask(secondSpl, sizePerOneTask)};
        return Arrays.asList(tasks);
    }
}