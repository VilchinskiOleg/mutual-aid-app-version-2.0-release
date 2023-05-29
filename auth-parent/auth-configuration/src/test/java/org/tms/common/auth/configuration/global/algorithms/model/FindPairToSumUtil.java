package org.tms.common.auth.configuration.global.algorithms.model;

import java.util.HashSet;

public class FindPairToSumUtil {

    public static int[] findPairToSum0(int[] arr, int expectedSum) {

        for (int i = 0; i < arr.length - 1; i++) {
            for (int n = i + 1; n < arr.length; n++) {
                if (arr[i] + arr[n] == expectedSum) {
                    return new int[] {arr[i], arr[n]};
                }
            }
        }

        return new int[0];
    }

    public static int[] findPairToSum1(int[] arr, int expectedSum) {

        HashSet<Integer> cash = new HashSet<>();

//        for (int i = 0; i < arr.length; i++) {
//            if (cash.contains(expectedSum - arr[i])) {
//                return new int[] {arr[i], expectedSum - arr[i]};
//            }
//            cash.add(arr[i]);
//        }

        for (int j : arr) {
            if (cash.contains(expectedSum - j)) {
                return new int[]{j, expectedSum - j};
            }
            cash.add(j);
        }

        return new int[0];
    }

    public static int[] findPairToSum2(int[] arr, int expectedSum) {

        for (int i = 0; i < arr.length - 1; i++) {
            int left = i + 1;
            int right = arr.length - 1;
            while (left <= right) {
                int mid = left + (right - left)/2;
                if (arr[i] + arr[mid] == expectedSum) {
                    return new int[] {arr[i], arr[mid]};
                } else if (arr[i] + arr[mid] > expectedSum) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
        }

        return new int[0];
    }

    public static int[] findPairToSum3(int[] arr, int expectedSum) {

        int i = 0, j = arr.length - 1;
        while (i < j) {
            if (arr[i] + arr[j] == expectedSum) {
                return new int[] {arr[i], arr[j]};
            } else if (arr[i] + arr[j] > expectedSum) {
                j--;
            } else {
                i++;
            }
        }

        return new int[0];
    }
}