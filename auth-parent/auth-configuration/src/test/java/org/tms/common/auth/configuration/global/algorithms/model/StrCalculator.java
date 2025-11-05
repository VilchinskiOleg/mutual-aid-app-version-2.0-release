package org.tms.common.auth.configuration.global.algorithms.model;

import java.util.Map;
import java.util.function.BiFunction;
import org.apache.commons.lang3.tuple.Pair;

public class StrCalculator {

    private static final String EXPR_PART_IS_NULL_ERROR_MSG =
            "Wasn't able to calculate str representation for Left and Right part of Expression";

    private final Map<String, BiFunction<Integer, Integer, Integer>> actionPerAlias = Map.of(
            "add", Integer::sum,
            "sub", (a, b) -> a - b,
            "mul", (a, b) -> a * b,
            "div", (a, b) -> a / b,
            "pow", (a, b) -> (int) Math.pow(a, b)
            );

    public Integer calculate(String scenario) {
        String operationAlias = scenario.substring(0, 3);
        String expression = scenario.substring(4, scenario.length() - 1);

        Pair<String, String> members = splitExpToTwoMembers(expression);

        if (members.getLeft() == null) {
            throw new RuntimeException(EXPR_PART_IS_NULL_ERROR_MSG);
        }

        Integer left = parseStrRepresentation(members.getLeft());
        Integer right = parseStrRepresentation(members.getRight());
        return actionPerAlias.get(operationAlias).apply(left, right);
    }

    private Pair<String, String> splitExpToTwoMembers(String expression) {
        int bracketCounter = 0;
        String leftStr = null;
        String rightStr = null;

        loop: for (int i = 0; i < expression.length(); i++) {
            char cur = expression.charAt(i);
            switch (cur) {
                case '(':
                    bracketCounter++;
                    break;
                case ')':
                    bracketCounter--;
                    break;
                case ',':
                    if (bracketCounter == 0) {
                        leftStr = expression.substring(0, i);
                        rightStr = expression.substring(i + 1);
                        break loop;
                    }
            }
        }

        return Pair.of(leftStr, rightStr);
    }

    private int parseStrRepresentation(String subExpression) {
        try {
            return Integer.parseInt(subExpression);
        } catch (NumberFormatException e) {
            return calculate(subExpression); // косвенная (не прямая) рекурсия
        }
    }
}
