package org.tms.common.auth.configuration.global.algorithms.model.sorting;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * log(n) - is only amount of recursive dividing operations.
 *
 * So, (entire) complexity time: O(n * log(n)), but in worst scenario
 * (if array isn't splitted equally) can become O(n^2)
 *
 * Key Idea - left part (sub array) will get values less or equal then middle val,
 * right part values that bigger than middle val.
 */
public class QuickSortUtil {

    /**
     * More preferable approach. IT doesn't create new tmp data structures just operating with indexes
     * and swapping values positions in scope of the same array.
     *
     * @param array
     * @param comp
     * @param <T>
     */
    public static <T> void sort(T[] array, Comparator<T> comp) {
        doSortInAscOrder(array, 0, array.length - 1, comp);
    }

    private static <T> void doSortInAscOrder(T[] array, int startInd, int endInd, Comparator<T> comp) {
        if (endInd - startInd > 0) {
            T pivotVal = array[endInd]; // can choose other position
            int curPosition = startInd;
            int nextSmallerValSlot = startInd;

            // Move all smaller (or equal) values to left part :
            while (curPosition <= endInd) {
                // if array[curPosition] <= pivotVal :
                if (comp.compare(array[curPosition], pivotVal) <= 0) {
                    // Swap values :
                    T tmp = array[nextSmallerValSlot];
                    array[nextSmallerValSlot] = array[curPosition];
                    array[curPosition] = tmp;

                    nextSmallerValSlot++;
                }
                curPosition++;
            }

            int middleInd = nextSmallerValSlot - 1; // because pivot val will be swapped last
            // Do next iteration (excluding pivot val) :
            doSortInAscOrder(array, startInd, middleInd - 1, comp);
            doSortInAscOrder(array, middleInd + 1, endInd, comp);
        }
    }


    /**
     * Less preferable option. It creates new DSs and it's more difficult for understanding.
     *
     * @param array
     * @return
     */
    public static List<Integer> sort(List<Integer> array) {

        if (array.size() <= 1) return array;

        boolean isEqual = false;
        for (int i = 1; i < array.size(); ++i) {
            isEqual = array.get(i - 1).equals(array.get(i));
            if (!isEqual) break;
        }

        if (isEqual) return array;

        List<Integer> leftPart;
        List<Integer> rightPart;
        for (int i = array.size() - 1; ; --i) {
            leftPart = new ArrayList<>(array);
            rightPart = new ArrayList<>(array);

            final var middleItem = array.get(i);
            leftPart.removeIf(item -> item > middleItem);
            rightPart.removeAll(leftPart);
            if (leftPart.size() != 0 && rightPart.size() != 0) break;
        }

        leftPart = sort(leftPart);
        rightPart = sort(rightPart);

        System.out.println("Left PART: " + leftPart);
        System.out.println("Right PART: " + rightPart);

        leftPart.addAll(rightPart);
        return leftPart;
    }
}