package org.tms.common.auth.configuration.global.algorithms.let_code;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.BiFunction;

public class Stack {

    private static final Map<String, BiFunction<Integer, Integer, Integer>> actionPerAlias = Map.of(
            "add", Integer::sum,
            "pow", (a, b) -> a - b,
            "mul", (a, b) -> a * b,
            "div", (a, b) -> a / b
    );

    // All calculation results cannot be negative
    public static Integer performCalculationsByStrScenario(String scenario) {

        Deque<String> work = new LinkedList<>();

        int beginIndex = 0;
        // cannot be negative after any calculations,
        // so use -1 as default state for variables :
        int a = -1, b = -1, res = -1;

        for (int i = 0; i < scenario.length(); i++) {
            // push stack :
            if ('(' == scenario.charAt(i)) {
                work.push(scenario.substring(beginIndex, i));
                beginIndex = i + 1;
            }

            // push stack or do nothing:
            if (',' == scenario.charAt(i)) {
                if (res >= 0 && beginIndex == i) { // situation like ->  .. pow(5,2), 5 ..
                    a = res;
                    b = -1;
                    res = -1;
                } else {
                    work.push(scenario.substring(beginIndex, i));
                }
                beginIndex = i + 1;
            }

            // pop stack :
            if (')' == scenario.charAt(i)) {
                if (res >= 0 && beginIndex == i) { // situation like ->  .. pow(5,2)) ..
                    a = -1;
                    b = res;
                } else {
                    work.push(scenario.substring(beginIndex, i));
                }

                b = b < 0 ? Integer.parseInt(work.pop()) : b;
                a = a < 0 ? Integer.parseInt(work.pop()) : a;
                BiFunction<Integer, Integer, Integer> action = actionPerAlias.get(work.pop());
                res = action.apply(a, b);

                beginIndex = i + 1;
            }
        }

        return res;
    }
}
