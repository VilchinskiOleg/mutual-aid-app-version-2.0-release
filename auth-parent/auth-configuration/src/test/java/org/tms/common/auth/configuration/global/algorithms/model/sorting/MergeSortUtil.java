package org.tms.common.auth.configuration.global.algorithms.model.sorting;

/**
 * Steps needs to be done :
 * 1. Split array to smaller portions (sub-arrays) until array has size = 1 (basic case) recursively
 * 2. Going back (after basic case), merge two sorted arrays into single one and return it as result
 *
 * log(n) - is only amount of recursive dividing operations.
 * So, (entire) complexity time: O(n * log(n))
 */
public class MergeSortUtil {

  public static int[] doSort(int[] arr, int start, int end) {
    if (end - start + 1 > 1) {
      int m = (start + end)/2;
      doSort(arr, start, m); // ?
      doSort(arr, m, end); // ?
      return merge(arr, start, m, end);
    } else {
      return arr;
    }
  }

  /**
   * Just need to adapt a bit method
   * {@link org.tms.common.auth.configuration.global.algorithms.let_code.Array#mergeSortedArrays_IntoFirstOne_NoExtraMemoryUsage(int[], int, int[], int)}
   * here :
   */
  private static int[] merge(int[] arr, int start, int m, int end) {
    // ..
    return null;
  }
}
