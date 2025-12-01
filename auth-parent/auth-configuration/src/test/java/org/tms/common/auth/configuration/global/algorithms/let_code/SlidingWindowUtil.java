package org.tms.common.auth.configuration.global.algorithms.let_code;

import java.util.HashSet;
import java.util.Set;

public class SlidingWindowUtil {

  /**
   * 3. Longest Substring Without Repeating Characters
   *
   * Given a string s, find the length of the longest substring without duplicate characters.
   */

  public int lengthOfLongestSubstring(String s) {
    Set<Character> subString = new HashSet<>();

    // Initialization:
    int leftBorder = 0;
    int rightBorder = 0;
    int maxSubStringVal = 0;

    while(rightBorder < s.length()) {
      char charToCheck = s.charAt(rightBorder);

      if (subString.contains(charToCheck)) {

        // move left border + remove char:
        maxSubStringVal = Math.max(maxSubStringVal, rightBorder - leftBorder); // update biggest length
        while (s.charAt(leftBorder) != charToCheck) {
          subString.remove(s.charAt(leftBorder));
          leftBorder++;
        }
        subString.remove(s.charAt(leftBorder));
        leftBorder++;

      } else {

        // move right border + populate char:
        subString.add(s.charAt(rightBorder));
        rightBorder++;
      }
    }

    return Math.max(maxSubStringVal, rightBorder - leftBorder);
  }



  /**
   * 209. Minimum Size Subarray Sum
   *
   * Given an array of positive integers nums and a positive integer target, return the minimal
   * length of a subarray whose sum is greater than or equal to target. If there is no such subarray,
   * return 0 instead.
   */

  public int minSubArrayLen(int target, int[] nums) {
    int leftBorder = 0;
    int rightBorder = 0;
    int subArraySum = 0;
    int minSubArrayLength = Integer.MAX_VALUE;

    while(rightBorder < nums.length || subArraySum >= target) {
      if (subArraySum < target) {
        subArraySum += nums[rightBorder];
        rightBorder++;
      } else {
        do {
          minSubArrayLength = Math.min(minSubArrayLength, rightBorder - leftBorder);
          subArraySum -= nums[leftBorder];
          leftBorder++;
        } while (subArraySum >= target);
      }
    }

    return minSubArrayLength <= nums.length ? minSubArrayLength : 0;
  }
}
