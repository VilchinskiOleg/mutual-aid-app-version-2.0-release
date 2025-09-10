package org.tms.common.auth.configuration.global.algorithms.let_code;

import java.util.HashSet;
import java.util.Set;

public class SlidingWindow {

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
}
