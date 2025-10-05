package org.tms.common.auth.configuration.global.algorithms;

import static java.lang.String.format;
import static java.util.Objects.isNull;
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
import org.tms.common.auth.configuration.global.algorithms.let_code.Array;
import org.tms.common.auth.configuration.global.algorithms.let_code.LetCodeUtils;
import org.tms.common.auth.configuration.global.algorithms.let_code.Stack;
import org.tms.common.auth.configuration.global.algorithms.let_code.StrCalculator;
import org.tms.common.auth.configuration.global.algorithms.let_code.TwoPointers;
import org.tms.common.auth.configuration.global.algorithms.model.BubbleSortUtil;
import org.tms.common.auth.configuration.global.algorithms.model.FastSortUtil;
import org.tms.common.auth.configuration.global.algorithms.model.FindPairToSumUtil;
import org.tms.common.auth.configuration.global.algorithms.model.ValuableGraphSearcher;

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
    void fastSortTest() {
        List<Integer> array = Arrays.asList(2, 33, 5, 11, 5, 7, 23, 3, 45, 45);
        log.info("Before: " + array);

        var sortedArray = FastSortUtil.sort(array);
        log.info("After: " + sortedArray);
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

        result = LetCodeUtils.mergeSortedArrays_IntoNewOne(new int[] {2,5,6}, 3, new int[] {1,2,3,0,0,0}, 3);
        System.out.println(Arrays.toString(result));
        result = LetCodeUtils.mergeSortedArrays_IntoNewOne(new int[] {2,5,6}, 3, new int[] {1,2,3}, 3);
        System.out.println(Arrays.toString(result));
        result = LetCodeUtils.mergeSortedArrays_IntoNewOne(new int[] {1,2,3,0,0,0}, 3, new int[] {2,5,6}, 3);
        System.out.println(Arrays.toString(result));
        result = LetCodeUtils.mergeSortedArrays_IntoNewOne(new int[] {1,2,3,}, 3, new int[] {2,5,6}, 3);
        System.out.println(Arrays.toString(result));

        result = LetCodeUtils.mergeSortedArrays_IntoNewOne(new int[] {0}, 0, new int[] {1}, 1);
        System.out.println(Arrays.toString(result));
        result = LetCodeUtils.mergeSortedArrays_IntoNewOne(new int[] {1}, 1, new int[] {}, 0);
        System.out.println(Arrays.toString(result));
    }

    @Test
    void testMergingIntoFirst(){
        int[] num1 = {1,2,3,0,0,0};

        LetCodeUtils.mergeSortedArrays_IntoFirstOne_NoExtraMemoryUsage(num1, 3, new int[] {2,5,6}, 3);
        System.out.println(Arrays.toString(num1));
    }

    @Test
    void testRemoveElement(){
        int[] nums;
        int limit;

        nums = new int[] {3,2,2,3};
        limit = LetCodeUtils.removeElement(nums, 3);

        nums = new int[] {0,1,2,2,3,0,4,2};
        limit = LetCodeUtils.removeElement(nums, 2);

        nums = new int[] {1};
        limit = LetCodeUtils.removeElement(nums, 1);

        nums = new int[] {3,1,3,3,3};
        limit = LetCodeUtils.removeElement(nums, 3);

        System.out.println("OK");
    }

    @Test
    void testRemoveDuplicatesFromSortedArray(){
        int[] nums;
        int limit;

        nums = new int[] {0,0,1,1,1,1,2,3,3};
        limit = LetCodeUtils.removeDuplicatesFromSortedArrayII(nums);

        System.out.println("OK");
    }

    @Test
    void testMajorityElement(){
        int[] nums;
        int res;

        nums = new int[] {2,2,1,1,1,2,2};
        res = LetCodeUtils.majorityElement(nums);

        nums = new int[] {2,2,1,3,1,1,4,1,1,5,1,1,6};
        res = LetCodeUtils.majorityElement(nums);

        System.out.println("OK");
    }

    @Test
    void testRotate(){
        int[] nums = {1,2,3,4,5,6};

        LetCodeUtils.rotate_solutionII(nums, 3);

        System.out.println("OK");
    }

    @Test
    void testTrap(){
        System.out.println(LetCodeUtils.trapIV(new int[] {0,1,0,2,1,0,1,3,2,1,2,1}));
        System.out.println(LetCodeUtils.trapIV(new int[] {4,2,0,3,2,5}));
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

        Array.rotate(arr1,3);
        Array.rotate(arr2,2);
        System.out.println("OK");
    }

    @Test
    void testMaxProfit() {
        int [] arr = {1,9,6,9,1,7,1,1,5,9,9,9};
        assertEquals(25,  Array.maxProfit_II(arr));
    }

    @Test
    void testCanCompleteCircuit() {
        int [] gas = {1,2,3,4,5};
        int [] cost = {3,4,5,1,2};
        assertEquals(3,  Array.canCompleteCircuit_I(gas, cost));
    }

    @Test
    void testIsPalindrome() {
        assertTrue(TwoPointers.isPalindrome("A man, a plan, a canal: Panama"));
        assertFalse(TwoPointers.isPalindrome("race a car"));
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
        String res = new Stack().simplifyPath("/home/user/Documents/../Pictures");
        assertEquals("/home/user/Pictures", res);
    }

    @Test
    void testEvalRPN() {
        var rpnCalculator = new Stack();
        assertEquals(9, rpnCalculator.evalRPN(new String [] {"2","1","+","3","*"}));
        assertEquals(6, rpnCalculator.evalRPN(new String [] {"4","13","5","/","+"}));
        assertEquals(22,
            rpnCalculator.evalRPN(new String [] {"10","6","9","3","+","-11","*","/","*","17","+","5","+"}));
    }
}