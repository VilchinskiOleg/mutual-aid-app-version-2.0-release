package org.tms.common.auth.configuration.global.algorithms.let_code;

import static java.util.Optional.ofNullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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
}
