package org.tms.common.auth.configuration.global.algorithms.let_code;

import static java.util.Optional.ofNullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HashMapUtil {

  /**
   * 383. Ransom Note
   *
   * Given two strings ransomNote and magazine, return true if ransomNote can be constructed
   * by using the letters from magazine and false otherwise.
   *
   * Each letter in magazine can only be used once in ransomNote.
   */

  public static boolean canConstruct(String ransomNote, String magazine) {
    Map<Character, Integer> magazineMap = magazine.chars().mapToObj(c -> (char) c)
        .collect(Collectors.toMap(Function.identity(), c -> 1, Integer::sum));

    for (Character c : ransomNote.toCharArray()){
      Integer counter = magazineMap.get(c);

      if (counter != null && counter > 0){
        magazineMap.put(c, --counter);
      } else {
        return false;
      }
    }

    return true;
  }



  /**
   * 205. Isomorphic Strings
   *
   * Given two strings s and t, determine if they are isomorphic.
   *
   * Two strings s and t are isomorphic if the characters in s can be replaced to get t.
   *
   * All occurrences of a character must be replaced with another character while preserving
   * the order of characters. No two characters may map to the same character, but a character
   * may map to itself.
   */

  public static boolean isIsomorphic(String s, String t) {

    Map<Character, Integer> indexS = new HashMap<>();
    Map<Character, Integer> indexT = new HashMap<>();

    int len = s.length();

    if (len != t.length()) {
      return false;
    }

    for (int i = 0; i < len; i++) {
      int sVal = ofNullable( indexS.get(s.charAt(i)) ).orElse(0);
      int tVal = ofNullable( indexT.get(t.charAt(i)) ).orElse(0);

      if (sVal != tVal) {
        return false;
      }

      indexS.put(s.charAt(i), i + 1);
      indexT.put(t.charAt(i), i + 1);
    }

    return true;
  }



  /**
   * 290. Word Pattern
   *
   * Given a pattern and a string s, find if s follows the same pattern.
   *
   * Here follow means a full match, such that there is a bijection between a letter in pattern
   * and a non-empty word in s. Specifically:
   *
   * - Each letter in pattern maps to exactly one unique word in s.
   * - Each unique word in s maps to exactly one letter in pattern.
   * - No two letters map to the same word, and no two words map to the same letter.
   */

  public static boolean wordPattern(String pattern, String s) {
    String[] words = s.split(" ");
    if (words.length != pattern.length()) return false;
    Map<Character, String> mapping = new HashMap<>();

    for (int i = 0; i < pattern.length(); i++) {
      String expectedWord = mapping.get(pattern.charAt(i));

      if (expectedWord == null && !mapping.values().contains(words[i])) {
        mapping.put(pattern.charAt(i), words[i]);
      } else if (words[i].equals(expectedWord)) {
        continue;
      } else {
        return false;
      }
    }

    return true;
  }



  /**
   * 242. Valid Anagram
   *
   * Given two strings s and t, return true if t is an anagram of s, and false otherwise.
   */

  public static boolean isAnagram(String s, String t) {
    if (s.length() != t.length()) return false;

    var sChars = s.toCharArray();
    Arrays.sort(sChars);
    var tChars = t.toCharArray();
    Arrays.sort(tChars);

    return new String(sChars).equals(new String(tChars));
  }

  public static boolean isAnagram_usingMap(String s, String t) {
    if (s.length() != t.length()) return false;

    Map<Character, Integer> sCharsCounter = new HashMap<>();

    for (char ch : s.toCharArray()) {
      int currVal = sCharsCounter.computeIfAbsent(ch, k -> 0);
      sCharsCounter.put(ch, currVal + 1);
    }

    for (char ch : t.toCharArray()) {
      Integer expectedVal = sCharsCounter.get(ch);
      if (expectedVal == null || expectedVal == 0) {
        return false;
      } else {
        sCharsCounter.put(ch, expectedVal - 1);
      }
    }

    return true;
  }

  public static boolean isAnagram_usingMapWithAtomicInt(String s, String t) {
    if (s.length() != t.length()) return false;

    Map<Character, AtomicInteger> sCharsCounter = new HashMap<>();

    for (char ch : s.toCharArray()) {
      sCharsCounter.computeIfAbsent(ch, k -> new AtomicInteger(0)).incrementAndGet();
    }

    for (char ch : t.toCharArray()) {
      AtomicInteger counter = sCharsCounter.get(ch);
      if (counter == null || counter.get() == 0) {
        return false;
      } else {
        counter.decrementAndGet();
      }
    }

    return true;
  }



  /**
   * 49. Group Anagrams
   *
   * Given an array of strings strs, group the anagrams together. You can return the answer in any order.
   *
   * Example 1:
   *
   * Input: strs = ["eat","tea","tan","ate","nat","bat"]
   *
   * Output: [["bat"],["nat","tan"],["ate","eat","tea"]]
   *
   * Explanation:
   *
   * There is no string in strs that can be rearranged to form "bat".
   * The strings "nat" and "tan" are anagrams as they can be rearranged to form each other.
   * The strings "ate", "eat", and "tea" are anagrams as they can be rearranged to form each other.
   */

  public static List<List<String>> groupAnagrams(String[] strs) {
    Map<String, List<String>> groups = new HashMap<>();

    for (String str : strs) {
      var chars = str.toCharArray();
      Arrays.sort(chars);
      String key = String.valueOf(chars);

      groups
          .computeIfAbsent(key, k -> new ArrayList<>())
          .add(str);
    }

    return new ArrayList<>(groups.values());
  }

  public static List<List<String>> groupAnagrams_usingBucketSort(String[] strs) {
    Map<String, List<String>> groups = new HashMap<>();

    for (String str : strs) {
      int[] letterCounters = new int[26];  // [0,0,0,0,..]

      for (char ch : str.toCharArray()) {
        letterCounters[ch - 'a']++;        // increment counter for letter
      }

      var sb = new StringBuilder();
      for (int counter : letterCounters) {
        sb.append(counter).append("#");
      }

      groups
          .computeIfAbsent(sb.toString(), k -> new ArrayList<>())
          .add(str);
    }

    return new ArrayList<>(groups.values());
  }



  /**
   * 1. Two Sum
   *
   * Given an array of integers nums and an integer target, return indices of the two numbers such
   * that they add up to target.
   *
   * You may assume that each input would have exactly one solution, and you may not use the same element twice.
   *
   * You can return the answer in any order.
   */

  public static int[] twoSum(int[] nums, int target) {
    Map<Integer, Integer> digitIndBySurplus = new HashMap<>();

    for (int i = 0; i < nums.length; i++) {
      Integer firstMemberInd = digitIndBySurplus.get(nums[i]);
      if (firstMemberInd == null) {
        digitIndBySurplus.put(target - nums[i], i); // put "potential" firstMemberInd (as val) by "potential" (expected) secondMemberVal (as key)
      } else {
        return new int[] {firstMemberInd, i};
      }
    }

    return new int[2]; // just MOCK
  }



  /**
   * 202. Happy Number
   *
   * Write an algorithm to determine if a number n is happy.
   *
   * A happy number is a number defined by the following process:
   *
   * - Starting with any positive integer, replace the number by the sum of the squares of its digits.
   * - Repeat the process until the number equals 1 (where it will stay), or it loops endlessly in a
   *   cycle which does not include 1.
   * - Those numbers for which this process ends in 1 are happy.
   *
   * Return true if n is a happy number, and false if not.
   */

  public static boolean isHappy(int n) {
    Map<Integer, Object> numbersCache = new HashMap<>();

    while (n != 1) {
      numbersCache.put(n, null);

      int res = 0;
      do{
        int lastDigit = n % 10;
        res += Math.pow(lastDigit, 2);
        n = n / 10;
      } while (n > 0);

      if (numbersCache.containsKey(res)) return false;
      n = res;
    }

    return n == 1;
  }
}
