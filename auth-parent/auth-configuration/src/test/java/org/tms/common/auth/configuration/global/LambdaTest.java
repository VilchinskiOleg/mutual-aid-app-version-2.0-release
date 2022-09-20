package org.tms.common.auth.configuration.global;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntSupplier;
import lombok.Getter;
import org.junit.jupiter.api.Test;

public class LambdaTest {

  @Test
  void crosscheck_if_external_variable_is_common_for_each_lambda_fun() {

    final var counter = new Counter();

    IntSupplier fun1 = () -> {
      counter.incrementVal();
      return counter.getVal();
    };
    int res1 = fun1.getAsInt();

    IntSupplier fun2 = () -> {
      counter.incrementVal();

      IntSupplier fun3 = () -> {
        counter.incrementVal();
        return counter.getVal();
      };

      fun3.getAsInt();
      return counter.getVal();
    };
    int res2 = fun2.getAsInt();

    assertEquals(1, res1);
    assertEquals(3, res2);

    assertEquals(res2, counter.getVal());
  }

  @Test
  void boubleSort() {

      int[] array = {2,33,5,11,7,23,3,45};
      System.out.println(Arrays.toString(array));

      for(int i = array.length - 1; i > 0; --i) {
        for(int n = 0; n < i; ++n) {

          if(array[n] > array[n+1]) {
            int temp = array[n+1];
            array[n+1] = array[n];
            array[n] = temp;
          }
        }
      }

      System.out.println(Arrays.toString(array));
  }

  @Test
  void fastSortTest() {

    List<Integer> array = new ArrayList<>();
    array.addAll(List.of(2,33,5,11,5,7,23,3,45,45));

    System.out.println("Before: " + array);

    fastSort(array);

    System.out.println("After: " + array);
  }

  private List<Integer> fastSort(List<Integer> array) {

    if (array.size() <= 1) return array;

    boolean isEqual = false;
    for (int i = 1; i < array.size(); ++i) {
//      isEqual = ??? array.get(i - 1) == array.get(i);
      if (isEqual) break;
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


    leftPart = fastSort(leftPart);
    rightPart = fastSort(rightPart);

    System.out.println("Left PART: " + leftPart);
    System.out.println("Right PART: " + rightPart);

    leftPart.addAll(rightPart);
    return leftPart;
  }


  @Test
  void test() {

    int [] left = {1, 5, 7, 8, 16, 21};
    int [] right = {3, 13, 14, 15, 17, 23}; // bigger

    int [] res = new int[left.length + right.length];

    for (int prevInd = 0, curInd = 0, resInd = 0, l = 0; curInd < right.length ; ++curInd) {

      int [] prevValArray = null;
      if (prevInd != curInd) {
        prevValArray  = l == 1 ? left : right;
      }

      var rVal = right[curInd];
      var lVar = left[curInd];
      int minVal;
      int newL;
      if (lVar < rVal) {
        minVal = lVar;
        newL = 0;
      } else {
        minVal = rVal;
        newL = 1;
      }

      if (prevValArray != null && prevValArray[prevInd] < minVal) {

        if (curInd - prevInd > 1) {
          var subArray = Arrays.copyOfRange(prevValArray, prevInd, curInd);
          int[] tempRes = new int[res.length];
          System.arraycopy(res, 0, tempRes, 0, curInd + 1);
          System.arraycopy(subArray, 0, tempRes, curInd + 1, subArray.length);
          res = tempRes;

          prevInd = curInd;
          resInd += subArray.length;
        } else {
          res[resInd] = prevValArray[prevInd];
          ++prevInd;
          ++resInd;
        }
      }

      res[resInd] = minVal;
      ++resInd;
      l = newL;
    }

    System.out.println("sdf");

  }



  private class Counter {

    @Getter
    private int val;

    public void incrementVal() {++val;}
  }
}