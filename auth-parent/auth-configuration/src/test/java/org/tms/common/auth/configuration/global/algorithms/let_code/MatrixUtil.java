package org.tms.common.auth.configuration.global.algorithms.let_code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatrixUtil {

    /**
     * 36. Valid Sudoku
     *
     * Determine if a 9 x 9 Sudoku board is valid. Only the filled cells need to be validated
     * according to the following rules:
     *
     * - Each row must contain the digits 1-9 without repetition.
     * - Each column must contain the digits 1-9 without repetition.
     * - Each of the nine 3 x 3 sub-boxes of the grid must contain the digits 1-9 without repetition.
     *
     * Note:
     *
     * - A Sudoku board (partially filled) could be valid but is not necessarily solvable.
     * - Only the filled cells need to be validated according to the mentioned rules.
     */

    public static boolean isValidSudoku(char[][] board) {
        int [][] rowCache = new int [board.length][10];    // int[10] per each row index
        int [][] columnCache = new int [board.length][10]; // int[10] per each column index
        Map<String, int[]> squareCache = new HashMap<>();  // int[10] per each square token (String)

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                char value = board[i][j];

                if (Character.isDigit(value)) { // OR: if(ch >= '0' && ch <= '9') {..}
                    int number = Character.getNumericValue(value);
                    // OR: int number = value - '0';

                    // 1. Check and update Row Cache:
                    if (rowCache[i][number] > 0) return false;
                    rowCache[i][number]++;

                    // 2. Check and update Column Cache:
                    if (columnCache[j][number] > 0) return false;
                    columnCache[j][number]++;

                    // 3. Check and update Square Cache:
                    int[] squareValues = squareCache.computeIfAbsent(
                        String.valueOf(i / 3) + j / 3,
                        k -> new int[10]);
                    if (squareValues[number] > 0) return false;
                    squareValues[number]++;
                }
            }

        }

        return true;
    }



    /**
     * 54. Spiral Matrix
     *
     * Given an m x n matrix, return all elements of the matrix in spiral order.
     *
     * @param matrix
     * @return
     */
    public static List<Integer> spiralOrder(int[][] matrix) {
        if (matrix == null || matrix.length == 0) return List.of();

        List<Integer> result = new ArrayList<>(matrix.length * matrix[0].length);
        int top = 0, down = matrix.length - 1, left = 0, right = matrix[0].length - 1;

        int direction = 0;

        while ( top <= down && left <= right){
            switch (direction) {
                case 0:
                    for (int i = left; i <= right; i++) {
                        result.add(matrix [top] [i]);
                    }
                    direction = 1;
                    top++;
                    break;
                case 1:
                    for (int i = top; i <= down; i++) {
                        result.add(matrix [i] [right]);
                    }
                    direction = 2;
                    right--;
                    break;
                case 2:
                    for (int i = right; i >= left; i--) {
                        result.add(matrix [down] [i]);
                    }
                    direction = 3;
                    down--;
                    break;
                case 3:
                    for (int i = down; i >= top; i--) {
                        result.add(matrix [i] [left]);
                    }
                    direction = 0;
                    left++;
                    break;
            }
        }

        return result;
    }

}
