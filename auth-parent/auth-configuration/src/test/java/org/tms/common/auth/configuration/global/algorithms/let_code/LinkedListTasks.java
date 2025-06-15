package org.tms.common.auth.configuration.global.algorithms.let_code;

import java.util.ArrayList;
import java.util.List;

public class LinkedListTasks {

    /**
     * Definition for singly-linked list.
     */

    public static class ListNode {
        int val;
        ListNode next;
        public ListNode(int val) {
            this.val = val;
            this.next = null;
        }
        public ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }



    /**
     * 141. Linked List Cycle
     *
     * Given head, the head of a linked list, determine if the linked list has a cycle in it.
     *
     * There is a cycle in a linked list if there is some node in the list that can be reached again by continuously following the next pointer.
     * Internally, pos is used to denote the index of the node that tail's next pointer is connected to. Note that pos is not passed as a parameter.
     *
     * Return true if there is a cycle in the linked list. Otherwise, return false.
     */

    public static boolean hasCycle(ListNode head) {
        // WRONG solution :

        List<Integer> visited = new ArrayList<>();
        while(head != null) {
            if (visited.contains(head.val)) return true;
            visited.add(head.val);
            head = head.next;
        }
        return false;
    }



    /**
     * 2. Add Two Numbers
     *
     * You are given two non-empty linked lists representing two non-negative integers.
     * The digits are stored in reverse order, and each of their nodes contains a single digit.
     * Add the two numbers and return the sum as a linked list.
     *
     * You may assume the two numbers do not contain any leading zero, except the number 0 itself.
     */

    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        // Validate if both numbers aren't empty :
        if (l1 == null || l2 == null) {
            return l1 != null ? l1 : l2;
        }

        ListNode resHead = l1;

        // 1.Sums common part. Accumulates results into L1 :
        int surplus = doCalculation(l1, l2, 0);
        while (l1.next != null && l2.next != null) {
            l1 = l1.next; l2 = l2.next;
            surplus  = doCalculation(l1, l2, surplus);
        }

        // 2.Concat tail to result (L1), if necessary (L1 was shorter) :
        if (l1.next == null) l1.next = l2.next;

        // 3.Adjust tail with surplus :
        while (surplus > 0) {
            if (l1.next != null) {
                l1 = l1.next;
                surplus = doCalculation(l1, surplus);
            } else {
                l1.next = new ListNode(surplus);
                break;
            }
        }

        return resHead;
    }

    private static int doCalculation(ListNode l1, ListNode l2, int surplus) {
        int calcRes = l1.val + l2.val + surplus;
        l1.val = calcRes % 10;
        return calcRes / 10;
    }

    private static int doCalculation(ListNode l, int surplus) {
        int calcRes = l.val + surplus;
        l.val = calcRes % 10;
        return calcRes / 10;
    }
}
