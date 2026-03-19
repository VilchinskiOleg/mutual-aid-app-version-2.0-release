package org.tms.common.auth.configuration.global.algorithms.model.sorting;

/**
 * Complexity time: O(n^2)
 */
public class InsertionSortUtil {

  // Method to perform insertion sort :
  public static void insertionSort(int[] arr) {
    int n = arr.length;

    for (int i = 1; i < n; i++) {
      int key = arr[i]; // The element to be inserted
      int j = i - 1;

      // Move elements greater than key to one position ahead :
      while (j >= 0 && arr[j] > key) {
        arr[j + 1] = arr[j];
        j = j - 1;
      }

      // Place the key at the correct position :
      arr[j + 1] = key;
    }
  }

}
