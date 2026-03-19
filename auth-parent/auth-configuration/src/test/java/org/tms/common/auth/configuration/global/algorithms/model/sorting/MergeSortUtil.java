package org.tms.common.auth.configuration.global.algorithms.model.sorting;

/**
 * Steps needs to be done :
 * 1. Split array to smaller portions (sub-arrays) until array has size = 1 (basic case) recursively
 * 2. Going back (after basic case), merge two sorted arrays into single one and return it as result
 *
 * log(n) - is only amount of recursive dividing operations.
 * So, (entire) complexity time: O(n * log(n))
 *
 * In fact - Merge Sort is the most likely used sort algorithm by default
 */
public class MergeSortUtil {

  public static void sort(int[] arr) {
    doSort(arr, 0, arr.length - 1);
  }

  private static void doSort(int[] arr, int start, int end) {
    if (end - start + 1 > 1) {
      // 1. Split :
      int m = (start + end)/2;
      // 2. Sort each :
      doSort(arr, start, m); // include 'm'
      doSort(arr, m + 1, end);
      // 3. Merge :
      doMerge(arr, start, m, end);
    }
  }

  /**
   *
   * @param arr - input array
   * @param s - start position index
   * @param m - middle position index
   * @param e - end position index
   * @return modified input array
   */
  private static int[] doMerge(int[] arr, int s, int m, int e) {
    int [] rightCopy = new int[e - m];
    System.arraycopy(arr, m + 1, rightCopy, 0, e - m);

    for (int lCount = m - s, rCount = e - (m + 1); lCount >= 0 || rCount >= 0;) {
      int lPosInd = s + lCount;
      int rPosInd = rCount; // Because each time we take into account entire new tmp arr, not part
      int index = lPosInd + rPosInd + 1;

      if (rCount < 0 || arr[lPosInd] > rightCopy[rPosInd]) { // Case: lCount < 0 -> impossible here
        arr[index] = arr[lPosInd];
        lCount--;
      } else {
        arr[index] = rightCopy[rPosInd];
        rCount--;
      }
    }

    return arr;
  }
}
