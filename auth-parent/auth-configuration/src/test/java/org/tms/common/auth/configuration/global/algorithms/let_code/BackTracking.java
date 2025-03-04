package org.tms.common.auth.configuration.global.algorithms.let_code;

import java.util.*;
import java.util.stream.Collectors;

public class BackTracking {


    /**
     * 17. Letter Combinations of a Phone Number
     *
     * Given a string containing digits from 2-9 inclusive, return all possible letter combinations that the number could represent. Return the answer in any order.
     *
     * A mapping of digits to letters (just like on the telephone buttons) is given below. Note that 1 does not map to any letters.
     */

    private static Map<Character, char[]> map = new HashMap<>();

    static {
        map.put('2', new char[] {'a', 'b', 'c'});
        map.put('3', new char[] {'d', 'e', 'f'});
        map.put('4', new char[] {'g', 'h', 'i'});
        map.put('5', new char[] {'j', 'k', 'l'});
        map.put('6', new char[] {'m', 'n', 'o'});
        map.put('7', new char[] {'p', 'q', 'r', 's'});
        map.put('8', new char[] {'t', 'u', 'v'});
        map.put('9', new char[] {'w', 'x', 'y', 'z'});
    }

    public static List<String> letterCombinations(String digits) {
        if (digits == null || digits.length() == 0) {
            return Collections.emptyList();
        }

        Deque<Deque<Character>> results = new LinkedList<>();
        int i = digits.length() - 1;

        while (results.isEmpty()) {
            var options = map.get(digits.charAt(i));
            if (options != null) {
                for (char ch : options) {
                    Deque<Character> d = new LinkedList<>();
                    d.add(ch);
                    results.add(d);
                }
            }
            i -= 1;
        }

        for (; i >= 0; i--) {
            int iterationSize = results.size();
            var options = map.get(digits.charAt(i));
            while (iterationSize-- > 0) {
                var item = results.pop();
                for (int n = 0; n < options.length - 1; n++) {
                    var subSequence = new LinkedList<>(item);
                    subSequence.addFirst(options[n]);
                    results.add(subSequence);
                }
                item.addFirst(options[options.length -1]);
                results.add(item);
            }
        }

        return results.stream()
                .map(charSequence -> {
                    var b = new StringBuilder();
                    for (char ch : charSequence) {
                        b.append(ch);
                    }
                    return b.toString();
                })
                .collect(Collectors.toList());
    }


    /**
     * 77. Combinations
     */

    public static List<List<Integer>> combine(int n, int k) {
        List<List<Integer>> results = new LinkedList<>();
        int restCounter = k - 1;

        for (int i = 1; i <= n - k + 1; i++) {
            List<Integer> headOption = new LinkedList<>();
            headOption.add(i);
            List<List<Integer>> combinations = combine(headOption, i + 1, n, restCounter);
            if (combinations.isEmpty()) break;
            results.addAll(combinations);
        }

        return results;
    }

    private static List<List<Integer>> combine(List<Integer> head,
                                               int nextMember, int lastMember,
                                               int restCounter) {
        if (restCounter == 0) {
            return List.of(head);
        }

        List<List<Integer>> results = new LinkedList<>();
        restCounter -= 1;

        while (nextMember <= lastMember) {
            List<Integer> headOption = new LinkedList<>(head);
            headOption.add(nextMember);
            List<List<Integer>> combinations = combine(headOption, ++nextMember, lastMember, restCounter);
            if (combinations.isEmpty()) break;
            results.addAll(combinations);
        }

        return results;
    }


    /**
     * 79. Word Search
     *
     * Given an m x n grid of characters board and a string word, return true if word exists in the grid.
     *
     * The word can be constructed from letters of sequentially adjacent cells, where adjacent cells are horizontally or vertically neighboring.
     * The same letter cell may not be used more than once.
     */

    public static class CandidateInfo {
        private Map.Entry<Integer, Integer> curPosition;
        private Map.Entry<Integer, Integer> prevPosition;
        private StringBuilder prefix;

        public CandidateInfo(Map.Entry<Integer, Integer> curPosition, Map.Entry<Integer, Integer> prevPosition,
                             StringBuilder prefix) {
            this.curPosition = curPosition;
            this.prevPosition = prevPosition;
            this.prefix = prefix;
        }

        public Map.Entry<Integer, Integer> getCurPosition() {
            return curPosition;
        }

        public Map.Entry<Integer, Integer> getPrevPosition() {
            return prevPosition;
        }

        public StringBuilder getPrefix() {
            return prefix;
        }
    }

    private static String [][] board;
    private static String word;

    public static boolean exist(String[][] board, String word) {
        if (!(board.length > 0)) return false;

        BackTracking.board = board;
        BackTracking.word = word;

        Deque<CandidateInfo> stack = new LinkedList<>();
        Map<Map.Entry<Integer, Integer>, Integer> visitedCount =
                new HashMap<>(board.length * board[0].length, 1.1f);

        for (int r = board.length - 1; r >= 0; r--) {
            for (int c = board[0].length - 1; c >= 0; c--) {
                // validation
                stack.push(new CandidateInfo(Map.entry(r,c), null, new StringBuilder()));
            }
        }

//        while (!stack.isEmpty()) {
//            var candidate = stack.pop();
//            var curPosition = candidate.getCurPosition();
//            var prefix = candidate.getPrefix();
//
//            int row = curPosition.getKey();
//            int col = curPosition.getValue();
//
//            prefix.append(board[row][col]);
//
//            if (word.contentEquals(prefix)) {
//                return true;
//            } else if (word.startsWith(prefix.toString())) {
//
//                boolean visited = false;
//
//                if (col + 1 < board[0].length
//                        && !visited.contains(Map.entry(row, col + 1))) {
//                    options.add(Map.entry(row, col + 1));
//                    // if so visited == true
//                }
//
//                if (row + 1 < board.length
//                        && !visited.contains(Map.entry(row + 1, col))) {
//                    options.add(Map.entry(row + 1, col));
//                }
//
//                if (col - 1 >= 0
//                        && !visited.contains(Map.entry(row, col - 1))) {
//                    options.add(Map.entry(row, col - 1));
//                }
//
//                if (row - 1 >= 0
//                        && !visited.contains(Map.entry(row - 1, col))) {
//                    options.add(Map.entry(row - 1, col));
//                }
//
//                if (visited) {
//                    visitedCount.computeIfAbsent()
//                } //visited.add(curPosition);
//            } else {
//                var prevPosition = candidate.getPrevPosition();
//                if (prevPosition != null && visitedCount.get(prevPosition) - 1 == 0) {
//                    visitedCount.remove(prevPosition);
//                }
//            }
//        }

        return false;
    }


    private static boolean exist(int row, int col, String prefix,
                                 List<Map.Entry<Integer, Integer>> visited) {

        final String updatedPrefix = prefix + board[row][col];

        if (word.equals(updatedPrefix)) return true;

        if (word.startsWith(updatedPrefix)) {
            visited.add(Map.entry(row, col));

            if (col + 1 < board[0].length
                    && !visited.contains(Map.entry(row, col + 1))
                    && exist(row, col + 1, updatedPrefix, visited)) {
                return true;
            }

            if (row + 1 < board.length
                    && !visited.contains(Map.entry(row + 1, col))
                    && exist(row + 1, col, updatedPrefix, visited)) {
                return true;
            }

            if (col - 1 >= 0
                    && !visited.contains(Map.entry(row, col - 1))
                    && exist(row, col - 1, updatedPrefix, visited)) {
                return true;
            }

            if (row - 1 >= 0
                    && !visited.contains(Map.entry(row - 1, col))
                    && exist(row - 1, col, updatedPrefix, visited)) {
                return true;
            }

            visited.remove(Map.entry(row, col));
        }

        return false;
    }
}
