package org.tms.common.auth.configuration.global.algorithms;

import static java.lang.String.format;
import static java.util.Objects.isNull;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.tms.common.auth.configuration.global.algorithms.model.ValuableGraphSearcher.Node;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.tms.common.auth.configuration.global.algorithms.let_code.ArrayUtil;
import org.tms.common.auth.configuration.global.algorithms.let_code.LinkedListUtil;
import org.tms.common.auth.configuration.global.algorithms.let_code.LinkedListUtil.ListNode;
import org.tms.common.auth.configuration.global.algorithms.let_code.MatrixUtil;
import org.tms.common.auth.configuration.global.algorithms.let_code.StackUtil;
import org.tms.common.auth.configuration.global.algorithms.let_code.TreeUtil;
import org.tms.common.auth.configuration.global.algorithms.let_code.TreeUtil.BSTIterator;
import org.tms.common.auth.configuration.global.algorithms.let_code.TreeUtil.TreeNode;
import org.tms.common.auth.configuration.global.algorithms.let_code.TwoPointersUtil;
import org.tms.common.auth.configuration.global.algorithms.model.FindPairToSumUtil;
import org.tms.common.auth.configuration.global.algorithms.model.StrCalculator;
import org.tms.common.auth.configuration.global.algorithms.model.ValuableGraphSearcher;
import org.tms.common.auth.configuration.global.algorithms.model.sorting.BubbleSortUtil;
import org.tms.common.auth.configuration.global.algorithms.model.sorting.BucketSortUtil;
import org.tms.common.auth.configuration.global.algorithms.model.sorting.MergeSortUtil;
import org.tms.common.auth.configuration.global.algorithms.model.sorting.QuickSortUtil;

@Slf4j
public class AlgorithmsTest {

    private static final String ROOT_MOCK_PATH = "global/algorithms/";

    @Test
    void searchShortestWayForValuableGraphTest() {
        Node root = readMock(ROOT_MOCK_PATH.concat("graph.json"), Node.class);
        var searcher = new ValuableGraphSearcher();
        log.info(searcher.searchShortestWay(root, "Piano").toString());
    }


    @Test
    void bubbleSort() {
        int[] array = {2,33,5,11,7,23,3,45};
        System.out.println(Arrays.toString(array));

        BubbleSortUtil.sortAsc(array);
        System.out.println(Arrays.toString(array));
    }

    @Test
    void mergeSortTest() {
        //Integer[] intArr = new Integer[] {2, 33, 5, 11, 5, 7, 23, 3, 45, 45};
        int[] array = {2, 33, 5, 11, 5, 7, 23, 3, 45, 45};
        log.info("Before: " + Arrays.toString(array));

        MergeSortUtil.sort(array);
        log.info("After: " + Arrays.toString(array));
        assertArrayEquals(new int[]{2, 3, 5, 5, 11, 7, 23, 33, 45, 45}, array);

//        String[] strArr = new String[] {"Zoe", "Alice", "John", "Bob", "Charlie", "Eve"};
//        log.info("Before: " + Arrays.toString(strArr));
//
//        QuickSortUtil.sort(strArr, String::compareTo);
//        log.info("After: " + Arrays.toString(strArr));
    }


    @Test
    void fastSortTest_I() {
        List<Integer> array = Arrays.asList(2, 33, 5, 11, 5, 7, 23, 3, 45, 45);
        log.info("Before: " + array);

        var sortedArray = QuickSortUtil.sort(array);
        log.info("After: " + sortedArray);
    }

    @Test
    void fastSortTest_II() {
        Integer[] intArr = new Integer[] {2, 33, 5, 11, 5, 7, 23, 3, 45, 45};
        log.info("Before: " + Arrays.toString(intArr));

        QuickSortUtil.sort(intArr, Integer::compareTo);
        log.info("After: " + Arrays.toString(intArr));

        String[] strArr = new String[] {"Zoe", "Alice", "John", "Bob", "Charlie", "Eve"};
        log.info("Before: " + Arrays.toString(strArr));

        QuickSortUtil.sort(strArr, String::compareTo);
        log.info("After: " + Arrays.toString(strArr));
    }

    @Test
    void bucketSortTest() {
        int[] arr = new int[] {9, 3, 5, 1, 4, 7, 2, 3, 5, 0};
        log.info("Before: " + Arrays.toString(arr));

        BucketSortUtil.sort(arr, 9);
        log.info("After: " + Arrays.toString(arr));
    }


    @Test
    void findPairToSumTest() {
        int[] arr = {1,4,5,8,0,2,13,7};

        int[] res0 = FindPairToSumUtil.findPairToSum0(arr, 7);
        int[] res1 = FindPairToSumUtil.findPairToSum1(arr, 7);

        BubbleSortUtil.sortAsc(arr);

        int[] res2_1 = FindPairToSumUtil.findPairToSum2(arr, 7);
        int[] res2_2 = FindPairToSumUtil.findPairToSum2(arr, 12);
        int[] res3_1 = FindPairToSumUtil.findPairToSum3(arr, 7);
        int[] res3_2 = FindPairToSumUtil.findPairToSum3(arr, 12);

        System.out.println("OK");
    }


    //TODO: try to improve:
    @Test
    void testMergeUnsortedArrays() {

        int [] left = {1, 5, 7, 8, 16, 21};
        int [] right = {3, 13, 14, 15, 17, 23}; // bigger

        List<Integer> lt = List.of(3, 13, 14, 15, 17, 23);
        TreeSet<Integer> set = new TreeSet(lt);
        int b = set.last();
        int prB = set.floor(b);

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

        /**
         * Check some bug/improvement [*] . Now work correct only for two arrays with the same length.
         */
        System.out.println("sdf");
    }


    private Node getValuableGraph() {
        var toPianoFromGuitar = new Node("Piano", 20, new ArrayList<>());
        var toPianoFromDrum = new Node("Piano", 10, new ArrayList<>());
        var toGuitarFromDisc = new Node("Guitar", 15, List.of(toPianoFromGuitar));
        var toGuitarFromPoster = new Node("Guitar", 30, List.of(toPianoFromGuitar));
        var toDrumFromDisc = new Node("Drum", 20, List.of(toPianoFromDrum));
        var toDrumFromPoster = new Node("Drum", 35, List.of(toPianoFromDrum));
        var toDiscFromBook = new Node("Disc", 5, List.of(toGuitarFromDisc, toDrumFromDisc));
        var toPosterFromBook = new Node("Poster", 0, List.of(toGuitarFromPoster, toDrumFromPoster));

        return new Node("Book", 0, List.of(toPosterFromBook, toDiscFromBook));
    }

    private <T> T readMock(String path, Class<T> targetType) {
        try {
            URL url = new ClassPathResource(path).getURL();
            if (isNull(url)) {
                throw new RuntimeException(format("Unexpected error: cannot build URL for resource {}", path));
            }
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(new FileReader(url.getPath()), targetType);
        } catch (IOException ex) {
            throw new RuntimeException("Unexpected error: cannot read MOCK from JSON file");
        }
    }



    /**
     * LetCode Tasks :.......
     */

    @Test
    void testMergingSortedArray(){
        int[] result;

        result = ArrayUtil.mergeSortedArrays_IntoNewOne(new int[] {2,5,6}, 3, new int[] {1,2,3,0,0,0}, 3);
        System.out.println(Arrays.toString(result));
        result = ArrayUtil.mergeSortedArrays_IntoNewOne(new int[] {2,5,6}, 3, new int[] {1,2,3}, 3);
        System.out.println(Arrays.toString(result));
        result = ArrayUtil.mergeSortedArrays_IntoNewOne(new int[] {1,2,3,0,0,0}, 3, new int[] {2,5,6}, 3);
        System.out.println(Arrays.toString(result));
        result = ArrayUtil.mergeSortedArrays_IntoNewOne(new int[] {1,2,3,}, 3, new int[] {2,5,6}, 3);
        System.out.println(Arrays.toString(result));

        result = ArrayUtil.mergeSortedArrays_IntoNewOne(new int[] {0}, 0, new int[] {1}, 1);
        System.out.println(Arrays.toString(result));
        result = ArrayUtil.mergeSortedArrays_IntoNewOne(new int[] {1}, 1, new int[] {}, 0);
        System.out.println(Arrays.toString(result));
    }

    @Test
    void testMergingIntoFirst(){
        int[] num1 = {1,2,3,0,0,0};

        ArrayUtil.mergeSortedArrays_IntoFirstOne_NoExtraMemoryUsage(num1, 3, new int[] {2,5,6}, 3);
        System.out.println(Arrays.toString(num1));
    }

    @Test
    void testRemoveElement(){
        int[] nums;
        int limit;

        nums = new int[] {3,2,2,3};
        limit = ArrayUtil.removeElement(nums, 3);

        nums = new int[] {0,1,2,2,3,0,4,2};
        limit = ArrayUtil.removeElement(nums, 2);

        nums = new int[] {1};
        limit = ArrayUtil.removeElement(nums, 1);

        nums = new int[] {3,1,3,3,3};
        limit = ArrayUtil.removeElement(nums, 3);

        System.out.println("OK");
    }

    @Test
    void testRemoveDuplicatesFromSortedArray(){
        int[] nums;
        int limit;

        nums = new int[] {0,0,1,1,1,1,2,3,3};
        limit = ArrayUtil.removeDuplicatesFromSortedArrayII(nums);

        System.out.println("OK");
    }

    @Test
    void testMajorityElement(){
        int[] nums;

        nums = new int[] {2,2,1,1,1,2,2};
        assertEquals(2, ArrayUtil.majorityElement(nums));

        nums = new int[] {2,2,1,3,1,1,4,1,1,5,1,1,6};
        assertEquals(1, ArrayUtil.majorityElement(nums));
    }

    @Test
    void testRotate(){
        int[] nums = {1,2,3,4,5,6};

        LetCodeUtils.rotate_solutionII(nums, 3);

        System.out.println("OK");
    }

    @Test
    void test_trappingRainWater(){
        assertEquals(6 ,ArrayUtil.trapI(new int[] {0,1,0,2,1,0,1,3,2,1,2,1}));
        assertEquals(9, ArrayUtil.trapII(new int[] {4,2,0,3,2,5}));
    }

    @Test
    void test_copyRandomList_usingSerialization() {
        var n1 = new LetCodeUtils.Node(3);
        var n2 = new LetCodeUtils.Node(17);
        var n3 = new LetCodeUtils.Node(7);
        n1.next = n2;
        n2.next = n3;
        n2.random = n1;

        var res = LetCodeUtils.copyRandomList_usingSerialization(n1);

        System.out.println("OK");
    }

    @Test
    void test_canFinish_forGraph() {
//        int[][] array = {
//                {1, 4},
//                {2, 4},
//                {3, 1},
//                {3, 2}
//        };
        int[][] array = {
                {1, 0},
                {0, 1}
        };
        boolean result = LetCodeUtils.canFinish(5, array);
        System.out.println("OK");
    }

    @Test
    @SneakyThrows
    void test_minMutation() {
        String [] arr = {"AAAAAAAA","AAAAAAAC","AAAAAACC","AAAAACCC","AAAACCCC","AACACCCC","ACCACCCC","ACCCCCCC","CCCCCCCA"};
        int r = LetCodeUtils.minMutation("AAAAAAAA", "CCCCCCCC", arr);
        System.out.println("OK");

        String healthWorkersJsonArray = "[{\"id\":1,\"name\":\"RehamMuzzamil\",\"qualification\":\"MBBS\",\"yearsOfExperience\":1.5},{\"id\":2,\"name\":\"MichaelJohn\",\"qualification\":\"FCPS\",\"yearsOfExperience\":5}]";
        ObjectMapper objectMapper = new ObjectMapper();

        System.out.println("OK");
    }

    @Test
    @SneakyThrows
    void test_exit() {

        int [] arr1 = {1,2,3,4,5,6,7};
        int [] arr2 = {-1,-100,3,99};

        ArrayUtil.rotate(arr1,3);
        ArrayUtil.rotate(arr2,2);
        System.out.println("OK");
    }

    @Test
    void testMaxProfit() {
        int [] arr = {1,9,6,9,1,7,1,1,5,9,9,9};
        assertEquals(25,  ArrayUtil.maxProfit_II(arr));
    }

    @Test
    void testCanCompleteCircuit() {
        int [] gas = {1,2,3,4,5};
        int [] cost = {3,4,5,1,2};
        assertEquals(3,  ArrayUtil.canCompleteCircuit_I(gas, cost));
    }

    @Test
    void testIsPalindrome() {
        assertTrue(TwoPointersUtil.isPalindrome("A man, a plan, a canal: Panama"));
        assertFalse(TwoPointersUtil.isPalindrome("race a car"));
    }

    @Test
    void testSorting() {
        NavigableSet<String> set = new TreeSet<>(Comparator.comparing(String::length));
        set.add("abc");
        set.add("aaaaaa");
        set.add("abbc");
        System.out.println(set);
        var c = set.comparator();

        List<String> list1 = Arrays.asList("a", "b", "c");
        List<String> list2 = Arrays.asList("a", "b", "c");
        List<String> list3 = Arrays.asList("c", "b", "a");

        System.out.println(list1.equals(list2)); // true
        System.out.println(list1.equals(list3)); // false (order matters)



    }

    @Test
    void strCalculatorTest() {
        var calculator = new StrCalculator();
        assertEquals(30, calculator.calculate("mul(3,div(pow(10,2),add(4,6)))"));
        assertEquals(125, calculator.calculate("pow(add(2,3),3)"));
        assertEquals(14, calculator.calculate("add(add(1,2),add(3,mul(2,4)))"));
        assertEquals(17, calculator.calculate("div(add(mul(3,pow(2,3)),10),2)"));
        assertEquals(225, calculator.calculate("pow(add(1,mul(2,add(3,pow(2,2)))),2)"));
    }

    @Test
    void test_stack_simplifyPath() {
        String res = new StackUtil().simplifyPath("/home/user/Documents/../Pictures");
        assertEquals("/home/user/Pictures", res);
    }

    @Test
    void testEvalRPN() {
        var rpnCalculator = new StackUtil();
        assertEquals(9, rpnCalculator.evalRPN(new String [] {"2","1","+","3","*"}));
        assertEquals(6, rpnCalculator.evalRPN(new String [] {"4","13","5","/","+"}));
        assertEquals(22,
            rpnCalculator.evalRPN(new String [] {"10","6","9","3","+","-11","*","/","*","17","+","5","+"}));
    }

    @Test
    void test_linkedList_rotateRight() {
        var ll = new LinkedListUtil();

        var head1 = new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(4, new ListNode(5)))));
        var res1 = ll.rotateRight_optimized(head1, 2);

        var head2 = new ListNode(0, new ListNode(1, new ListNode(2)));
        var res2 = ll.rotateRight_optimized(head2, 4);

        System.out.println("OK");
    }

    @Test
    void test_linkedList_BSTIterator() {
        // 1. Create the leaf nodes first (3, 9, 20)
        TreeNode node3 = new TreeNode(3);
        TreeNode node9 = new TreeNode(9);
        TreeNode node20 = new TreeNode(20);

        // 2. Create node 15, assigning its left (9) and right (20) children
        TreeNode node15 = new TreeNode(15, node9, node20);

        // 3. Create the root node 7, assigning its left (3) and right (15) children
        TreeNode headNode = new TreeNode(7, node3, node15);

        var bstIterator = new BSTIterator(headNode);
        while (bstIterator.hasNext()) {
            System.out.println("Next val -> " + bstIterator.next());
        }

        System.out.println("Finished");
    }

    @Test
    void test_zigzagLevelOrder() {
        TreeNode node2 = new TreeNode(2, new TreeNode(4), null);
        TreeNode node3 = new TreeNode(3, null, new TreeNode(5));
        TreeNode root = new TreeNode(1, node2, node3);

        var res = TreeUtil.zigzagLevelOrder(root);

        System.out.println("Ok");
    }

    @Test
    void test_kthSmallest() {
        // 1. Create the leaf nodes (nodes with no children)
        TreeNode node1 = new TreeNode(1);
        TreeNode node4 = new TreeNode(4);
        TreeNode node6 = new TreeNode(6);

        // 2. Create the next level up
        // Node 2 has left child 1, no right child
        TreeNode node2 = new TreeNode(2, node1, null);

        // 3. Create the nodes above them
        // Node 3 has left child 2 and right child 4
        TreeNode node3 = new TreeNode(3, node2, node4);

        // Node 5 is the root of the entire tree
        // Node 5 has left child 3 and right child 6
        TreeNode root = new TreeNode(5, node3, node6);

        //var res = TreeUtil.kthSmallest_without_treeModification(root, 3);

        System.out.println("Ok");
    }

    @Test
    void test_sudoku_validation() {
        char[][] board = {
            {'.','.','.','.','5','.','.','1','.'},
            {'.','4','.','3','.','.','.','.','.'},
            {'.','.','.','.','.','3','.','.','1'},
            {'8','.','.','.','.','.','.','2','.'},
            {'.','.','2','.','7','.','.','.','.'},
            {'.','1','5','.','.','.','.','.','.'},
            {'.','.','.','.','.','2','.','.','.'},
            {'.','2','.','9','.','.','.','.','.'},
            {'.','.','4','.','.','.','.','.','.'}
        };

        assertFalse(MatrixUtil.isValidSudoku(board));
    }
}