package org.tms.common.auth.configuration.global.algorithms.let_code;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Tree {

    /**
     * Definition for a binary tree node.
     */
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode() {}
        public TreeNode(int val) { this.val = val; }
        public TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }



    /**
     * 98. Validate Binary Search Tree
     *
     * Given the root of a binary tree, determine if it is a valid binary search tree (BST).
     *
     * A valid BST is defined as follows:
     *
     * 1.The left subtree of a node contains only nodes with keys less than the node's key.
     * 2.The right subtree of a node contains only nodes with keys greater than the node's key.
     * 3.Both the left and right subtrees must also be binary search trees.
     *
     * BST example :
     *                100
     *               /     \
     *             50       150
     *            /  \     /    \
     *          25   75  125    175
     *         / \   /   / \    /   \
     *       10  30 60  110 130 160  200
     *                  /       \
     *                 105       165
     */

    public static boolean isValidBST(TreeNode root) {
        return validateBST(root.left, Integer.MIN_VALUE, root.val)
                && validateBST(root.right, root.val, Integer.MAX_VALUE);
    }

    private static boolean validateBST(TreeNode node, int min, int max) {
        if (node == null) return true;
        if (!(min < node.val && node.val < max)) return false;
        return validateBST(node.left, min, node.val) && validateBST(node.right, node.val, max);
    }



    /**
     * 105. Construct Binary Tree from Preorder and Inorder Traversal
     *
     * [Construct Binary Tree using -> Preorder Traversal + Inorder Traversal]
     *
     * Given two integer arrays preorder and inorder where preorder is the preorder traversal of a binary tree
     * and inorder is the inorder traversal of the same tree, construct and return the binary tree.
     *
     * example :
     *
     *          3
     *        /  \
     *       9   20
     *          /  \
     *         15   7
     *
     * Input:   preorder = [3,9,20,15,7], inorder = [9,3,15,20,7]
     * Output:  [3,9,20,null,null,15,7]
     */

    public static TreeNode buildTree_fromPreorderAndInorder_usingRecursion(int[] preorder, int[] inorder) {
        // It doesn't meter what to use for this block preorder or inorder (replaceable):
        if (preorder.length == 1) {
            return new TreeNode(preorder[0]);
        }

        Queue<Integer> preorderQueue = new LinkedList<>();
        for (int num : preorder) {
            preorderQueue.add(num);
        }

        return doBuildTree(preorderQueue, inorder);
    }

    private static TreeNode doBuildTree(Queue<Integer> preorder, int[] inorder) {
        int rootValIndex, rootVal = preorder.poll();

        if (inorder.length == 1) {
            return new TreeNode(rootVal, null, null);
        } else {
            for (rootValIndex = 0; inorder[rootValIndex] != rootVal; rootValIndex++); // calculate index of rootVal into inorder
            var inorderLeft = (rootValIndex == 0) ?
                    null : Arrays.copyOfRange(inorder, 0, rootValIndex);
            var inorderRight = (rootValIndex == inorder.length - 1) ?
                    null : Arrays.copyOfRange(inorder, rootValIndex + 1, inorder.length);

            // Calculate Left Child first :
            return new TreeNode(rootVal,
                    inorderLeft != null ? doBuildTree(preorder, inorderLeft) : null,
                    inorderRight != null ? doBuildTree(preorder, inorderRight) : null);
        }
    }


    public static TreeNode buildTree_fromPreorderAndInorder_usingCycle(int[] preorder, int[] inorder) {
        Queue<Integer> preorderQueue = new LinkedList<>();
        for (int num : preorder) {
            preorderQueue.add(num);
        }

        // Initialization :
        TreeNode head = null;
        Deque<ChildNodeInfo> work = new LinkedList<>(); // Use as stack
        work.push(new ChildNodeInfo(null, inorder, true)); // Let's assume our root node - is left node, for some another one (could be in theory)

        // Work cycle :
        while (!work.isEmpty()) {
            int rootValIndex, rootVal = preorderQueue.poll();
            var child = new TreeNode(rootVal, null, null);

            ChildNodeInfo nodeInfo = work.pop();
            TreeNode parent = nodeInfo.getParent();
            inorder = nodeInfo.getInorder();

            // A) Reduce stack :
            if (inorder.length == 1) {
                if (parent == null) {
                    return child; // Tree with single Node
                } else if (nodeInfo.isLeftChild) {
                    parent.left = child;
                } else {
                    parent.right = child;
                }
                // B) Increase stack :
            } else {
                // 1. Link Nodes in a Tree :
                if (head == null) {
                    head = child;
                } else if (nodeInfo.isLeftChild) {
                    parent.left = child;
                } else {
                    parent.right = child;
                }

                // 2. Add new NodeInfo to Stack (put child as a parent for next nodes) :
                for (rootValIndex = 0; inorder[rootValIndex] != rootVal; rootValIndex++); // calculate index of rootVal into inorder
                if (rootValIndex < inorder.length - 1) {
                    var inorderRight = Arrays.copyOfRange(inorder, rootValIndex + 1, inorder.length);
                    work.push(new ChildNodeInfo(child, inorderRight, false));
                }
                if (rootValIndex > 0) {
                    var inorderLeft = Arrays.copyOfRange(inorder, 0, rootValIndex);
                    work.push(new ChildNodeInfo(child, inorderLeft, true));
                }
            }
        }

        return head;
    }

    @Getter
    @AllArgsConstructor
    public static class ChildNodeInfo {
        private final TreeNode parent;
        private final int[] inorder;
        private final Boolean isLeftChild;
    }



    /**
     * 106. Construct Binary Tree from Inorder and Postorder Traversal
     *
     * [Construct Binary Tree using -> Inorder Traversal + Postorder Traversal]
     *
     * Given two integer arrays inorder and postorder where inorder is the inorder traversal of a binary tree
     * and postorder is the postorder traversal of the same tree, construct and return the binary tree.
     *
     * example :
     *
     *          3
     *        /  \
     *       9   20
     *          /  \
     *         15   7
     *
     * Input:   inorder = [9,3,15,20,7], postorder = [9,15,7,20,3]
     * Output:  [3,9,20,null,null,15,7]
     */

    public static TreeNode buildTree_fromInorderAndPostorder_usingRecursion(int[] inorder, int[] postorder) {
        // It doesn't meter what to use for this block preorder or inorder (replaceable):
        if (inorder.length == 1) {
            return new TreeNode(inorder[0]);
        }

        Queue<Integer> postorderReversedQueue = new LinkedList<>();
        for (int i = postorder.length - 1; i >= 0; i--) {
            postorderReversedQueue.add(postorder[i]);
        }

        return doBuild(inorder, postorderReversedQueue);
    }

    private static TreeNode doBuild(int[] inorder, Queue<Integer> postorder) {
        int rootValIndex, rootVal = postorder.poll();

        if (inorder.length == 1) {
            return new TreeNode(rootVal, null, null);
        } else {
            for (rootValIndex = 0; inorder[rootValIndex] != rootVal; rootValIndex++); // calculate index of rootVal into inorder
            var inorderLeft = (rootValIndex == 0) ?
                    null : Arrays.copyOfRange(inorder, 0, rootValIndex);
            var inorderRight = (rootValIndex == inorder.length - 1) ?
                    null : Arrays.copyOfRange(inorder, rootValIndex + 1, inorder.length);

            // Calculate Right Child first :
            TreeNode rightChild = inorderRight != null ? doBuild(inorderRight, postorder) : null;
            TreeNode leftChild = inorderLeft != null ? doBuild(inorderLeft, postorder) : null;
            return new TreeNode(rootVal, leftChild, rightChild);
        }
    }



    /**
     * 112. Path Sum
     *
     * Given the root of a binary tree and an integer targetSum, return true if the tree has a root-to-leaf path
     * such that adding up all the values along the path equals targetSum.
     *
     * A leaf is a node with no children.
     */

    public static boolean hasPathSum(TreeNode root, int targetSum) {
        if (root == null) return false;
        return (targetSum - root.val == 0 && Objects.isNull(root.left) && Objects.isNull(root.right))
                || hasPathSum(root.left, targetSum - root.val)
                || hasPathSum(root.right, targetSum - root.val);
    }



    /**
     * 124. Binary Tree Maximum Path Sum
     *
     * A path in a binary tree is a sequence of nodes where each pair of adjacent nodes in the sequence has an edge connecting them.
     * A node can only appear in the sequence at most once. Note that the path does not need to pass through the root.
     *
     * The path sum of a path is the sum of the node's values in the path.
     *
     * Given the root of a binary tree, return the maximum path sum of any non-empty path.
     *
     *                  10
     *               /     \\
     *             -5       5
     *            /  \     //  \
     *          2     0   12    -7
     *         / \   /   / \\  /   \
     *       10  -6 6  -11  3 16    0
     *                  /
     *                 10
     *
     *
     * SOLUTION. There are two things you have take care about on each recursion call :
     * 1. Return bigger subTree -> left branch / right branch / nothing + cur node val
     *    (in a bse case return just -> cur node val)
     * 2. Refresh max sum value -> left branch / nothing + cur node val + right branch / nothing
     */

    public static int maxPathSum(TreeNode root) {
        if (root.left == null && root.right == null) {
            return root.val;
        } else {
            AtomicInteger maxPathSumVal = new AtomicInteger(Integer.MIN_VALUE);
            int leftSum = (root.left == null) ? 0 : maxPathSum(root.left, maxPathSumVal);
            int rightSum = (root.right == null) ? 0 : maxPathSum(root.right, maxPathSumVal);
            int newMaxPathSumCandidate = Math.max(leftSum, 0) + root.val + Math.max(rightSum, 0);
            return Math.max(maxPathSumVal.get(), newMaxPathSumCandidate);
        }
    }

    private static int maxPathSum(TreeNode root, AtomicInteger maxPathSumVal) {
        if (root.left == null && root.right == null) {
            maxPathSumVal.set(Math.max(maxPathSumVal.get(), root.val));
            return root.val;
        } else {
            int leftSum = (root.left == null) ? 0 : maxPathSum(root.left, maxPathSumVal);
            int rightSum = (root.right == null) ? 0 : maxPathSum(root.right, maxPathSumVal);
            int newMaxPathSumCandidate = Math.max(leftSum, 0) + root.val + Math.max(rightSum, 0);
            maxPathSumVal.set(Math.max(maxPathSumVal.get(), newMaxPathSumCandidate));
            return Math.max(Math.max(leftSum, rightSum), 0) + root.val;
        }
    }



    /**
     * 129. Sum Root to Leaf Numbers
     *
     * You are given the root of a binary tree containing digits from 0 to 9 only.
     *
     * Each root-to-leaf path in the tree represents a number.
     *
     * For example, the root-to-leaf path 1 -> 2 -> 3 represents the number 123.
     * Return the total sum of all root-to-leaf numbers.
     * Test cases are generated so that the answer will fit in a 32-bit integer.
     *
     * A leaf node is a node with no children.
     *
     *
     * Example :
     *
     *           4
     *         /   \
     *        9     0
     *      /  \
     *     5    1
     *
     * The root-to-leaf path 4->9->5 represents the number 495.
     * The root-to-leaf path 4->9->1 represents the number 491.
     * The root-to-leaf path 4->0 represents the number 40.
     * Therefore, sum = 495 + 491 + 40 = 1026
     */

    public static int sumNumbers(TreeNode root) {
        int leftSum = Objects.isNull(root.left) ? 0 : sumNumbers(root.left, String.valueOf(root.val));
        int rightSum = Objects.isNull(root.right) ? 0 : sumNumbers(root.right, String.valueOf(root.val));
        return (leftSum == 0 && rightSum == 0) ? root.val : (leftSum + rightSum);
    }

    private static int sumNumbers(TreeNode root, String prefix) {

        if (root.left == null && root.right == null) {
            return Integer.parseInt(prefix + root.val);
        } else {
            int leftSum = (root.left == null) ? 0
                    : sumNumbers(root.left, prefix + root.val);
            int rightSum = (root.right == null) ? 0
                    : sumNumbers(root.right, prefix + root.val);
            return leftSum + rightSum;
        }
    }



    /**
     * 222. Count Complete Tree Nodes
     *
     * Given the root of a complete binary tree, return the number of the nodes in the tree.
     *
     * According to Wikipedia, every level, except possibly the last, is completely filled in a complete binary tree,
     * and all nodes in the last level are as far left as possible. It can have between 1 and 2h nodes inclusive at the last level h.
     *
     * Design an algorithm that runs in less than O(n) time complexity.
     */

    public static int countNodes(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return 1 + countNodes(root.left) + countNodes(root.right);
    }
}
