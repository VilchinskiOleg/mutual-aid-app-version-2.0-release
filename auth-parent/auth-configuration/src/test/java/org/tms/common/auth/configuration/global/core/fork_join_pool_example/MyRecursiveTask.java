package org.tms.common.auth.configuration.global.core.fork_join_pool_example;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class MyRecursiveTask extends RecursiveTask<Integer> {

    int [] args;
    int sizePerOneTask;

    public MyRecursiveTask(int[] args, int sizePerOneTask) {
        this.args = args;
        this.sizePerOneTask = sizePerOneTask;
    }

    @Override
    protected Integer compute() {
        if (args.length > sizePerOneTask) {
            return ForkJoinTask.invokeAll(splitTask(args)).stream()
                    .mapToInt(ForkJoinTask::join)
                    .sum();
        } else {
            return calculate(args);
        }
    }

    private Collection<MyRecursiveTask> splitTask(int[] args) {
        int middleOfArray = args.length/2;
        var firstPart = Arrays.copyOfRange(args, 0, middleOfArray);
        var secondPart = Arrays.copyOfRange(args, middleOfArray, args.length);

        MyRecursiveTask [] tasks = {new MyRecursiveTask(firstPart, sizePerOneTask), new MyRecursiveTask(secondPart, sizePerOneTask)};
        return Arrays.asList(tasks);
    }

    private int calculate(int [] args) {
        System.out.println("process task by Thread = " + Thread.currentThread().getName());
        return Arrays.stream(args).sum();
    }
}