package org.tms.common.auth.configuration.global.algorithms.let_code;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.NavigableMap;
import java.util.HashMap;
import java.util.TreeMap;
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

    /**
     * Option #1 :
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
     * Option #2 [in general the same approach, just a bit optimized] :
     */
    public int maxProfit_I_optimized(int[] prices) {

        int maxProfit = 0;
        int minPrice = prices[0];

        for (int i = 1; i < prices.length; i++) {
            minPrice = Math.min(minPrice, prices[i - 1]);
            maxProfit = Math.max(maxProfit, prices[i] - minPrice);
        }

        return maxProfit;
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

    /**
     * Option #1 :
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
     * Option #2 [the best one] :
     */
    public int maxProfit_II_optimized_and_simple(int[] prices) {
        int profit = 0;

        for (int i = 1; i < prices.length; i++) {
            if (prices[i - 1] < prices[i]) profit += prices[i] - prices[i - 1];
        }

        return profit;
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
    public static boolean canJump_recursive(int[] nums) {

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
     * Option #5 [the best one]:
     */
    public static boolean canJump_cycle(int[] nums) {
        int targetPosition = nums.length - 1;

        for (int i = targetPosition - 1; i >= 0; i--) {
            if (i + nums[i] >= targetPosition) targetPosition = i;
        }

        return targetPosition == 0;
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

    /**
     * Option #1 :
     */
    public static int jump_recursive(int[] nums) {
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

    /**
     * Option #2 [the best one]:
     */
    public int jump_cycle(int[] nums) {
        int targetPosition = nums.length - 1;
        int jumpCounter = 0;

        while (targetPosition != 0) {
            Integer earliestStartPosition = null;

            for (int i = targetPosition - 1; i >= 0; i--) {
                if (i + nums[i] >= targetPosition) earliestStartPosition = i;
            }

            targetPosition = earliestStartPosition;
            jumpCounter++;
        }

        return jumpCounter;
    }



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
     * 238. Product of Array Except Self
     *
     * Given an integer array nums, return an array answer such that answer[i] is equal to the product
     * of all the elements of nums except nums[i].
     *
     * The product of any prefix or suffix of nums is guaranteed to fit in a 32-bit integer.
     *
     * You must write an algorithm that runs in O(n) time and without using the division operation.
     */

    /**
     * Speed -> O(n); Memory -> O(n)
     */
    public static int[] productExceptSelf(int[] nums) {
        int[] prefix = new int[nums.length];
        int[] postfix = new int[nums.length];

        int leftTotalProduct = 1;
        int rightTotalProduct = 1;

        // 1. Populate prefix and postfix arrays:
        for (int i = 0; i < nums.length; i++) {

            // Populate arrays:
            prefix[i] = leftTotalProduct;
            postfix[nums.length - 1 - i] = rightTotalProduct;

            // Re-calc totalProducts:
            leftTotalProduct *= nums[i];
            rightTotalProduct *= nums[nums.length - 1 - i];
        }

        // 2. Re-calculate nums:
        for (int i = 0; i < nums.length; i++) {
            nums[i] = prefix[i] * postfix[i];
        }

        return nums;
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

    public static int canCompleteCircuit_III(int[] gas, int[] cost) {

        int startInd = 0;
        int gasTank = 0;

        for (int i = 0; i < gas.length * 2; i++) {
            if (i >= gas.length && i % gas.length == startInd) return startInd;

            gasTank += gas[i % gas.length] - cost[i % gas.length];
            if (gasTank < 0) {
                startInd = i + 1;
                gasTank = 0;
            }

            if (startInd >= gas.length) break;
        }

        return -1;
    }



   /**
     * 135. Candy
     *
     * There are n children standing in a line. Each child is assigned a rating value given in the integer array ratings.
     *
     * You are giving candies to these children subjected to the following requirements:
     *
     * Each child must have at least one candy.
     * Children with a higher rating get more candies than their neighbors.
     * Return the minimum number of candies you need to have to distribute the candies to the children.
     */

    public static int candy(int[] ratings) {
        int n = ratings.length;
        int[] candies = new int[n];
        // 0. Default candies propagation, if all would have the same rating :
        Arrays.fill(candies, 1); // fill whole array with val

        // 1. Adjust candies propagation in -> direction :
        for (int i = 1; i < n; i++) {
            if (ratings[i] > ratings[i - 1]) {
                candies[i] = candies[i - 1] + 1;
            }
        }

        // 2. Adjust candies propagation in <- direction :
        for (int i = n - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                candies[i] = Math.max(candies[i], candies[i + 1] + 1);
            }
        }

        // 3. Calculate the result :
        int totalCandies = 0;
        for (int candy : candies) {
            totalCandies += candy;
        }

        return totalCandies;
    }

    /**
     * NOTE:
     * --- Be open to calculate and save the part of work instead of performing whole work in a single iteration ! ---
     */



    /**
     * 42. Trapping Rain Water
     *
     * Given n non-negative integers representing an elevation map where the width of each bar is 1,
     * compute how much water it can trap after raining.
     */

    /**
     * Second approach ( Speed -> O(n); Memory -> O(n) ) :
     */

    public static int trapI(int[] height) {

        int water, maxH;
        int [] maxLeftH = new int [height.length];
        int [] maxRightH = new int [height.length];

        // Calculate max Left Height for [i] element :
        maxH = 0;
        for (int i = 0; i < maxLeftH.length; i++){
            maxLeftH[i] = maxH;
            maxH = Math.max(maxH, height[i]);
        }

        // Calculate max Right Height for [i] element :
        maxH = 0;
        for (int i = maxRightH.length - 1; i >= 0; i--){
            maxRightH[i] = maxH;
            maxH = Math.max(maxH, height[i]);
        }

        // Calculate water spreading :
        water = 0;
        for (int i = 0; i < height.length; i++){
            water += Math.max(Math.min(maxLeftH[i], maxRightH[i]) - height[i], 0);
        }

        return water;
    }

    /**
     * Third approach ( Speed -> O(n); Memory -> O(1) ) :
     */

    public static int trapII(int[] height) {

        int water = 0;

        int maxLeftH = height[0];
        int maxRightH = height[height.length - 1];

        for (int l = 1, r = height.length - 2; l <= r; ){
            if (maxLeftH <= maxRightH){
                water += Math.max(maxLeftH - height[l], 0);
                maxLeftH = Math.max(maxLeftH, height[l]);
                l++;
            } else {
                water += Math.max(maxRightH - height[r], 0);
                maxRightH = Math.max(maxRightH, height[r]);
                r--;
            }
        }

        return water;
    }



    /**
     * 13. Roman to Integer
     * 
     * Roman numerals are represented by seven different symbols: I, V, X, L, C, D and M.
     * 
     *   Symbol       Value
     *   I             1
     *   V             5
     *   X             10
     *   L             50
     *   C             100
     *   D             500
     *   M             1000
     * 
     * For example, 2 is written as II in Roman numeral, just two ones added together. 
     * 12 is written as XII, which is simply X + II. The number 27 is written as XXVII, which is XX + V + II.
     * 
     * Roman numerals are usually written largest to smallest from left to right. However, 
     * the numeral for four is not IIII. Instead, the number four is written as IV. 
     * Because the one is before the five we subtract it making four. The same principle applies to the number nine,
     * which is written as IX. There are six instances where subtraction is used:
     * 
     * - I can be placed before V (5) and X (10) to make 4 and 9. 
     * - X can be placed before L (50) and C (100) to make 40 and 90. 
     * - C can be placed before D (500) and M (1000) to make 400 and 900.
     * 
     * Given a roman numeral, convert it to an integer.
     */

    public static int romanToInt(String s) {
        Map<Character, Integer> romanMap = Map.of(
            'I', 1, 'V', 5, 'X', 10, 'L', 50, 
            'C', 100, 'D', 500, 'M', 1000);
    
        Map<Character, List<Character>> romanPrefixMap = Map.of(
            'I', List.of('V', 'X'), 
            'X', List.of('L', 'C'), 
            'C', List.of('D', 'M'));
        
        int res = 0;
    
        for (int i = 0; i < s.length(); i++) {
            char current = s.charAt(i);
            if (i + 1 < s.length() 
                        && romanPrefixMap.containsKey(current) 
                        && romanPrefixMap.get(current).contains(s.charAt(i + 1))) {
                res -= romanMap.get(current);
            } else {
                res += romanMap.get(current);
            }
        }
        
        return res;   
    }



    /**
     * 12. Integer to Roman
     * 
     * Roman numerals are formed by appending the conversions of decimal place values from highest to lowest. 
     * Converting a decimal place value into a Roman numeral has the following rules:
     * 
     * - If the value does not start with 4 or 9, select the symbol of the maximal value that can be subtracted from the input, 
     * append that symbol to the result, subtract its value, and convert the remainder to a Roman numeral.
     * 
     * - If the value starts with 4 or 9 use the subtractive form representing one symbol subtracted from 
     * the following symbol, for example, 4 is 1 (I) less than 5 (V): IV and 9 is 1 (I) less than 10 (X): IX. 
     * Only the following subtractive forms are used: 4 (IV), 9 (IX), 40 (XL), 90 (XC), 400 (CD) and 900 (CM).    
     * 
     * - Only powers of 10 (I, X, C, M) can be appended consecutively at most 3 times to represent multiples of 10. 
     * You cannot append 5 (V), 50 (L), or 500 (D) multiple times. If you need to append a symbol 4 times use 
     * the subtractive form.
     * 
     * Given an integer, convert it to a Roman numeral.
     */

    private static NavigableMap<Integer, Character> romanMap = new TreeMap<>();

    static {
        romanMap.put(1, 'I');
        romanMap.put(5, 'V');
        romanMap.put(10, 'X');
        romanMap.put(50, 'L');
        romanMap.put(100, 'C');
        romanMap.put(500, 'D');
        romanMap.put(1000, 'M');
    }

    public static String intToRoman(int num) {
            
        Deque<Character> payload = new LinkedList<>();
        int divider = 10;
        int convertedPart = 0;

        while (num != convertedPart) {
            // 1. Calculate number (postfix) for processing :
            int numToProcess = num % divider - convertedPart;
            if (numToProcess == 0) {
                divider *= 10;
                continue;
            } 

            // 2. Processing number (postfix) :
            var rangeFrom = romanMap.floorEntry(numToProcess);
            int rangeFromInt = rangeFrom.getKey();

            if (rangeFrom.getKey() == numToProcess) {
                payload.push(rangeFrom.getValue());
            } else {
                // calculate rangeMinUnit depending on rangeFromInt = 1XX.. or rangeFromInt = 5XX.. :
                int rangeMinUnit = numToProcess - rangeFromInt >= rangeFromInt ? rangeFromInt : rangeFromInt / 5;
                int rangeMinUnitCounter = (numToProcess - rangeFrom.getKey()) / rangeMinUnit;

                if ((rangeMinUnit != rangeFromInt && rangeMinUnitCounter <= 3) 
                        || (rangeMinUnit == rangeFromInt && rangeMinUnitCounter <= 2)) {
                    while (rangeMinUnitCounter-- > 0) {
                        payload.push(romanMap.get(rangeMinUnit));
                    }
                    payload.push(rangeFrom.getValue());
                } else {
                    var rangeTo = romanMap.ceilingEntry(numToProcess);
                    payload.push(rangeTo.getValue());
                    payload.push(romanMap.get(rangeMinUnit));
                }
            }

            // 3. Update data (pointers) :
            divider *= 10;
            convertedPart += numToProcess;
        }

        StringBuilder sb = new StringBuilder();
        payload.forEach(sb::append);
        return sb.toString();
    }
    


    /**
     * 58. Length of Last Word
     * 
     * Given a string s consisting of words and spaces, return the length of the last word in the string.
     * 
     * A word is a maximal substring consisting of non-space characters only.
     */

    public int lengthOfLastWord(String s) {
        int count = 0;
        
        for (int i = s.length() - 1; i >= 0; i--) {
            if (s.charAt(i) == ' ') {
                if (count > 0) break; 
            } else {
                count++;
            }
        }  

        return count;
    }



    /**
     * 14. Longest Common Prefix
     * 
     * Write a function to find the longest common prefix string amongst an array of strings.
     * 
     * If there is no common prefix, return an empty string "".
     */

    public static String longestCommonPrefix(String[] strs) {

        StringBuilder res = new StringBuilder();
        Arrays.sort(strs); // After, first and last strs will differ the most.

        String first = strs[0];
        String last = strs[strs.length-1];

        // Here just compare first and last, and build common subStr :
        for (int i = 0; i < Math.min(first.length(), last.length()); i++) {
            if (first.charAt(i) != last.charAt(i)) {
                return res.toString();
            }
            res.append(first.charAt(i));
        }

        return res.toString();
    }



    /**
     * 151. Reverse Words in a String
     * 
     * Given an input string s, reverse the order of the words.
     * 
     * A word is defined as a sequence of non-space characters. The words in s will be separated by at least one space.
     * 
     * Return a string of the words in reverse order concatenated by a single space.
     * 
     * Note that s may contain leading or trailing spaces or multiple spaces between two words. 
     * The returned string should only have a single space separating the words. Do not include any extra spaces.
     */

    /**
     * Speed -> O(n); Memory -> O(n)
     */
    public static String reverseWordsI(String s) {

        String[] words = s.trim().split("\\s+");

        int l = 0;
        int r = words.length - 1;

        while (l <= r){
            if (l != r){
                String tmp = words[r];
                words[r] = words[l];
                words[l] = tmp;
            }
            l++;
            r--;
        }

        return String.join(" ", words);
    }

    /**
     * Speed -> O(n); Memory -> O(n)
     */
    public static String reverseWordsII(String s) {

        String[] str = s.trim().split("\\s+");

        // Initialize the output string
        String out = "";

        // Iterate through the words in reverse order
        for (int i = str.length - 1; i > 0; i--) {
            // Append the current word and a space to the output
            out += str[i] + " ";
        }

        // Append the first word to the output (without trailing space)
        return out + str[0];
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



    /**
     * 6. Zigzag Conversion
     *
     * The string "PAYPALISHIRING" is written in a zigzag pattern on a given number of rows like this
     * (you may want to display this pattern in a fixed font for better legibility) :
     *
     * P   A   H   N
     * A P L S I I G
     * Y   I   R
     * And then read line by line: "PAHNAPLSIIGYIR"
     *
     * Write the code that will take a string and make this conversion given a number of rows:
     *
     * string convert(string s, int numRows);
     */

    public static String convert(String s, int numRows) {
        if (numRows <= 1 || s.length() <= numRows) return s;

        StringBuilder sb = new StringBuilder();

        // 1. Figure out increment val:
        int increment = 2 * (numRows - 1);

        // 2. For each row calculate sequence of chars. For example ("PAYPALISHIRING", 3):
        //      0 row -> P   A   H   N
        //      1 row -> A P L S I I G
        //      2 row -> Y   I   R
        for(int i = 0; i < numRows; i++) {
            for(int j = i; j < s.length(); j += increment) {
                // Default condition:
                sb.append(s.charAt(j));

                // Specific condition. For Middle Rows:
                int middlePosInd; // middlePosInd = j + increment - (2 * i)
                if (i > 0 && i < numRows - 1 // ensure it's Middle Row
                    && (middlePosInd = j + increment - 2 * i) < s.length()) {
                    sb.append(s.charAt(middlePosInd));
                }
            }
        }

        return sb.toString();
    }



    /**
     * 28. Find the Index of the First Occurrence in a String
     *
     * Given two strings needle and haystack, return the index of the first occurrence of needle in
     * haystack, or -1 if needle is not part of haystack.
     */

    public static int strStr(String haystack, String needle) {
        int ind = -1;

        root :for (int i = 0; i <= haystack.length() - needle.length(); i++){
            if (haystack.charAt(i) == needle.charAt(0)){
                ind = i;
                for (int n = 1; n < needle.length(); n++){
                    if (haystack.charAt(n + i) != needle.charAt(n)) continue root;
                }
                return ind;
            }
        }

        return -1;
    }
}
