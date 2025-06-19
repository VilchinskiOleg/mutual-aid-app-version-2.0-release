package org.tms.common.auth.configuration.global.algorithms.let_code;

import java.util.*;

public class LinkedListTasks {

    /**
     * Definition for singly-linked list Node :
     */

    public static class ListNode {
        int val;
        ListNode next;

        public ListNode() {}
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
     * Definition for a Node :
     */
    public static class Node {
        int val;
        public Node next;
        public Node random;

        public Node(int val) {
            this.val = val;
            this.next = null;
            this.random = null;
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
        // WRONG solution :........
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



    /**
     * 21. Merge Two Sorted Lists
     *
     * You are given the heads of two sorted linked lists list1 and list2.
     *
     * Merge the two lists into one sorted list. The list should be made by splicing together the nodes of the first two lists.
     *
     * Return the head of the merged linked list.
     */

    public static ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        // Validation :
        if (list1 == null || list2 == null) {
            return list1 != null ? list1 : list2;
        }

        // Initialization (first node will be just a mock) :
        ListNode resultHead = new ListNode();
        ListNode resultCurrent = resultHead;

        // 1.Work cycle :
        while (list1 != null && list2 != null) {
            if (list1.val <= list2.val) {
                resultCurrent.next = list1;
                list1 = list1.next;
            } else {
                resultCurrent.next = list2;
                list2 = list2.next;
            }
            resultCurrent = resultCurrent.next;
        }

        // 2.Add tail to result :
        resultCurrent.next = list1 != null ? list1 : list2;

        return resultHead.next;
    }



    /**
     * 138. Copy List with Random Pointer
     */

    /**
     * [1.] ... Works only if all values are unique ...:
     */
    public static Node copyRandomList_partlySolution(Node head) {
        Node replicaHead = new Node(0); // mock-node
        Node replica = replicaHead;
        // key to associate node position inside the list + node itself :
        Map<Integer, Node> futureNodes = new HashMap<>();
        Map<Integer, Node> visitedNodes = new HashMap<>();

        while (head != null) {
            replica.next = futureNodes.containsKey(head.val)
                    ? futureNodes.remove(head.val) : new Node(head.val);
            replica = replica.next;

            if (head.random != null) {
                if (head.val == head.random.val) {
                    replica.random = replica;
                } else if (visitedNodes.containsKey(head.random.val)) {
                    replica.random = visitedNodes.get(head.random.val);
                } else {
                    var random = new Node(head.random.val);
                    replica.random = random;
                    futureNodes.put(random.val, random);
                }
            }

            head = head.next;
            visitedNodes.put(replica.val, replica);
        }

        return replicaHead.next != null ? replicaHead.next : null;
    }

    /**
     * [2.] ... The best solution ...:
     */
    public static Node copyRandomList(Node head) {
        if (head == null) return null;

        // 1. Create replica nodes and incorporate them into existed list (A -> A' -> B -> B') :
        var nodeCur = head;
        while (nodeCur != null) {
            // Create deap copy :
            var nodeReplica = new Node(nodeCur.val);
            nodeReplica.next = nodeCur.next;
            nodeReplica.random = nodeCur.random;
            // Do like so : A -> A' :
            nodeCur.next = nodeReplica;

            nodeCur = nodeReplica.next;
        }

        // 2. Shifts random node reference from original node to replica :
        nodeCur = head.next; // starts from replica head
        while (nodeCur != null) {
            if (nodeCur.random != null) nodeCur.random = nodeCur.random.next;
            nodeCur = nodeCur.next != null ? nodeCur.next.next : null;
        }

        // 3. Transform   A -> A' -> B -> B'   to   A' -> B'  and  A -> B :
        Node headReplica = null;
        Node nodeReplica = null;
        nodeCur = head;
        while (nodeCur != null) {
            if (nodeReplica != null) nodeReplica.next = nodeCur.next;
            nodeReplica = nodeCur.next;
            if (headReplica == null) headReplica = nodeReplica;

            nodeCur.next = nodeReplica.next;
            nodeCur = nodeCur.next;
        }

        return headReplica;
    }



    /**
     * 92. Reverse Linked List II
     */

    public static ListNode reverseBetween(ListNode head, int left, int right) {

    }






    /**
     * 19. Remove Nth Node From End of List
     */

    public static ListNode removeNthFromEnd(ListNode head, int n) {
        if (n == 0) return head;
        if (head.next == null) return null;

        ListNode pointer = head;
        ListNode pointerWithDelay = head;
        while (pointer.next != null) {
            pointer = pointer.next;
            if (n == 0) {
                pointerWithDelay = pointerWithDelay.next;
            } else --n;
        }

        if (n == 0) {
            pointerWithDelay.next = pointerWithDelay.next.next;
            return head;
        } else { // n = 1
            return head.next;
        }
    }



    /**
     * 82. Remove Duplicates from Sorted List II
     */

    public static ListNode deleteDuplicates(ListNode head) {
        ListNode node = head;
        boolean isTracking = false;
        ListNode processedPart = null;

        while (node != null && node.next != null) {
            if (node.val == node.next.val) {
                if (!isTracking) isTracking = true;
            } else if (isTracking) {
                if (processedPart != null) {
                    processedPart.next = node.next;
                } else {
                    head = node.next;
                    processedPart = null;
                }
                isTracking = false;
            } else {
                processedPart = node;
            }
            node = node.next;
        }

        if (isTracking) {
            if (processedPart != null) {
                processedPart.next = null;
            } else {
                head = null;
            }
        }

        return head;
    }
}
