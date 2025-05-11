package org.tms.common.auth.configuration.global.algorithms.let_code;

import java.util.ArrayList;
import java.util.List;

public class Matrix {

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
