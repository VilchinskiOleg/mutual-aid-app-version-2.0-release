package org.tms.common.auth.configuration.global.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.IntSupplier;
import lombok.Getter;
import org.junit.jupiter.api.Test;

public class LambdaTest {

  private class Counter {
    @Getter
    private int val;
    public void incrementVal() {++val;}
  }

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
}