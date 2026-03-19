package org.tms.common.auth.configuration.global.algorithms.model.sorting;

/**
 * Complexity time: O(n)
 *
 * But it requires specific conditions.
 */
public class BucketSortUtil {

  public static void sort(int[] arr, int maxVal) {
    int[] bucketArr = new int[maxVal + 1]; //add '+ 1' if you take into account '0' val

    // 1. Populate bucket array :
    for (int val : arr) {
      bucketArr[val] += 1;
    }

    // 2. Generate new (sorted) array using buckets :
    int currPosition = 0;
    for (int i = 0; i < bucketArr.length; i++) {
      int valCounter = bucketArr[i];
      while (valCounter-- > 0) {
        arr[currPosition] = i;
        currPosition++;
      }
    }
  }
}
