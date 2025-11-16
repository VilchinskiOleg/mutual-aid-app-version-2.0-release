package org.tms.common.auth.configuration.global.algorithms.let_code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;

public class LinkedList {

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
    public static class ReferencedListNode {
        int val;
        public ReferencedListNode next;
        public ReferencedListNode random;

        public ReferencedListNode(int val) {
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
     *
     *
     *
     * Solution Approach :
     * 1 . Start both pointers at the head of the list.
     *
     * 2 . Move slow by 1 step and fast by 2 steps.
     *
     * 3 . If fast meets slow, a cycle exists.
     *
     * 4 . If fast reaches the end (null), no cycle exists.
     *
     * 🐢 moves 1 step, 🐇 moves 2 steps. If they meet, there's a cycle! 🎯
     */

    public static boolean hasCycle(ListNode head) {
        ListNode slowPointer = head;
        ListNode fastPointer = head;
        while(fastPointer != null && fastPointer.next != null) {
            slowPointer = slowPointer.next;
            fastPointer = fastPointer.next.next;
            if (slowPointer == fastPointer) return true;
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
    public static ReferencedListNode copyRandomList_partlySolution(ReferencedListNode head) {
        ReferencedListNode replicaHead = new ReferencedListNode(0); // mock-node
        ReferencedListNode replica = replicaHead;
        // key to associate node position inside the list + node itself :
        Map<Integer, ReferencedListNode> futureNodes = new HashMap<>();
        Map<Integer, ReferencedListNode> visitedNodes = new HashMap<>();

        while (head != null) {
            replica.next = futureNodes.containsKey(head.val)
                    ? futureNodes.remove(head.val) : new ReferencedListNode(head.val);
            replica = replica.next;

            if (head.random != null) {
                if (head.val == head.random.val) {
                    replica.random = replica;
                } else if (visitedNodes.containsKey(head.random.val)) {
                    replica.random = visitedNodes.get(head.random.val);
                } else {
                    var random = new ReferencedListNode(head.random.val);
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
    public static ReferencedListNode copyRandomList(ReferencedListNode head) {
        if (head == null) return null;

        // 1. Create replica nodes and incorporate them into existed list (A -> A' -> B -> B') :
        var nodeCur = head;
        while (nodeCur != null) {
            // Create deap copy :
            var nodeReplica = new ReferencedListNode(nodeCur.val);
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
        ReferencedListNode headReplica = null;
        ReferencedListNode nodeReplica = null;
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
     *
     * Given the head of a singly linked list and two integers left and right where left <= right,
     * reverse the nodes of the list from position left to position right, and return the reversed list.
     */

    public static ListNode reverseBetween(ListNode head, int left, int right) {

        if (head.next == null || left == right) return head;

        head = new ListNode(0, head); // Add mock-node at the beginning
        ListNode current = head;
        int counter = 0; // Count each iteration to detect borders

        ListNode leftNode = null;
        ListNode rightNode = null;
        ArrayList<ListNode> nodesToReverse = new ArrayList<>();

        while (current != null) {
            if (leftNode != null) {
                nodesToReverse.add(current);
            }
            if (counter == right) {
                rightNode = current.next;
                break;
            }
            if (leftNode == null && counter + 1 == left) {
                leftNode = current;
            }
            current = current.next;
            counter += 1;
        }

        for (int i = nodesToReverse.size() - 1; i >= 0; i--) {
            leftNode.next = nodesToReverse.get(i);
            leftNode = leftNode.next;
        }

        leftNode.next = rightNode;
        return head.next;
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

    /**
     * 82.1. Straightforward solution :
     */
    public static ListNode deleteDuplicates_straightforward(ListNode head) {
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

    /**
     * 82.2. More elegant solution :
     */
    public static ListNode deleteDuplicates_elegant(ListNode head) {

        // Initialize "dummy" node to handle head removals :
        ListNode dummy = new ListNode(-1);
        dummy.next = head;

        ListNode prev = dummy;
        ListNode cur = head;

        while (cur != null && cur.next != null) {
            if (cur.val == cur.next.val) {
                // 1.1. Skip all nodes with the same value :
                while (cur.next != null && cur.val == cur.next.val) {
                    cur = cur.next;
                }
                // 1.2. Remove duplicates :
                prev.next = cur.next;
            } else {
                // 2. Move to next distinct node :
                prev = prev.next; // or just -> prev = cur;
            }
            cur = cur.next;
        }

        return dummy.next;
    }



    /**
     * 61. Rotate List
     *
     * Given the head of a linked list, rotate the list to the right by k places.
     */

    /**
     * 61.1. Not optimised :
     */
    public ListNode rotateRight(ListNode head, int k) {
        if (k == 0 || head == null || head.next == null) return head;

        ListNode pointer = head;
        ListNode delayedPointer = head;

        // 1. Define intermediate node, where list should be splitted :
        while (pointer.next != null || k > 0) {
            if (k == 0) {
                pointer = pointer.next;
                delayedPointer = delayedPointer.next;
            } else {
                pointer = (pointer.next == null) ? head : pointer.next;
                k--;
            }
        }

        // 2. Rebuild (concat) new list using sub-lists :
        if (pointer != delayedPointer) {
            ListNode newHead = delayedPointer.next; // delayedPointer.next -> becomes a head of new list
            delayedPointer.next = null;             // delayedPointer      -> becomes a tail of new list

            pointer.next = head;                    // join two sub-lists together
            head = newHead;
        }

        return head;
    }

    /**
     * 61.2. Optimised :
     *
     * Calculates size of list after first completed loop and then reduce 'k' in order to avoid additional loops.
     */
    public ListNode rotateRight_optimized(ListNode head, int k) {
        if (k == 0 || head == null || head.next == null) return head;

        ListNode pointer = head;
        ListNode delayedPointer = head;

        int counter = 0;
        int listSize = 0;

        // 1. Define intermediate node, where list should be splitted :
        while (pointer.next != null || counter < k) {
            if (counter == k) {
                pointer = pointer.next;
                delayedPointer = delayedPointer.next;
            } else {
                // Optimisation :
                if (listSize++ < k && pointer.next == null) {
                    k = k % listSize;
                    counter = 0;
                } else {
                    counter++;
                }

                pointer = (pointer.next == null) ? head : pointer.next;
            }
        }

        // 2. Rebuild (concat) new list using sub-lists :
        if (pointer != delayedPointer) {
            ListNode newHead = delayedPointer.next;
            delayedPointer.next = null;

            pointer.next = head;
            head = newHead;
        }

        return head;
    }



    /**
     * 86. Partition List
     *
     * Given the head of a linked list and a value x, partition it such that all nodes less
     * than x come before nodes greater than or equal to x.
     *
     * You should preserve the original relative order of the nodes in each of the two partitions.
     */

    public ListNode partition(ListNode head, int x) {

        ListNode dummyBigHead = new ListNode(-1);
        ListNode bigCurr = dummyBigHead;

        ListNode dummySmallHead = new ListNode(-1);
        ListNode smallCurr = dummySmallHead;

        while (head != null) {
            if (head.val < x) {
                smallCurr.next = head;
                smallCurr = smallCurr.next;
            } else {
                bigCurr.next = head;
                bigCurr = bigCurr.next;
            }
            ListNode nextNode = head.next;
            head.next = null;
            head = nextNode;
        }

        smallCurr.next = dummyBigHead.next;
        return dummySmallHead.next;
    }



    /**
     * 146. LRU Cache
     *
     * Design a data structure that follows the constraints of a Least Recently Used (LRU) cache.
     *
     * Implement the LRUCache class:
     *
     * LRUCache(int capacity) Initialize the LRU cache with positive size capacity.
     * int get(int key) Return the value of the key if the key exists, otherwise return -1.
     * void put(int key, int value) Update the value of the key if the key exists. Otherwise,
     * add the key-value pair to the cache. If the number of keys exceeds the capacity from this operation,
     * evict the least recently used key.
     * The functions get and put must each run in O(1) average time complexity.
     */

    public static class LRUCache {

        private EntryNode[] hashTable;

        private EntryNode head;
        private EntryNode tail;
        private int size = 0;

        public LRUCache(int capacity) {
            hashTable = new EntryNode[capacity];
        }

        public int get(int key) {
            int hash = hash(key);
            EntryNode first;
            EntryNode[] eqNodeData;

            if ((first = hashTable[(hashTable.length - 1) & hash]) != null
                && (eqNodeData = getEqualNodeOrPrevious(hash, key, first))[1] != null) {

                updateNodeAsLastCalled(eqNodeData[1]);
                return eqNodeData[1].value;
            } else {
                return -1; // not found
            }
        }

        public void put(int key, int value) {
            int hash = hash(key);
            EntryNode first, newNode;
            EntryNode[] eqNodeData = null;

            if ((first = hashTable[(hashTable.length - 1) & hash]) != null
                && (eqNodeData = getEqualNodeOrPrevious(hash, key, first))[1] != null) {

                eqNodeData[1].value = value;
                updateNodeAsLastCalled(eqNodeData[1]);
            } else {
                newNode = new EntryNode(tail, null, hash, key, value, null);
                // 1. Update HashTable :
                if (first == null) {
                    hashTable[(hashTable.length - 1) & hash] = newNode;
                } else { // eqNodeData[1] == null
                    eqNodeData[0].next = newNode;
                }
                // 2. Update LRU links :
                if (tail == null) {
                    initLruPointers(newNode);
                } else {
                    tail.right = newNode;
                    tail = newNode;
                }
                resizeLruCacheIfNecessary();
            }
        }

        private int hash(int key) {
            int h;
            return (h = Integer.hashCode(key)) ^ (h >>> 16);
        }

        private EntryNode getNode(int hash, int key) {
            EntryNode node = hashTable[(hashTable.length - 1) & hash];
            while (node != null) {
                if (hash == node.hash && key == node.key) {
                    break;
                } else {
                    node = node.next;
                }
            }
            return node;
        }

        private EntryNode[] getEqualNodeOrPrevious(int hash, int key, EntryNode first) { // !!
            EntryNode prev = null, node = first;
            while (node != null) {
                if (hash == node.hash && key == node.key) {
                    break;
                } else {
                    prev = node;
                    node = node.next;
                }
            }
            return new EntryNode[] {prev, node};
        }

        private void initLruPointers(EntryNode node) {
            head = node;
            tail = node;
        }

        private void updateNodeAsLastCalled(EntryNode node) {
            if (node.right != null) {
                // 1. update left reference (remove node from the middle/head) :
                if (node.left == null) {
                    head = node.right;
                } else {
                    node.left.right = node.right;
                }
                // 2. move node to the end of list :
                tail.right = node;
                node.left = tail;
                node.right = null;
                tail = node;
            }
        }

        private void resizeLruCacheIfNecessary() {
            if (++size > hashTable.length) { // size > capacity
                hashTable[(hashTable.length - 1) & hash(head.key)] = (head.next != null)
                    ? head.next : null;
                head = head.right;
                head.left = null;
            }
        }

        @AllArgsConstructor
        private class EntryNode{

            private EntryNode left;
            private EntryNode right;

            private int hash;
            private Integer key;
            private Integer value;

            private EntryNode next;
        }
    }
}
