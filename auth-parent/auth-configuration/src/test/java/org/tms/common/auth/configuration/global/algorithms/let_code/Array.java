package org.tms.common.auth.configuration.global.algorithms.let_code;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Array {

    /**
     * 189. Rotate Array
     *
     * Given an integer array nums, rotate the array to the right by k steps, where k is non-negative.
     */

    public static void rotate(int[] nums, int k) {
        if (nums.length <= 1 || k % nums.length == 0) return;
        k = k % nums.length;
        reversFun(nums, 0, nums.length);
        reversFun(nums, 0, k);
        reversFun(nums, k, nums.length);
    }

    private static void reversFun(int[] nums, int left, int right) {
        for (int i = 0; i < (right - left) / 2; i++) {
            int temp = nums[left + i];
            nums[left + i] = nums[right - 1 - i];
            nums[right - 1 - i] = temp;
        }
    }


    /**
     * 121. Best Time to Buy and Sell Stock
     *
     * You are given an array prices where prices[i] is the price of a given stock on the ith day.
     *
     * You want to maximize your profit by choosing a single day to buy one stock and choosing a different day in the future to sell that stock.
     *
     * Return the maximum profit you can achieve from this transaction. If you cannot achieve any profit, return 0.
     */

    public static int maxProfit_I(int[] prices) {

        Integer priceToBuy = null;
        int delta = 0;

        for (int i = 1; i < prices.length; i++){

            if (prices[i] > prices[i - 1]){

                int delta2 = prices[i] - prices[i - 1];
                int delta3 = priceToBuy != null ? prices[i] - priceToBuy : -1;

                delta = Math.max(delta, Math.max(delta2, delta3));

                priceToBuy = priceToBuy != null ? Math.min(prices[i - 1], priceToBuy) : prices[i - 1];
            }
        }
        return delta;
    }


    /**
     * 122. Best Time to Buy and Sell Stock II
     *
     * You are given an integer array prices where prices[i] is the price of a given stock on the ith day.
     *
     * On each day, you may decide to buy and/or sell the stock. You can only hold at most one share of the stock at any time.
     * However, you can buy it then immediately sell it on the same day.
     *
     * Find and return the maximum profit you can achieve.
     */

    private static List<Integer> minVals;
    private static List<Integer> maxVals;

    public static int maxProfit_II(int[] prices) {
        minVals = new ArrayList<>(prices.length / 2);
        maxVals = new ArrayList<>(prices.length / 2);

        for (int i = 1; i < prices.length; i++){
            if (prices[i - 1] < prices[i] && minVals.size() == maxVals.size()) {
                minVals.add(prices[i - 1]);
            } else if (prices[i - 1] > prices[i] && minVals.size() != maxVals.size()) {
                maxVals.add(prices[i - 1]);
            }
            // Check last element :
            if (i == prices.length - 1 && minVals.size() != maxVals.size()
                        && Math.min(prices[i - 1], minVals.get(minVals.size() - 1)) < prices[i])
                maxVals.add(prices[i]);
        }

        if (minVals.size() > maxVals.size()) minVals.remove(minVals.size() - 1);
        if (minVals.isEmpty()) return 0;

        Integer[] indexes = IntStream.rangeClosed(0, minVals.size() - 1).boxed().toArray(Integer[]::new);
        return calculateDelta(indexes);
    }

    private static int calculateDelta(Integer[] indexes) {

        if (indexes.length == 1) {
            return maxVals.get(indexes[0]) - minVals.get(indexes[0]);
        } else {
            int mid = indexes.length / 2;
            Integer[] head = Arrays.copyOfRange(indexes, 0, mid);
            Integer[] tail = Arrays.copyOfRange(indexes, mid, indexes.length);
            return Math.max(
                    calculateDelta(head) + calculateDelta(tail),
                    maxVals.get(indexes[indexes.length - 1]) - minVals.get(indexes[0])
            );
        }
    }


    /**
     * 134. Gas Station
     *
     * There are n gas stations along a circular route, where the amount of gas at the ith station is gas[i].
     *
     * You have a car with an unlimited gas tank and it costs cost[i] of gas to travel from the ith station to its
     * next (i + 1)th station. You begin the journey with an empty tank at one of the gas stations.
     *
     * Given two integer arrays gas and cost, return the starting gas station's index if you can travel around the
     * circuit once in the clockwise direction, otherwise return -1. If there exists a solution, it is guaranteed to be unique.
     */

    public static int canCompleteCircuit_I(int[] gas, int[] cost) {
        if (gas.length == 1) {
            return gas [0] >= cost [0] ? 0 : -1;
        }

        int [] diff = new int [gas.length];
        for (int i = 0; i < gas.length; i++) {
            diff [i] = gas [i] - cost [i];
        }

        int startInd = -1, diffIncome = 0;

        for (int i = 0; i < diff.length * 2; i++) {
            if (i >= diff.length && startInd == -1) break;

            int curInd = i % diff.length;
            int nextInd = (i + 1) % diff.length;
            diffIncome += diff [curInd];

            if (diffIncome >= 0 && startInd == nextInd) {
                return startInd;
            } else if (diffIncome > 0 && startInd < 0) {
                startInd = curInd;
            } else if (diffIncome <= 0) {
                startInd = -1;
                diffIncome = 0;
            }
        }

        return -1;
    }

    public static int canCompleteCircuit_II(int[] gas, int[] cost) {
        if (gas.length == 1) {
            return gas [0] >= cost [0] ? 0 : -1;
        }

        int startInd = -1, diffIncome = 0;
        int [] diff = new int [gas.length];

        for (int i = 0; i < gas.length * 2; i++) {
            if (i < gas.length) {
                diff[i] = gas[i] - cost[i];
            } else if (startInd == -1) {
                break;
            }

            int curInd = i % diff.length;
            int nextInd = (i + 1) % diff.length;
            diffIncome += diff [curInd];

            if (diffIncome >= 0 && startInd == nextInd) {
                return startInd;
            } else if (diffIncome > 0 && startInd < 0) {
                startInd = curInd;
            } else if (diffIncome <= 0) {
                startInd = -1;
                diffIncome = 0;
            }
        }

        return -1;
    }
}
