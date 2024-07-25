package org.tms.common.auth.configuration.global.algorithms.model;

import java.util.*;
import java.util.stream.IntStream;

public class LetCodeUtils {

    /**
     * ----------------------------------- Task №1 ------------------------------------------
     */

    /**
     * Speed -> O(m + n)
     *
     * @param nums1
     * @param m
     *
     * @param nums2
     * @param n
     * @return
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
     * Speed -> O(m + n), but uses additional memory subNum1 = num1/2
     *
     * @param nums1
     * @param m
     *
     * @param nums2
     * @param n
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
     * Speed -> O(m + n), and without additional memory usage.
     * BEST OPTION !
     *
     * @param nums1
     * @param m
     *
     * @param nums2
     * @param n
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
     * ----------------------------------- Task №2 ------------------------------------------
     */

    /**
     * Speed -> O(n)
     *
     * @param nums
     * @param val
     * @return
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
     * ----------------------------------- Task №3 ------------------------------------------
     */

    /**
     * Speed -> O(n)
     *
     * @param nums
     * @return
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
     * ----------------------------------- Task №4 ------------------------------------------
     */

    /**
     * Speed -> O(n)
     *
     * @param nums
     * @return
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
     * ----------------------------------- Task №5 ------------------------------------------
     */

    /**
     * Speed -> O(n)
     *
     * @param nums
     * @return
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
     * ----------------------------------- Task №6 ------------------------------------------
     */

    /**
     * Speed -> O(n^2); Memory -> O(1)
     *
     * @param nums
     * @param k
     */
    public static void rotate_solutionI(int[] nums, int k) {

        for(int i = 0; i < k; i++){
            int last = nums[nums.length - 1];
            for (int n = nums.length - 2; n >= 0; n--){
                nums[n + 1] = nums[n];
            }
            nums[0] = last;
        }
    }

    /**
     * Speed -> O(n); Memory -> O(1)
     *
     * @param nums
     * @param k
     */
    public static void rotate_solutionII(int[] nums, int k) {

    }


    /**
     * ----------------------------------- Task №7 ------------------------------------------
     */

    public static int maxProfit(int[] prices) {

        Integer priceToBuy = null;
        int delta = 0;

        // V.1 :
        for (int i = 1; i < prices.length; i++){

            if (prices[i] > prices[i - 1]){

                int delta2 = prices[i] - prices[i - 1];
                int delta3 = priceToBuy != null ? prices[i] - priceToBuy : -1;

                delta = Math.max(delta, Math.max(delta2, delta3));

                priceToBuy = priceToBuy != null ? Math.min(prices[i - 1], priceToBuy) : prices[i - 1];
            }
        }

        // V.2 :
//        for (int i = 1; i < prices.length; i++){
//
//            if (prices[i] > prices[i - 1]){
//
//                int delta1 = delta;
//                int delta2 = prices[i] - prices[i - 1];
//                int delta3 = priceToBuy != null ? prices[i] - priceToBuy : -1;
//
//                int maxDelta = Math.max(delta1, Math.max(delta2, delta3));
//
//                if (maxDelta == delta2){
//                    priceToBuy = prices[i - 1];
//                    delta = delta2;
//                } else if (maxDelta == delta3) {
//                    delta = delta3;
//                } else {
//                    priceToBuy = Math.min(prices[i - 1], priceToBuy);
//                }
//            }
//        }

        return delta;
    }


    /**
     * ----------------------------------- Task №8 ------------------------------------------
     */


    /**
     * ----------------------------------- Task №9 ------------------------------------------
     *
     * Jump Game
     *
     * You are given an integer array nums. You are initially positioned at the array's first index,
     * and each element in the array represents your maximum jump length at that position.
     *
     * Return true if you can reach the last index, or false otherwise.
     */

    /**
     * Option 1:
     *
     * @param nums
     * @return
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
     * Option 2:
     *
     * @param nums
     * @return
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
     * Option 3:
     *
     * @param nums
     * @return
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
     * Option 4:
     *
     * @param nums
     * @return
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
     * ----------------------------------- Task №10 ------------------------------------------
     *
     * Jump Game II
     *
     * You are given a 0-indexed array of integers nums of length n. You are initially positioned at nums[0].
     *
     * Each element nums[i] represents the maximum length of a forward jump from index i. In other words,
     * if you are at nums[i], you can jump to any nums[i + j] where:
     *
     *      0 <= j <= nums[i] and
     *      i + j < n
     *
     * Return the minimum number of jumps to reach nums[n - 1]. The test cases are generated such that you can reach nums[n - 1].
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

    /**
     * Попробовать то же свмое реализовать через цикл (*)
     */


    /**
     * ----------------------------------- Task №11 ------------------------------------------
     *
     * H-Index
     *
     * Given an array of integers citations where citations[i] is the number of citations a researcher received for
     * their ith paper, return the researcher's h-index.
     *
     * According to the definition of h-index on Wikipedia: The h-index is defined as the maximum value of h such
     * that the given researcher has published at least h papers that have each been cited at least h times.
     */

    /**
     * Speed -> O(n^2); Memory -> O(1)
     *
     * @param citations
     * @return
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
     *
     * @param citations
     * @return
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
     *
     * @param citations
     * @return
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
     * ----------------------------------- Task №13 ------------------------------------------
     *
     * Product of Array Except Self
     *
     * Given an integer array nums, return an array answer such that answer[i] is equal to the product of all the elements of nums except nums[i].
     *
     * The product of any prefix or suffix of nums is guaranteed to fit in a 32-bit integer.
     *
     * You must write an algorithm that runs in O(n) time and without using the division operation.
     */

    /**
     * Speed -> O(n); Memory -> O(1)
     *
     * @param nums
     * @return
     */
    public static int[] productExceptSelf(int[] nums) {
        int[] res = new int[nums.length];
        int product = 1;
        int zeroValCounter = 0;

        for (int i = 0; i < nums.length; i++){
            if (nums[i] != 0){
                product *= nums[i];
            } else {
                zeroValCounter++;
            }
        }

        for (int i = 0; i < nums.length; i++){
            switch (zeroValCounter){
                case 0:
                    res[i] = product / nums[i];
                    break;
                case 1:
                    res[i] = nums[i] == 0 ? product : 0;
                    break;
                default:
                    res[i] = 0;
                    break;
            }
        }

        return res;
    }


    /**
    * ----------------------------------- Task №14 ------------------------------------------
    */


    /**
     * ----------------------------------- Task №15 ------------------------------------------
     *
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
     * ----------------------------------- Task №16 ------------------------------------------
     *
     * 42. Trapping Rain Water
     *
     * Given n non-negative integers representing an elevation map where the width of each bar is 1,
     * compute how much water it can trap after raining.
     */


    /**
     * First approach (works, but pretty slowly) :
     */

    public static int trapI(int[] height) {

        int water = 0;

        if (height.length < 3) return water;

        int [] waterSpreading = new int[height.length];
        int maxHeight = 0;

        for (int maxHeightCandidate : height) {
            maxHeight = Math.max(maxHeight, maxHeightCandidate);
        }

        for (int n = 0; n < height.length; n++){
            waterSpreading[n] = maxHeight - height[n];
        }

        int [] expected = new int[waterSpreading.length - 2];
        Arrays.fill(expected, 0);
        while (!Arrays.equals(waterSpreading, 1, waterSpreading.length - 1, expected, 0, expected.length)){

            int firstTopIndex = -1;
            int waterCounter = 0;

            for (int n = 0; n < height.length; n++){

                if (waterSpreading[n] == 0) {
                    if (firstTopIndex >= 0){
                        water += waterCounter;
                        waterCounter = 0;

                        if (n - firstTopIndex > 1) {
                            Arrays.fill(waterSpreading, firstTopIndex + 1, n, 0);
                        }
                    }
                    firstTopIndex = n;
                } else {
                    waterCounter += firstTopIndex >= 0 ? waterSpreading[n] : 0;
                }
            }

            for (int n = 0; n < height.length; n++){
                waterSpreading[n] = Math.max(waterSpreading[n] - 1, 0);
            }
        }

        return water;
    }

    public static int trapII(int[] height) {

        int water = 0;

        if (height.length < 3) return water;

        int [] waterSpreading = new int[height.length];
        int maxHeight = 0;

        for (int maxHeightCandidate : height) {
            maxHeight = Math.max(maxHeight, maxHeightCandidate);
        }

        for (int n = 0; n < height.length; n++){
            waterSpreading[n] = maxHeight - height[n];
        }

        int shouldStopCounter;
        do {

            shouldStopCounter = 0;
            int firstTopIndex = -1;
            int waterCounter = 0;

            for (int n = 0; n < waterSpreading.length; n++){

                if (waterSpreading[n] == 0) {
                    if (firstTopIndex >= 0){
                        water += waterCounter;
                        waterCounter = 0;

                        if (n - firstTopIndex > 1) {
                            Arrays.fill(waterSpreading, firstTopIndex + 1, n, 0);
                        }
                    }
                    firstTopIndex = n;
                    shouldStopCounter += n == 0 || n == waterSpreading.length - 1 ? 0 : 1;
                } else {
                    waterCounter += firstTopIndex >= 0 ? waterSpreading[n] : 0;
                }
            }

            for (int n = 0; n < waterSpreading.length; n++){
                waterSpreading[n] = Math.max(waterSpreading[n] - 1, 0);
            }

        } while (shouldStopCounter < waterSpreading.length - 2);

        return water;
    }


    /**
     * Second approach ( Speed -> O(n); Memory -> O(n) ) :
     */

    public static int trapIII(int[] height) {

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

    public static int trapIV(int[] height) {

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
     * ----------------------------------- Task №17 ------------------------------------------
     *
     * 13. Roman to Integer
     *
     */


    /**
     * ----------------------------------- Task №? ------------------------------------------
     *
     * 114. Longest Common Prefix
     *
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
     * ----------------------------------- Task №? ------------------------------------------
     *
     * 151. Reverse Words in a String
     *
     */

    /**
     * Speed -> O(n); Memory -> O(n)
     *
     * @param s
     * @return
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
     *
     * @param s
     * @return
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
     * ----------------------------------- Task №? ------------------------------------------
     *
     * 28. Find the Index of the First Occurrence in a String
     *
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


    /**
     * ----------------------------------- Task №? ------------------------------------------
     *
     * 151. Reverse Words in a String
     *
     * Given an array of strings words and a width maxWidth, format the text such that each line has exactly maxWidth
     * characters and is fully (left and right) justified.
     *
     * You should pack your words in a greedy approach; that is, pack as many words as you can in each line.
     * Pad extra spaces ' ' when necessary so that each line has exactly maxWidth characters.
     *
     * Extra spaces between words should be distributed as evenly as possible. If the number of spaces on a line doesn't
     * divide evenly between words, the empty slots on the left will be assigned more spaces than the slots on the right.
     *
     * For the last line of text, it should be left-justified, and no extra space is inserted between words.
     */

    public static List<String> fullJustify(String[] words, int maxWidth) {
        List<String> ans = new ArrayList<>();
        int i = 0;
        while (i < words.length) {
            StringBuilder sb = new StringBuilder();
            while (i < words.length && sb.length() + words[i].length() <= maxWidth) {
                sb.append(words[i]).append(" ");
                i++;
            }
            String str = sb.toString().trim();
            if (i < words.length)
                ans.add(addSpaces(str, maxWidth));
            else {
                while (str.length() != maxWidth)
                    str += " ";
                ans.add(str);
            }
        }
        return ans;
    }

    private static String addSpaces(String s, int width) {
        StringBuilder sb = new StringBuilder();
        String[] arr = s.split(" ");
        int count = arr.length - 1;

        if (count == 0) {
            while (s.length() != width)
                s += " ";
            return s;
        }

        int padding = width - s.length();
        int equalSpace = padding / count;
        int moreNeeded = padding % count;
        String spaces = " ";

        while (equalSpace-- > 0)
            spaces += " ";

        String sp = spaces + " ";
        for (String ele : arr) {
            if (moreNeeded-- > 0)
                sb.append(ele + sp);
            else
                sb.append(ele + spaces);
        }
        return sb.toString().trim();
    }

}
