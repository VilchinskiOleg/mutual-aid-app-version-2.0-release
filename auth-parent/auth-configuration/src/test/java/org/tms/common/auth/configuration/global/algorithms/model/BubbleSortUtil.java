package org.tms.common.auth.configuration.global.algorithms.model;

public class BubbleSortUtil {

    /**
     * Ascending sort.
     */
    public static void sortAsc(int[] arr) {

        for(int i = arr.length - 1; i > 0; --i) {
            for(int n = 0; n < i; ++n) {
                if(arr[n] > arr[n+1]) {
                    int temp = arr[n+1];
                    arr[n+1] = arr[n];
                    arr[n] = temp;
                }
            }
        }
    }

    /**
     * Descending sort.
     */
    public static void sortDesc(int[] arr) {

        for(int i = 0; i < arr.length - 1; ++i) {
            for(int n = arr.length - 1; n > i; --n ) {
                if(arr[n] > arr[n - 1]) {
                    int temp = arr[n-1];
                    arr[n-1] = arr[n];
                    arr[n] = temp;
                }
            }
        }
    }
}