package org.tms.common.auth.configuration.global.core;

import java.util.*;

public class GetNonRepeatableSubSequenceMain {


    public static void main(String[] args) {

    }

    public static String detectWordWithMaxRepLetter(String line) {
        String[] words = line.split(" ");

        int maxRepAmount = 0;
        String repWord = null;

        for (String word : words) {

            char[] chars = word.toCharArray();
            Arrays.sort(chars);
            int counter = 0;

            for(int i = 0; i < chars.length - 1; i++) {

                if (chars[i] == chars[i+1]) {
                    counter++;
                } else {
                    if (counter > maxRepAmount) {
                        maxRepAmount = counter;
                        repWord = word;
                    }
                    counter = 0;
                }
            }

            if (counter > maxRepAmount) {
                maxRepAmount = counter;
                repWord = word;
            }
        }

        return repWord == null ? "-1" : repWord;
    }



    /**
     * Task for LitCode:
     * @param args - ...
     */
    public static void mainB(String[] args) {

//        var m = Pattern.compile("[a-z]{0,2}").matcher("abcabcbb");
//
//        System.out.println(m.matches());
//
//        while (m.find()) {
//            System.out.println("sub sequence is: " + m.group());
//            System.out.println("indexes of sub sequence are: " + m.start() + " and " + m.end());
//        }

        String ex1 = "abcabcbb";
        String ex2 = "dvfmda";
        System.out.println(getLengthOfLongestSubSequence1(ex2));
    }

    private static int getLengthOfLongestSubSequence0(String s) {

        Set<Integer> cash = new HashSet();
        int maxLength = 0;
        int currentLength = 0;

        for(char ch : s.toCharArray()) {
            //1th case: if left part bigger:
            if(cash.contains((int) ch)) {
                maxLength = Math.max(maxLength, currentLength);
                cash.clear();
                currentLength = 1;
                cash.add((int) ch);
            } else {
                currentLength++;
                cash.add((int) ch);
            }
        }

        return Math.max(maxLength, currentLength);
    }

    // "d_vdf_{d, end}"
    private static int getLengthOfLongestSubSequence1(String s) {

        char[] chars = s.toCharArray();
        Map<Integer, Integer> cash = new HashMap<>();
        char leftBorder = 0;
        int leftBorderNextChIndex = 0;
        int maxLength = 0;

        for(int i = 0; i < chars.length; i++) {
            //2th case: if right part (between a...a...a) bigger:
            Integer tempIndex;
            if((tempIndex = cash.get((int) chars[i])) != null) {
                if (leftBorder == chars[i]) {
                    maxLength = Math.max(maxLength, i - leftBorderNextChIndex);
                    //refresh left border data for current value:
                    cash.put((int) leftBorder, i);
                    leftBorderNextChIndex = i;
                } else {
                    //change left border data:
                    leftBorder = chars[i];
                    leftBorderNextChIndex = tempIndex + 1;
                }
            } else {
                cash.put((int) chars[i], i);
            }
        }

        return Math.max(maxLength, chars.length - leftBorderNextChIndex);
    }

    // final solution:
//    private static int getLengthOfLongestSubSequence2(String s) {
//
//    }



    /**
     * Evolution of Live Game :
     */
    public static void fun() {
        var state = initArea(5);
        showState(state);

        int amountOfLifeIteration = 5;

        while (amountOfLifeIteration-- > 0) {
            state = runOneLiveCycle(state);
            showState(state);
        }
    }

    public static void showState(int[][] currentState) {
        for (int i = 0; i < currentState.length; i++) {
            for (int n = 0; n < currentState[0].length; n++) {
                System.out.printf("%s  ", currentState[i][n]);
            }
            System.out.print("\n");
        }
        System.out.print("\n\n");
    }

    public static int[][] initArea(int size) {
        int[][] state = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int n = 0; n < size; n++) {
                state[i][n] = (Math.random() * 10 > 7) ? 1 : 0;
            }
        }

        return state;
    }

    //Если это пустая клетка и соседей ровно 3, то эта клетка оживает. Во всех остальных случаях пустая клетка остается пустой.
    //Если же это живая клетка, то подсчитывается количество живых соседей.
    //Если соседей 0 или 1, то клетка умирает от одиночества.
    //Если соседей 2 или 3, то клетка продолжает жить.
    //Если соседей 4 или больше, то клетка умирает от перенаселения.
    public static int[][] runOneLiveCycle(int[][] currentState) {
        int[][] newState = new int [currentState.length][currentState[0].length];

        for (int i = 0; i < currentState.length; i++) {
            for (int n = 0; n < currentState[0].length; n++) {
                // for every step just check state of

                int neighbourSum =
                        ((i > 0 && n > 0) ? currentState[i - 1][n - 1] : 0) +
                                ((i > 0) ? currentState[i - 1][n] : 0) +
                                ((i > 0 && n != currentState[0].length - 1) ? currentState[i - 1][n + 1] : 0) +
                                ((n != currentState[0].length - 1) ? currentState[i][n + 1] : 0) +
                                ((i != currentState.length - 1 && n != currentState[0].length - 1) ? currentState[i + 1][n + 1] : 0) +
                                ((i != currentState[0].length - 1) ? currentState[i + 1][n] : 0) +
                                ((i != currentState[0].length - 1 && n > 0) ? currentState[i + 1][n - 1] : 0) +
                                ((n > 0) ? currentState[i][n - 1] : 0);

                if (currentState[i][n] > 0) {
                    newState[i][n] = (neighbourSum == 2 || neighbourSum == 3) ? 1 : 0;
                } else {
                    newState[i][n] = neighbourSum == 3 ? 1 : 0;
                }
            }
        }

        return newState;
    }
}