package org.tms.common.auth.configuration.global.algorithms;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LetCodeUtils {






    /**
     * ----------------------------------- Task №15 ------------------------------------------
     *
     * 135. Candy
     *
     * There are n children standing in a line. Each child is assigned a rating value given in the integer array ratings.
     *
     * You are giving candies to these children subjected to the following requirements:
     *
     * Each child must have at least one candy.
     * Children with a higher rating get more candies than their neighbors.
     * Return the minimum number of candies you need to have to distribute the candies to the children.
     */

    public static int candy(int[] ratings) {
        int n = ratings.length;
        int[] candies = new int[n];
        // 0. Default candies propagation, if all would have the same rating :
        Arrays.fill(candies, 1); // fill whole array with val

        // 1. Adjust candies propagation in -> direction :
        for (int i = 1; i < n; i++) {
            if (ratings[i] > ratings[i - 1]) {
                candies[i] = candies[i - 1] + 1;
            }
        }

        // 2. Adjust candies propagation in <- direction :
        for (int i = n - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                candies[i] = Math.max(candies[i], candies[i + 1] + 1);
            }
        }

        // 3. Calculate the result :
        int totalCandies = 0;
        for (int candy : candies) {
            totalCandies += candy;
        }

        return totalCandies;
    }

    /**
     * NOTE:
     * --- Be open to calculate and save the part of work instead of performing whole work in a single iteration ! ---
     */


    /**
     * ----------------------------------- Task №16 ------------------------------------------
     *
     * 42. Trapping Rain Water
     *
     * Given n non-negative integers representing an elevation map where the width of each bar is 1,
     * compute how much water it can trap after raining.
     */


    /**
     * First approach (works, but pretty slowly) :
     */

    public static int trapI(int[] height) {

        int water = 0;

        if (height.length < 3) return water;

        int [] waterSpreading = new int[height.length];
        int maxHeight = 0;

        for (int maxHeightCandidate : height) {
            maxHeight = Math.max(maxHeight, maxHeightCandidate);
        }

        for (int n = 0; n < height.length; n++){
            waterSpreading[n] = maxHeight - height[n];
        }

        int [] expected = new int[waterSpreading.length - 2];
        Arrays.fill(expected, 0);
        while (!Arrays.equals(waterSpreading, 1, waterSpreading.length - 1, expected, 0, expected.length)){

            int firstTopIndex = -1;
            int waterCounter = 0;

            for (int n = 0; n < height.length; n++){

                if (waterSpreading[n] == 0) {
                    if (firstTopIndex >= 0){
                        water += waterCounter;
                        waterCounter = 0;

                        if (n - firstTopIndex > 1) {
                            Arrays.fill(waterSpreading, firstTopIndex + 1, n, 0);
                        }
                    }
                    firstTopIndex = n;
                } else {
                    waterCounter += firstTopIndex >= 0 ? waterSpreading[n] : 0;
                }
            }

            for (int n = 0; n < height.length; n++){
                waterSpreading[n] = Math.max(waterSpreading[n] - 1, 0);
            }
        }

        return water;
    }

    public static int trapII(int[] height) {

        int water = 0;

        if (height.length < 3) return water;

        int [] waterSpreading = new int[height.length];
        int maxHeight = 0;

        for (int maxHeightCandidate : height) {
            maxHeight = Math.max(maxHeight, maxHeightCandidate);
        }

        for (int n = 0; n < height.length; n++){
            waterSpreading[n] = maxHeight - height[n];
        }

        int shouldStopCounter;
        do {

            shouldStopCounter = 0;
            int firstTopIndex = -1;
            int waterCounter = 0;

            for (int n = 0; n < waterSpreading.length; n++){

                if (waterSpreading[n] == 0) {
                    if (firstTopIndex >= 0){
                        water += waterCounter;
                        waterCounter = 0;

                        if (n - firstTopIndex > 1) {
                            Arrays.fill(waterSpreading, firstTopIndex + 1, n, 0);
                        }
                    }
                    firstTopIndex = n;
                    shouldStopCounter += n == 0 || n == waterSpreading.length - 1 ? 0 : 1;
                } else {
                    waterCounter += firstTopIndex >= 0 ? waterSpreading[n] : 0;
                }
            }

            for (int n = 0; n < waterSpreading.length; n++){
                waterSpreading[n] = Math.max(waterSpreading[n] - 1, 0);
            }

        } while (shouldStopCounter < waterSpreading.length - 2);

        return water;
    }


    /**
     * Second approach ( Speed -> O(n); Memory -> O(n) ) :
     */

    public static int trapIII(int[] height) {

        int water, maxH;
        int [] maxLeftH = new int [height.length];
        int [] maxRightH = new int [height.length];

        // Calculate max Left Height for [i] element :
        maxH = 0;
        for (int i = 0; i < maxLeftH.length; i++){
            maxLeftH[i] = maxH;
            maxH = Math.max(maxH, height[i]);
        }

        // Calculate max Right Height for [i] element :
        maxH = 0;
        for (int i = maxRightH.length - 1; i >= 0; i--){
            maxRightH[i] = maxH;
            maxH = Math.max(maxH, height[i]);
        }

        // Calculate water spreading :
        water = 0;
        for (int i = 0; i < height.length; i++){
            water += Math.max(Math.min(maxLeftH[i], maxRightH[i]) - height[i], 0);
        }

        return water;
    }

    /**
     * Third approach ( Speed -> O(n); Memory -> O(1) ) :
     */

    public static int trapIV(int[] height) {

        int water = 0;

        int maxLeftH = height[0];
        int maxRightH = height[height.length - 1];

        for (int l = 1, r = height.length - 2; l <= r; ){
            if (maxLeftH <= maxRightH){
                water += Math.max(maxLeftH - height[l], 0);
                maxLeftH = Math.max(maxLeftH, height[l]);
                l++;
            } else {
                water += Math.max(maxRightH - height[r], 0);
                maxRightH = Math.max(maxRightH, height[r]);
                r--;
            }
        }

        return water;
    }


    /**
     * ----------------------------------- Task №17 ------------------------------------------
     *
     * 13. Roman to Integer
     *
     */


    /**
     * ----------------------------------- Task №? ------------------------------------------
     *
     * 114. Longest Common Prefix
     *
     */

    public static String longestCommonPrefix(String[] strs) {

        StringBuilder res = new StringBuilder();
        Arrays.sort(strs); // After, first and last strs will differ the most.

        String first = strs[0];
        String last = strs[strs.length-1];

        // Here just compare first and last, and build common subStr :
        for (int i = 0; i < Math.min(first.length(), last.length()); i++) {
            if (first.charAt(i) != last.charAt(i)) {
                return res.toString();
            }
            res.append(first.charAt(i));
        }

        return res.toString();
    }


    /**
     * ----------------------------------- Task №? ------------------------------------------
     *
     * 151. Reverse Words in a String
     *
     */

    /**
     * Speed -> O(n); Memory -> O(n)
     *
     * @param s
     * @return
     */
    public static String reverseWordsI(String s) {

        String[] words = s.trim().split("\\s+");

        int l = 0;
        int r = words.length - 1;

        while (l <= r){
            if (l != r){
                String tmp = words[r];
                words[r] = words[l];
                words[l] = tmp;
            }
            l++;
            r--;
        }

        return String.join(" ", words);
    }

    /**
     * Speed -> O(n); Memory -> O(n)
     *
     * @param s
     * @return
     */
    public static String reverseWordsII(String s) {

        String[] str = s.trim().split("\\s+");

        // Initialize the output string
        String out = "";

        // Iterate through the words in reverse order
        for (int i = str.length - 1; i > 0; i--) {
            // Append the current word and a space to the output
            out += str[i] + " ";
        }

        // Append the first word to the output (without trailing space)
        return out + str[0];
    }


    /**
     * ----------------------------------- Task №? ------------------------------------------
     *
     * 28. Find the Index of the First Occurrence in a String
     *
     */

    public static int strStr(String haystack, String needle) {
        int ind = -1;

        root :for (int i = 0; i <= haystack.length() - needle.length(); i++){
            if (haystack.charAt(i) == needle.charAt(0)){
                ind = i;
                for (int n = 1; n < needle.length(); n++){
                    if (haystack.charAt(n + i) != needle.charAt(n)) continue root;
                }
                return ind;
            }
        }

        return -1;
    }


    /**
     * ----------------------------------- Task №? ------------------------------------------
     *
     * 151. Reverse Words in a String
     *
     * Given an array of strings words and a width maxWidth, format the text such that each line has exactly maxWidth
     * characters and is fully (left and right) justified.
     *
     * You should pack your words in a greedy approach; that is, pack as many words as you can in each line.
     * Pad extra spaces ' ' when necessary so that each line has exactly maxWidth characters.
     *
     * Extra spaces between words should be distributed as evenly as possible. If the number of spaces on a line doesn't
     * divide evenly between words, the empty slots on the left will be assigned more spaces than the slots on the right.
     *
     * For the last line of text, it should be left-justified, and no extra space is inserted between words.
     */

    public static List<String> fullJustify(String[] words, int maxWidth) {
        List<String> ans = new ArrayList<>();
        int i = 0;
        while (i < words.length) {
            StringBuilder sb = new StringBuilder();
            while (i < words.length && sb.length() + words[i].length() <= maxWidth) {
                sb.append(words[i]).append(" ");
                i++;
            }
            String str = sb.toString().trim();
            if (i < words.length)
                ans.add(addSpaces(str, maxWidth));
            else {
                while (str.length() != maxWidth)
                    str += " ";
                ans.add(str);
            }
        }
        return ans;
    }

    private static String addSpaces(String s, int width) {
        StringBuilder sb = new StringBuilder();
        String[] arr = s.split(" ");
        int count = arr.length - 1;

        if (count == 0) {
            while (s.length() != width)
                s += " ";
            return s;
        }

        int padding = width - s.length();
        int equalSpace = padding / count;
        int moreNeeded = padding % count;
        String spaces = " ";

        while (equalSpace-- > 0)
            spaces += " ";

        String sp = spaces + " ";
        for (String ele : arr) {
            if (moreNeeded-- > 0)
                sb.append(ele + sp);
            else
                sb.append(ele + spaces);
        }
        return sb.toString().trim();
    }


    /**
     * ----------------------------------- Task №? ------------------------------------------
     * 36. Valid Sudoku
     *
     */

    public static boolean isValidSudoku(char[][] board) {
        // validate rows :
        for (char[] row : board){
            List<Integer> numbers = new ArrayList<>();
            for(char ch : row){
                if (ch != '.') numbers.add((int) ch);
            }

            if (!validateArray(numbers.toArray(new Integer[numbers.size()]))) return false;
        }

        // validate cols :
        for (int i = 0; i < board[0].length; i++){
            List<Integer> colsNumbers = new ArrayList<>();
            for (int j = 0; j < board.length; j++){
                if (board[j][i] != '.') colsNumbers.add((int) board[j][i]);
            }
            if (!validateArray(colsNumbers.toArray(new Integer[colsNumbers.size()]))) return false;
        }

        return true;
    }

    private static boolean validateArray(Integer[] arrInt){
//        List<Integer> numbers = new ArrayList<>();
//        for(char ch : arrCh){
//            if (ch != '.') numbers.add((int) ch);
//        }
//
//        Integer[] arrInt = numbers.toArray(new Integer[numbers.size()]);

        Arrays.sort(arrInt);

        for (int i = 1; i < arrInt.length; i++){
            if (arrInt[i - 1] == arrInt[i]){
                return false;
            }
        }
        return true;
    }



    /**
     * 228. Summary Ranges
     */

    public static List<String> summaryRanges(int[] nums) {
        List<String> res = new ArrayList<>();

        if (nums.length > 0) {
            int first = nums[0];
            for (int i = 1; i < nums.length; i++) {
                if (nums[i] - nums[i - 1] != 1) {
                    writeRange(res, first, nums[i - 1]);
                    first = nums[i];
                }
            }
            writeRange(res, first, nums[nums.length - 1]);
        }

        return res;
    }

    private static void writeRange(List<String> res, int first, int last) {
        res.add(first == last ?
                String.valueOf(first) : String.format("%s->%s", first, last));
    }


    /**
     * 56. Merge Intervals
     */

    public static int[][] merge(int[][] intervals) {
        if (intervals.length > 1) {
            Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
            List<int[]> res = new ArrayList<>();
            int[] prev = intervals[0];

            for (int i = 1; i < intervals.length; i++) {
                int[] cur = intervals[i];
                if (cur[0] - prev[1] <= 0) {
                    if (prev[1] < cur[1]) {
                        // merge two intervals :
                        int[] newInterval = new int[2];
                        newInterval[0] = prev[0];
                        newInterval[1] = cur[1];

                        prev = newInterval;
                    }

                    // else: live 'prev' val as it was
                } else {
                    res.add(prev);
                    prev = cur;
                }
            }
            res.add(prev);

            return res.toArray(new int[res.size()][2]);
        } else {
            return intervals;
        }
    }


    /**
     * 71. Simplify Path
     *
     * You are given an absolute path for a Unix-style file system, which always begins with a slash '/'. Your task is to transform this absolute path into its simplified canonical path.
     *
     * The rules of a Unix-style file system are as follows:
     *
     * A single period '.' represents the current directory.
     * A double period '..' represents the previous/parent directory.
     * Multiple consecutive slashes such as '//' and '///' are treated as a single slash '/'.
     * Any sequence of periods that does not match the rules above should be treated as a valid directory or file name. For example, '...' and '....' are valid directory or file names.
     * The simplified canonical path should follow these rules:
     *
     * The path must start with a single slash '/'.
     * Directories within the path must be separated by exactly one slash '/'.
     * The path must not end with a slash '/', unless it is the root directory.
     * The path must not have any single or double periods ('.' and '..') used to denote current or parent directories.
     * Return the simplified canonical path.
     */

    public static String simplifyPath(String path) {
        String backOneLayer = "..";
        path = path.replaceAll("/+", "/");
        Deque<String> pathItems = Arrays.stream(path.split("/"))
                .filter(it -> it.length() > 0)
                .collect(LinkedList::new, LinkedList::add, LinkedList::addAll);
        while (!pathItems.isEmpty()) {
            String pathItem = pathItems.pollLast();
        }
        return "OK";
    }


    /**
     * 138. Copy List with Random Pointer
     *
     * A linked list of length n is given such that each node contains an additional random pointer,
     * which could point to any node in the list, or null.
     *
     * Construct a deep copy of the list. The deep copy should consist of exactly n brand new nodes,
     * where each new node has its value set to the value of its corresponding original node. Both the next and random pointer of the new nodes should point to new nodes in the copied list such that the pointers in the original list and copied list represent the same list state. None of the pointers in the new list should point to nodes in the original list.
     *
     * For example, if there are two nodes X and Y in the original list, where X.random --> Y,
     * then for the corresponding two nodes x and y in the copied list, x.random --> y.
     *
     * Return the head of the copied linked list.
     *
     * The linked list is represented in the input/output as a list of n nodes.
     * Each node is represented as a pair of [val, random_index] where:
     *
     * val: an integer representing Node.val
     * random_index: the index of the node (range from 0 to n-1) that the random pointer points to, or null if it does not point to any node.
     * Your code will only be given the head of the original linked list.
     */

    public static Node copyRandomList(Node head) {
        if (head == null) return head;

        Node copyHead = new Node(head.val);

        Map<Integer, Node> copyNodeByVal = new HashMap<>();
        copyNodeByVal.put(copyHead.val, copyHead);

        Node cur = head, copyCur = copyHead;
        while (cur.next != null) {
            copyCur.next = new Node(cur.next.val);
            cur.next = cur; copyCur = copyCur.next;
            copyNodeByVal.put(copyCur.val, copyCur);
        }

        cur = head; copyCur = copyHead;
        do {
            Optional<Integer> key = Optional.ofNullable(cur.random).map(node -> node.val);
            if (key.isPresent()) {
                copyCur.random = copyNodeByVal.get(key.get());
            }
        } while ((cur = cur.next) != null && (copyCur = copyCur.next) != null);

        return copyHead;
    }

    /**
     * Second approach. Using serialization / deserialization
     * (typical and universal approach in Java to copy Object and whole its dependencies) :
     *
     * @param head - object to copy.
     * @return copy of the provided Object
     */
    public static Node copyRandomList_usingSerialization(Node head) {
        byte[] objBytes = null;
        Node result = null;

        // Serialization :
        try ( var bArrOutStr = new ByteArrayOutputStream();
              var objOutStr = new ObjectOutputStream(bArrOutStr)
        ) {
            objOutStr.writeObject(head);
            objOutStr.flush();

            objBytes = bArrOutStr.toByteArray();
        } catch (IOException e) {
            System.out.println("Failed..");
        }

        if (objBytes == null) return null;

        // Deserialization :
        try ( var bArrInStr = new ByteArrayInputStream(objBytes);
              var objInStr = new ObjectInputStream(bArrInStr)
        ) {
            result = (Node) objInStr.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Failed..");
        }

        return result;
    }

    public static class Node implements Serializable {
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
     * --------------------------Binary Tree--------------------------
     */




    /**
     * 82. Remove Duplicates from Sorted List II
     *
     * Given the head of a sorted linked list, delete all nodes that have duplicate numbers,
     * leaving only distinct numbers from the original list. Return the linked list sorted as well.
     */





    /**
     * 207. Course Schedule
     *
     * There are a total of numCourses courses you have to take, labeled from 0 to numCourses - 1.
     * You are given an array prerequisites where prerequisites[i] = [ai, bi] indicates that you must take course bi first if you want to take course ai.
     *
     * For example, the pair [0, 1], indicates that to take course 0 you have to first take course 1.
     * Return true if you can finish all courses. Otherwise, return false.
     *
     *
     * Constraints:
     *
     * 1 <= numCourses <= 2000
     * 0 <= prerequisites.length <= 5000
     * prerequisites[i].length == 2
     * 0 <= ai, bi < numCourses
     * All the pairs prerequisites[i] are unique.
     */

    public static boolean canFinish_withDefect(int numCourses, int[][] prerequisites) {
        Map<Integer, List<Integer>> coursesRelations = new HashMap<>();
        for (int[] coursesRelation : prerequisites) {
            List<Integer> relations = coursesRelations.computeIfAbsent(coursesRelation[0], key -> new LinkedList<>());
            relations.add(coursesRelation[1]);
        }

        for (int i = 0; i < numCourses; i++) {
            Deque<Integer> courseDependenciesGraph = new LinkedList<>();
            List<Integer> countedCourses = new LinkedList<>();
            courseDependenciesGraph.push(i);

            while (!courseDependenciesGraph.isEmpty()) {
                int course = courseDependenciesGraph.pop();
                if (countedCourses.contains(course)) {
                    return false;
                } else {
                    countedCourses.add(course);
                }
                Optional.ofNullable(coursesRelations.get(course))
                        .ifPresent(relations -> relations.forEach(courseDependenciesGraph::push));
            }
        }

        return true;
    }

    /**
     * But there is issue with counting visited nodes (courses).
     * I should consider only visited nods within one branch (not all related to root node, as in a code above).
     *
     * So that, I should have replaced Stack implementation from Collection.utils (Deque<Integer> courseDependenciesGraph)
     * with recursion :
     */

    public static boolean canFinish_tooSlow(int numCourses, int[][] prerequisites) {
        Map<Integer, List<Integer>> coursesRelations = new HashMap<>();
        for (int[] coursesRelation : prerequisites) {
            List<Integer> relations = coursesRelations.computeIfAbsent(coursesRelation[0], key -> new LinkedList<>());
            relations.add(coursesRelation[1]);
        }

        for (int i = 0; i < numCourses; i++) {
            if (!isAvailableI(i, coursesRelations, new LinkedList<>())) return false;
        }

        return true;
    }


    private static boolean isAvailableI(int courseToValidate, Map<Integer, List<Integer>> coursesRelations,
                                        List<Integer> countedCourses) {

        List<Integer> relatedCourses = coursesRelations.get(courseToValidate);
        if (relatedCourses != null) {
            if (countedCourses.removeAll(relatedCourses)) {
                return false;
            } else {
                List<Integer> updatedCountedCourses = new LinkedList<>(countedCourses);
                updatedCountedCourses.add(courseToValidate);
                for (int relatedCourse : relatedCourses) {
                    if (!isAvailableI(relatedCourse, coursesRelations, updatedCountedCourses)) return false;
                }
            }
        }
        return true;
    }

    private static boolean isAvailableII(Integer courseToValidate,
                                         Map<Integer, List<Integer>> coursesRelations,
                                         List<Integer> countedCourses) {

        List<Integer> relatedCourses = coursesRelations.get(courseToValidate);
        if (relatedCourses != null) {
            if (countedCourses.removeAll(relatedCourses)) {
                return false;
            } else {
                countedCourses.add(courseToValidate);
                for (int relatedCourse : relatedCourses) {
                    if (!isAvailableII(relatedCourse, coursesRelations, countedCourses)) return false;
                }
                countedCourses.remove(courseToValidate);
            }
        }
        return true;
    }

    /**
     * But, any way approaches above work slowly... (but work)
     *
     * In order to sped up it, you can just remove already checked courses from Map :
     */

    public static boolean canFinish(int numCourses, int[][] prerequisites) {
        Map<Integer, List<Integer>> coursesRelations = new HashMap<>();
        for (int[] coursesRelation : prerequisites) {
            List<Integer> relations = coursesRelations.computeIfAbsent(coursesRelation[0], key -> new LinkedList<>());
            relations.add(coursesRelation[1]);
        }

        for (int[] rout : prerequisites) {
            if (rout[0] < numCourses) {
                if (!isAvailable(rout[0], coursesRelations, new LinkedList<>())) return false;
            }
        }

        return true;
    }

    private static boolean isAvailable(Integer courseToValidate,
                                         Map<Integer, List<Integer>> coursesRelations,
                                         List<Integer> countedCourses) {

        List<Integer> relatedCourses = coursesRelations.get(courseToValidate);
        if (relatedCourses != null && !relatedCourses.isEmpty()) {

            // 1. Validate :
            if (countedCourses.removeAll(relatedCourses)) {
                return false;
            } else {
                countedCourses.add(courseToValidate);
                for (int relatedCourse : relatedCourses) {
                    if (!isAvailable(relatedCourse, coursesRelations, countedCourses)) return false;
                }
            }

            // 2. Clean up checked course :
            countedCourses.remove(courseToValidate);
            coursesRelations.remove(courseToValidate);
        }
        return true;
    }

    /**
     * One more workable solution (in Progress..) :
     */

    public static boolean canFinishX(int numCourses, int[][] prerequisites) {
        Map<Integer, Set<Integer>> parentsByChildCourse = new HashMap<>();
        Set<Integer> coursesWithDeps = new HashSet<>();
        Set<Integer> appliedCourses = new HashSet<>();

        for (int[] courseRelation : prerequisites) {
            coursesWithDeps.add(courseRelation[0]);
            Set<Integer> parentCourses =
                    parentsByChildCourse.computeIfAbsent(courseRelation[1], key -> new HashSet<>());
            parentCourses.add(courseRelation[0]);
        }

        int counter = 0;
        for (int i = 0; i < numCourses; i++) {
            if (!coursesWithDeps.contains(i) && parentsByChildCourse.get(i) != null) {
                appliedCourses.add(i);
                Set<Integer> parents = parentsByChildCourse.get(i);
                while (!parents.isEmpty()) {
                    if (parents.removeAll(appliedCourses)) return false;
                    appliedCourses.addAll(parents);
                    parents = parents.stream()
                                     .flatMap(childCourse -> parentsByChildCourse.computeIfAbsent(childCourse, key -> new HashSet<>()).stream())
                                                                                                                                      .collect(Collectors.toSet());
                }
                counter++;
            }
        }

        return counter > 0 || coursesWithDeps.isEmpty();
    }


    /**
     * 433. Minimum Genetic Mutation
     */

    public static int minMutation(String startGene, String endGene, String[] bank) {
        if (bank.length == 0) return -1;

        List<String> passedGenes = new LinkedList<>();
        Queue<Map.Entry<String, Integer>> currGeneOptions = new LinkedList<>();
        currGeneOptions.add(Map.entry(startGene, 0));

        while (!currGeneOptions.isEmpty()) {
            Map.Entry<String, Integer> option = currGeneOptions.poll();
            for (String candidate : bank) {
                if (passedGenes.contains(candidate)) continue;
                long diffs = IntStream.range(0, option.getKey().length())
                        .filter(i -> option.getKey().charAt(i) != candidate.charAt(i))
                        .count();
                if (1 == diffs) {
                    Map.Entry<String, Integer> newGeneOption = Map.entry(candidate, option.getValue() + 1);

                    if (endGene.equals(newGeneOption.getKey())) {
                        return newGeneOption.getValue();
                    } else {
                        currGeneOptions.add(newGeneOption);
                    }
                }
            }
            passedGenes.add(option.getKey());
        }

        return -1;
    }
}
