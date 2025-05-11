package org.tms.common.auth.configuration.global.algorithms.let_code;

public class TwoPointers {

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
}
