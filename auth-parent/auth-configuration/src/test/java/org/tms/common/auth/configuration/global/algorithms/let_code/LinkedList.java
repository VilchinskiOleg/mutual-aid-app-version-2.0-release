package org.tms.common.auth.configuration.global.algorithms.let_code;

import java.util.ArrayList;
import java.util.List;

public class LinkedList {

    /**
     * Definition for singly-linked list.
     */

    static class ListNode {
         int val;
         ListNode next;
         ListNode(int x) {
             val = x;
             next = null;
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

    public static LetCodeUtils.ListNode addTwoNumbers(LetCodeUtils.ListNode l1, LetCodeUtils.ListNode l2) {
        // Validation :
        if (l1 == null || l2 == null) {
            return l1 != null ? l1 : l2;
        }

        // Initialization :
        LetCodeUtils.ListNode resultHead = l1;
        int surplus = doCalculation(l1, l2, 0);

        // Work cycle :
        while (l1.next != null && l2.next != null) {
            l1 = l1.next; l2 = l2.next;
            surplus  = doCalculation(l1, l2, surplus);
        }

        if (l1.next == null) l1.next = l2.next;

        while (surplus > 0 && l1.next != null) {
            l1 = l1.next;
            surplus = doCalculation(l1, surplus);
        }

        if (surplus > 0) {
            l1.next = new LetCodeUtils.ListNode(surplus, null);
        }

        // Return the result :
        return resultHead;
    }

    public static int doCalculation(LetCodeUtils.ListNode l1, LetCodeUtils.ListNode l2, int surplus) {
        int calcRes = l1.val + l2.val + surplus;
        l1.val = calcRes % 10;
        return calcRes / 10;
    }

    public static int doCalculation(LetCodeUtils.ListNode l, int surplus) {
        int calcRes = l.val + surplus;
        l.val = calcRes % 10;
        return calcRes / 10;
    }
}
