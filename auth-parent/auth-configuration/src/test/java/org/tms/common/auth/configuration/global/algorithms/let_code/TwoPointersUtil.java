package org.tms.common.auth.configuration.global.algorithms.let_code;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TwoPointersUtil {

    /**
     * 125. Valid Palindrome
     *
     * A phrase is a palindrome if, after converting all uppercase letters into lowercase letters and removing all
     * non-alphanumeric characters, it reads the same forward and backward. Alphanumeric characters include letters and numbers.
     *
     * Given a string s, return true if it is a palindrome, or false otherwise.
     */

    public static boolean isPalindrome(String s) {
        // Remove all spaces :
        // s = s.replaceAll("\\s+", "");

        // Remove all non-alphanumeric characters (including spaces) :
        s = s.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();

        for (int i = 0; i < s.length() / 2; i++) {
            if (s.charAt(i) != s.charAt(s.length() - 1 - i)) return false;
        }
        return true;
    }



    /**
     * 392. Is Subsequence
     *
     * Given two strings s and t, return true if s is a subsequence of t, or false otherwise.
     *
     * A subsequence of a string is a new string that is formed from the original string
     * by deleting some (can be none) of the characters without disturbing the relative positions of
     * the remaining characters. (i.e., "ace" is a subsequence of "abcde" while "aec" is not).
     */

    public boolean isSubsequence(String s, String t) {
        int subSeqPointer = 0;
        for (int seqPointer = 0; seqPointer < t.length() && subSeqPointer < s.length(); seqPointer++) {
            if (t.charAt(seqPointer) == s.charAt(subSeqPointer)) subSeqPointer++;
        }
        return subSeqPointer == s.length();
    }



    /**
     * 167. Two Sum II - Input Array Is Sorted
     *
     * Given a 1-indexed array of integers numbers that is already sorted in non-decreasing order,
     * find two numbers such that they add up to a specific target number. Let these two numbers be
     * numbers[index1] and numbers[index2] where 1 <= index1 < index2 <= numbers.length.
     *
     * Return the indices of the two numbers, index1 and index2, added by one as an integer array [index1, index2] of length 2.
     *
     * The tests are generated such that there is exactly one solution. You may not use the same element twice.
     *
     * Your solution must use only constant extra space.
     */

    public int[] twoSumI(int[] numbers, int target) { // works even if 'numbers' isn't sorted
        Map<Integer, Integer> firstDigitIndexByResidue = new HashMap<>();
        for (int i = 0; i < numbers.length; i++) {
            Integer firstDigitInd = firstDigitIndexByResidue.get(numbers[i]);
            if (firstDigitInd != null) {
                return new int[]{firstDigitInd, i + 1};
            } else {
                firstDigitIndexByResidue.put(target - numbers[i], i + 1);
            }
        }
        return new int[2];
    }

    public int[] twoSumII(int[] numbers, int target) {
        int leftPointer = 0;
        int rightPointer = numbers.length - 1;
        while (leftPointer < rightPointer) {
            int sum = numbers[leftPointer] + numbers[rightPointer];
            if (sum < target) {
                leftPointer++;
            } else if (sum > target) {
                rightPointer--;
            } else {
                return new int[]{leftPointer + 1, rightPointer + 1};
            }
        }
        return new int[2];
    }



    /**
     * 11. Container With Most Water
     *
     * You are given an integer array height of length n.
     * There are n vertical lines drawn such that the two endpoints of the ith line are (i, 0) and (i, height[i]).
     *
     * Find two lines that together with the x-axis form a container, such that the container contains the most water.
     *
     * Return the maximum amount of water a container can store.
     *
     * Notice that you may not slant the container.
     */

    public int maxArea(int[] height) {
        int waterSum = 0;
        int leftP = 0;
        int rightP = height.length - 1;

        while (leftP <= rightP) {
            int waterSumCandidate = Math.min(height[leftP], height[rightP]) * (rightP - leftP);
            waterSum = Math.max(waterSumCandidate, waterSum);

            if (height[leftP] <= height[rightP]) {
                leftP++;
            } else {
                rightP--;
            }
        }

        return waterSum;
    }



    /**
     * 15. 3Sum
     *
     * Given an integer array nums,
     * return all the triplets [nums[i], nums[j], nums[k]] such that i != j, i != k, and j != k,
     * and nums[i] + nums[j] + nums[k] == 0.
     *
     * Notice that the solution set must not contain duplicate triplets.
     */

    public static List<List<Integer>> threeSum(int[] nums) {

        Set<List<Integer>> res = new HashSet<>();
        Arrays.sort(nums);

        for (int p1 = 0; p1 + 2 < nums.length && nums[p1] <= 0; p1++) {
            int lp = p1 + 1;
            int rp = nums.length - 1;

            while (lp < rp) {
                int sum = nums[p1] + nums[lp] + nums[rp];

                if (sum < 0) {
                    lp++;
                } else if (sum > 0) {
                    rp--;
                } else {
                    res.add(List.of(nums[p1], nums[lp], nums[rp]));
                    lp++;
                }
            }
        }

        return new ArrayList<>(res);
    }
}
