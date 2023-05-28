package org.tms.common.auth.configuration.global.core.fork_join_pool_example;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

/**
 * Example of using ForkJoinPool executor.
 *
 * More info can see on https://www.baeldung.com/java-fork-join .
 */
public class Main {

    public static void main(String[] args) {
         var executor = new ForkJoinPool(3);

        //First Experiment:
         int[] arr = {1,3,5,34,12,4,5,6,78,5,45,6,78,90,56,17,34,23,12,21,23,42,54,35,67};
         var task = new MyRecursiveTask(arr, 4);

//         executor.execute(task);
//         int res = task.join();

        int res = executor.invoke(task);
        System.out.println(res);


        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        //Second Experiment:
        Integer[] arr2 = {1,3,5,34,12,4,5,6,78,5,45,6,78,90,56,17,34,23,12,21,23,42,54,35,67};
        List<Integer> list = Arrays.asList(arr2);
        var taskSec = new MyUpgratedRecursiveTask(list.spliterator(), 4);

        int res2 = executor.invoke(taskSec);
        System.out.println(res2);
    }
}