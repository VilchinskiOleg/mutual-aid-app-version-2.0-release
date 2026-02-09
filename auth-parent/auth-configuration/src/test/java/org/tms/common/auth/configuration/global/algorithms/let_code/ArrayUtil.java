package org.tms.common.auth.configuration.global.algorithms.let_code;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class ArrayUtil {

    /**
     * 88. Merge Sorted Array
     *
     * You are given two integer arrays nums1 and nums2, sorted in non-decreasing order,
     * and two integers m and n, representing the number of elements in nums1 and nums2 respectively.
     *
     * Merge nums1 and nums2 into a single array sorted in non-decreasing order.
     *
     * The final sorted array should not be returned by the function, but instead be stored inside
     * the array nums1. To accommodate this, nums1 has a length of m + n, where the first m elements
     * denote the elements that should be merged, and the last n elements are set to 0 and
     * should be ignored. nums2 has a length of n.
     */

    /**
     * Option #1. Speed -> O(m + n). But return new array (uses additional memory) :
     */
    public static int[] mergeSortedArrays_IntoNewOne(int[] nums1, int m, int[] nums2, int n){

        int [] res = new int [m + n];

        for (int mCur = m, nCur = n; !(mCur == 0 && nCur == 0);){

            int index = (m + n) - (mCur + nCur);

            if ( mCur != 0
                && ( nCur == 0 || nums1[m - mCur] <= nums2[n - nCur] ) )
            {
                res[index] = nums1[m - mCur];
                mCur--;
            } else {
                res[index] = nums2[n - nCur];
                nCur--;
            }
        }

        return res;
    }

    /**
     * Option #2. Speed -> O(m + n), but uses additional memory subNum1 = num1/2 :
     */
    public static void mergeSortedArrays_IntoFirstOne(int[] nums1, int m, int[] nums2, int n){

        int [] subNums1 = Arrays.copyOfRange(nums1, 0, m);

        for (int mCur = m, nCur = n; !(mCur == 0 && nCur == 0);){

            int index = (m + n) - (mCur + nCur);

            if ( mCur != 0
                && ( nCur == 0 || subNums1[m - mCur] <= nums2[n - nCur] ) )
            {
                nums1[index] = subNums1[m - mCur];
                mCur--;
            } else {
                nums1[index] = nums2[n - nCur];
                nCur--;
            }
        }
    }

    /**
     * Option #3. Speed -> O(m + n), and without additional memory usage. The BEST Option :
     */
    public static void mergeSortedArrays_IntoFirstOne_NoExtraMemoryUsage(int[] nums1, int m, int[] nums2, int n) {

        for (int mCur = m - 1, nCur = n - 1; !(mCur < 0 && nCur < 0); ) {

            int index = mCur + nCur + 1;

            if ( mCur >= 0 && (nCur < 0 || nums1[mCur] >= nums2[nCur]) ) {
                nums1[index] = nums1[mCur];
                mCur--;
            } else {
                nums1[index] = nums2[nCur];
                nCur--;
            }
        }
    }



    /**
     * 27. Remove Element
     *
     * Given an integer array nums and an integer val, remove all occurrences of val in nums in-place.
     * The order of the elements may be changed. Then return the number of elements in nums which are not equal to val.
     *
     * Consider the number of elements in nums which are not equal to val be k, to get accepted,
     * you need to do the following things:
     *
     * - Change the array nums such that the first k elements of nums contain the elements which are
     * not equal to val. The remaining elements of nums are not important as well as the size of nums.
     * - Return k.
     *
     *
     * Speed -> O(n)
     */

    public static int removeElement(int[] nums, int val) {
        int limit = nums.length;

        for ( int i = nums.length - 1; i >= 0; i-- ){

            if ( nums[i] == val ){
                if (i != limit - 1){
                    nums[i] = nums[limit - 1];
                }
                limit--;
            }

        }

        return limit;
    }



    /**
     * 26. Remove Duplicates from Sorted Array
     *
     * Given an integer array nums sorted in non-decreasing order, remove the duplicates in-place
     * such that each unique element appears only once. The relative order of the elements should be kept the same.
     *
     * Consider the number of unique elements in nums to be k.
     * After removing duplicates, return the number of unique elements k.
     *
     * The first k elements of nums should contain the unique numbers in sorted order.
     * The remaining elements beyond index k - 1 can be ignored.
     *
     *
     * Speed -> O(n)
     */

    public static int removeDuplicatesFromSortedArray(int[] nums){

        int limit = nums.length;
        int valueToCompareWith = nums[0];

        for (int i = 1; i < nums.length; i++){

            if (nums[i] == valueToCompareWith){
                limit--;
            }

            int gap = nums.length - limit;
            if (gap != 0){
                nums[i - gap] = nums[i];
            }

            valueToCompareWith = nums[i];
        }

        return limit;
    }



    /**
     * 80. Remove Duplicates from Sorted Array II
     *
     * Given an integer array nums sorted in non-decreasing order, remove some duplicates in-place
     * such that each unique element appears at most twice. The relative order of the elements should be kept the same.
     *
     * Since it is impossible to change the length of the array in some languages, you must instead
     * have the result be placed in the first part of the array nums. More formally, if there are k
     * elements after removing the duplicates, then the first k elements of nums should hold the final result.
     * It does not matter what you leave beyond the first k elements.
     *
     * Return k after placing the final result in the first k slots of nums.
     *
     * Do not allocate extra space for another array. You must do this by modifying the input array
     * in-place with O(1) extra memory.
     *
     *
     * Speed -> O(n)
     */

    public static int removeDuplicatesFromSortedArrayII(int[] nums){

        int limit = nums.length;
        int valueToCompareWith = nums[0];
        boolean alreadyHasDuplicate = false;

        for (int i = 1; i < nums.length; i++){

            if (nums[i] == valueToCompareWith && alreadyHasDuplicate){
                limit--;
            } else {
                alreadyHasDuplicate = nums[i] == valueToCompareWith;
            }

            int gap = nums.length - limit;
            if (gap != 0){
                nums[i - gap] = nums[i];
            }

            valueToCompareWith = nums[i];
        }

        return limit;
    }



    /**
     * 169. Majority Element
     *
     *  Given an array nums of size n, return the majority element.
     *
     * The majority element is the element that appears more than ⌊n / 2⌋ times.
     * You may assume that the majority element always exists in the array.
     */

    public static int majorityElement(int[] nums) {

        int counter = 1;
        int candidate = nums[0];

        for(int i = 1; i < nums.length; i++ ){

            counter += nums[i] == candidate ? 1 : - 1;

            if (counter == 0){
                candidate = nums[i];
                counter = 1;
            }
        }

        return candidate;
    }

    public static int majorityElement_almostTheSameImpl(int[] nums) {

        int counter = 1;
        Integer candidate = null;

        for(int num : nums){

            if (counter == 0){
                candidate = num;
            }

            counter += (num == candidate) ? 1 : - 1;
        }

        return candidate;
    }



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
     * 55. Jump Game
     *
     * You are given an integer array nums. You are initially positioned at the array's first index,
     * and each element in the array represents your maximum jump length at that position.
     *
     * Return true if you can reach the last index, or false otherwise.
     */

    /**
     * Option #1:
     */
    public static boolean canJump_first(int[] nums) {

        boolean isLastIndex = nums.length == 1;
        Deque<Integer> stack = new LinkedList<>();
        stack.push(0);

        while (!isLastIndex && !stack.isEmpty()) {

            int currentPosition = stack.pollFirst();
            int options = nums[currentPosition];

            if (options > 0){
                for (int i = options, lim = 10; i >= 1 || lim > 0; i--, lim--){
                    if (currentPosition + i >= nums.length - 1){
                        return true;
                    }
                    stack.push(currentPosition + (options - i + 1));
                }
            }
        }

        return isLastIndex;
    }


    /**
     * Option #2:
     */
    public static boolean canJump_second(int[] nums) {

        return process_forSecond(nums, 0);
    }

    private static boolean process_forSecond(int[] nums, int currentPosition) {
        if (currentPosition >= nums.length - 1){
            return true;
        }

        int options = nums[currentPosition];

        if (options > 0){
            for (int i = options; i >= 1; i--){
                if (process_forSecond(nums, currentPosition + i)){
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Option #3:
     */
    public static boolean canJump_third(int[] nums) {

        return process_forThird(nums, 0);
    }

    private static boolean process_forThird(int[] nums, int currentPosition) {
        int maxJump = nums[currentPosition];

        if (currentPosition + maxJump >= nums.length - 1){
            return true;
        }

        if (maxJump > 0){
            Integer[] jumpOptions = IntStream.range(currentPosition + 1, currentPosition + maxJump + 1)
                .boxed()
                .toArray(Integer[]::new);
            Arrays.sort(
                jumpOptions,
                (o1, o2) -> (o2 + nums[o2]) - (o1 + nums[o1])
            );

            for (Integer jumpOption : jumpOptions){
                if (process_forThird(nums, jumpOption)){
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Option #4:
     */
    public static boolean canJump(int[] nums) {

        return process(nums, 0);
    }

    private static boolean process(int[] nums, int currentPosition) {
        int maxJump = nums[currentPosition];

        if (currentPosition + maxJump >= nums.length - 1){
            return true;
        }

        if (maxJump > 0){
            int jumpOption = IntStream.range(currentPosition + 1, currentPosition + maxJump + 1)
                .boxed()
                .sorted((o1, o2) -> (o2 + nums[o2]) - (o1 + nums[o1]))
                .findFirst().get();

            return process(nums, jumpOption);
        }

        return false;
    }



    /**
     * 45. Jump Game II
     *
     * You are given a 0-indexed array of integers nums of length n. You are initially positioned at index 0.
     *
     * Each element nums[i] represents the maximum length of a forward jump from index i. In other words,
     * if you are at index i, you can jump to any index (i + j) where:
     *
     *      0 <= j <= nums[i] and
     *      i + j < n
     *
     * Return the minimum number of jumps to reach index n - 1. The test cases are generated such
     * that you can reach index n - 1.
     */

    public static int jump(int[] nums) {
        return process(nums, 0, 0);
    }

    private static int process(int[] nums, int currentPosition, int counter) {
        int maxJump = nums[currentPosition];

        if (currentPosition == nums.length - 1){
            return counter;
        } else if (currentPosition + maxJump >= nums.length - 1){
            counter++;
            return counter;
        }

        int jumpOption = IntStream.range(currentPosition + 1, currentPosition + maxJump + 1)
            .boxed()
            .sorted((o1, o2) -> (o2 + nums[o2]) - (o1 + nums[o1]))
            .findFirst().get();

        counter++;

        return process(nums, jumpOption, counter);
    }

    // Попробовать то же свмое реализовать через цикл (*) ?



    /**
     * 274. H-Index
     *
     * Given an array of integers citations where citations[i] is the number of citations a researcher
     * received for their ith paper, return the researcher's h-index.
     *
     * According to the definition of h-index on Wikipedia: The h-index is defined as the maximum
     * value of h such that the given researcher has published at least h papers that have each been
     * cited at least h times.
     */

    /**
     * Speed -> O(n^2); Memory -> O(1)
     */
    public static int hIndex_v1(int[] citations) {

        for (int hIndex = citations.length; hIndex > 0 ; hIndex--){
            int counter = 0;
            for (int citation : citations){
                if (citation >= hIndex) counter++;
            }
            if (counter >= hIndex) return hIndex;
        }

        return 0;
    }

    /**
     * Speed -> O(n log n); Memory -> O(1)
     */
    public static int hIndex_v2(int[] citations) {
        int hIndex = 0;
        Arrays.sort(citations); // Speed -> O(n log n)

        for (int i = citations.length - 1; i >= 0; i--){
            int hIndexToCheck = citations.length - i;
            if(citations[i] >= hIndexToCheck){
                hIndex = hIndexToCheck;
            }else{
                break;
            }
        }

        return hIndex;
    }

    /**
     * Speed -> O(n); Memory -> O(n)
     */
    public static int hIndex_v3(int[] citations) {
        int hIndex = 0;

        int[] hIndexCandidatesCounter = new int[citations.length + 1];
        for (int i = 0; i < citations.length; i++){
            int hIndexCandidate = Math.min(citations[i], citations.length);
            hIndexCandidatesCounter[hIndexCandidate] += 1;
        }

        // Speed -> O(n)
        int sortedCitIndex = 0;
        for(int hIndexCandidate = 0; hIndexCandidate < hIndexCandidatesCounter.length; hIndexCandidate++){
            for (int count = hIndexCandidatesCounter[hIndexCandidate]; count > 0; count--){
                citations[sortedCitIndex] = hIndexCandidate;
                sortedCitIndex++;
            }
        }

        for (int i = citations.length - 1; i >= 0; i--){
            int hIndexToCheck = citations.length - i;
            if(citations[i] >= hIndexToCheck){
                hIndex = hIndexToCheck;
            }else{
                break;
            }
        }

        return hIndex;
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



    /**
     * 560. Subarray Sum Equals K
     *
     * Given an array of integers nums and an integer k, return the total number of subarrays whose sum equals to k.
     *
     * A subarray is a contiguous non-empty sequence of elements within an array.
     *
     * Example 1:
     *
     *      Input: nums = [1,1,1], k = 2
     *      Output: 2
     *
     * Example 2:
     *
     *      Input: nums = [1,2,3], k = 3
     *      Output: 2
     */

    /**
     * Approach #1 :
     *
     * Time complexity - O(n)
     * [You can use this approach only for positive numbers (!) ]
     */
    public static int subarraySum_withSlightWindow(int[] nums, int k) {
        int counter = 0, sum = nums[0];
        int leftP = 0, rightP = 0;
        int lastNumsP = nums.length - 1;


        while (leftP < lastNumsP || rightP < lastNumsP) {

            if (sum == k) counter++;

            if (sum < k && rightP < lastNumsP) {
                rightP++;
                sum += nums[rightP];
            } else {
                sum -= nums[leftP];
                leftP++;
            }
        }

        if (sum == k) counter++;

        return counter;
    }

    /**
     * Approach #2 :
     *
     * Time complexity - O(n)
     * [Also works with negative numbers]
     */
    public static int subarraySum_withPrefixSum(int[] nums, int k) {
        Map<Integer, Integer> sumCache = new HashMap<>();
        int totalSum = 0;
        int kCounter = 0;

        for (int num : nums) {
            totalSum += num;

            // 1. Check if total sum equals k :
            if (totalSum == k) kCounter++;
            // 2. Check if any sub-array sum equals k :
            if (!sumCache.isEmpty()) {
                for (int subArraySum : sumCache.keySet()) {
                    if (totalSum - subArraySum == k) kCounter += sumCache.get(subArraySum);
                }
            }

            int curCount = sumCache.computeIfAbsent(totalSum, key -> 0);
            sumCache.put(totalSum, curCount + 1);
        }

        return kCounter;
    }
}
