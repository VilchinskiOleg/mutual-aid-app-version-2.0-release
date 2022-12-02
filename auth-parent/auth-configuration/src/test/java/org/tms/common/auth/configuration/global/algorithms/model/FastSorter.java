package org.tms.common.auth.configuration.global.algorithms.model;

import java.util.ArrayList;
import java.util.List;

public class FastSorter {

    public List<Integer> sort(List<Integer> array) {

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