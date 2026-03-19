package org.tms.common.auth.configuration.global.algorithms.let_code;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class StackUtil {

  /**
   * 20. Valid Parentheses
   * <p>
   * Given a string s containing just the characters '(', ')', '{', '}', '[' and ']', determine if
   * the input string is valid.
   * <p>
   * An input string is valid if:
   * <p>
   * 1. Open brackets must be closed by the same type of brackets. 2. Open brackets must be closed
   * in the correct order. 3. Every close bracket has a corresponding open bracket of the same
   * type.
   */

  public static boolean isValid(String s) {
    if (s.length() < 2) {
      return false;
    }

    Deque<Character> openBracketStack = new LinkedList<>();

    Map<Character, Character> bracketsPair = Map.of(')', '(', '}', '{', ']', '[');
    List<Character> openBracketOptions = List.of('(', '{', '[');

    for (int i = 0; i < s.length(); i++) {
      if (openBracketOptions.contains(s.charAt(i))) {
        openBracketStack.push(s.charAt(i)); // adds element
      } else {
        if (openBracketStack.isEmpty()) {
          return false;
        }
        Character expectedOpenBracket = bracketsPair.get(s.charAt(i));
        Character actualOpenBracket = openBracketStack.pop();  // removes element
        if (!expectedOpenBracket.equals(actualOpenBracket)) {
          return false;
        }
      }
    }

    return openBracketStack.isEmpty();
  }


  /**
   * 71. Simplify Path
   * <p>
   * You are given an absolute path for a Unix-style file system, which always begins with a slash
   * '/'. Your task is to transform this absolute path into its simplified canonical path.
   * <p>
   * The rules of a Unix-style file system are as follows:
   * <p>
   * A single period '.' represents the current directory. A double period '..' represents the
   * previous/parent directory. Multiple consecutive slashes such as '//' and '///' are treated as a
   * single slash '/'. Any sequence of periods that does not match the rules above should be treated
   * as a valid directory or file name. For example, '...' and '....' are valid directory or file
   * names.
   * <p>
   * The simplified canonical path should follow these rules:
   * <p>
   * The path must start with a single slash '/'. Directories within the path must be separated by
   * exactly one slash '/'. The path must not end with a slash '/', unless it is the root directory.
   * The path must not have any single or double periods ('.' and '..') used to denote current or
   * parent directories. Return the simplified canonical path.
   */

  public String simplifyPath(String path) {
    Deque<String> dirs = new LinkedList<>();
    StringBuilder tmp = new StringBuilder();

    for (int i = 0; i < path.length(); i++) {
      char cur = path.charAt(i);
      if (cur == '/') {
        // 2. process generated expression :
        if (tmp.length() > 0) {
          processCandidate(tmp.toString(), dirs);
          tmp.delete(0, tmp.length());
        }
      } else {
        // 1. accumulate expression :
        tmp.append(cur);
      }
    }

    if (tmp.length() > 0) {
      processCandidate(tmp.toString(), dirs);
    }

    return convertPathToStr(dirs);
  }

  private void processCandidate(String candidate, Deque<String> path) {
    switch (candidate) {
      case ".":
        // Do nothing. Skip cur dir
        break;
      case "..":
        if (!path.isEmpty()) {
          path.pop(); // Remove prev dir
        }
        break;
      default:
        path.push(candidate); // Populate cur dir to path
    }
  }

  private String convertPathToStr(Deque<String> path) {
    var pathStr = new StringBuilder();
    while (!path.isEmpty()) {
      pathStr.append("/").append(path.pollLast());
    }
    return pathStr.length() > 0 ? pathStr.toString() : "/";
  }



  /**
   * 155. Min Stack
   * <p>
   * Design a stack that supports push, pop, top, and retrieving the minimum element in constant
   * time.
   * <p>
   * Implement the MinStack class:
   * <p>
   * MinStack() initializes the stack object. void push(int val) pushes the element val onto the
   * stack. void pop() removes the element on the top of the stack. int top() gets the top element
   * of the stack. int getMin() retrieves the minimum element in the stack. You must implement a
   * solution with O(1) time complexity for each function.
   */

  public static class MinStack {

    private Node head = null;

    public void push(int val) {
      // Each Node has each own 'minVal' which considers only
      // value of curr Node and values from tail of Sequence (doesn't consider values behind) !
      int minVal = head != null ? Math.min(val, head.minVal) : val;

      // Put new Node in head of Queue (on top of Stack) :
      head = new Node(val, head, minVal);
    }

    public void pop() {
      if (head != null) {
        head = head.next;
      }
    }

    public int top() {
      if (head != null) {
        return head.val;
      } else {
        throw new UnsupportedOperationException("Stack is empty");
      }
    }

    public int getMin() {
      if (head != null) {
        return head.minVal;
      } else {
        throw new UnsupportedOperationException("Stack is empty");
      }
    }


    public class Node {

      private final int val;
      private final Node next;
      private final int minVal;

      public Node(int val, Node next, int minVal) {
        this.val = val;
        this.next = next;
        this.minVal = minVal;
      }
    }

  }


  /**
   * 150. Evaluate Reverse Polish Notation
   * <p>
   * You are given an array of strings tokens that represents an arithmetic expression in a Reverse
   * Polish Notation.
   * <p>
   * Evaluate the expression. Return an integer that represents the value of the expression.
   * <p>
   * Note that:
   * <p>
   * - The valid operators are '+', '-', '*', and '/'. - Each operand may be an integer or another
   * expression. - The division between two integers always truncates toward zero. - There will not
   * be any division by zero. - The input represents a valid arithmetic expression in a reverse
   * polish notation. - The answer and all the intermediate calculations can be represented in a
   * 32-bit integer.
   */

  private final Map<String, BiFunction<Integer, Integer, Integer>> operatorByStrAlias = Map.of(
      "+", Integer::sum,
      "-", (a, b) -> a - b,
      "*", (a, b) -> a * b,
      "/", (a, b) -> a / b
  );

  public int evalRPN(String[] tokens) {

    Deque<Integer> operandsStack = new LinkedList<>();

    BiFunction<Integer, Integer, Integer> operator;
    int leftOpnd , rightOpnd;

    for (String token : tokens) {
      operator = operatorByStrAlias.get(token);
      if (operator != null) {
        rightOpnd = operandsStack.pop();
        leftOpnd = operandsStack.pop();
        operandsStack.push(operator.apply(leftOpnd, rightOpnd));
      } else {
        operandsStack.push(Integer.parseInt(token));
      }
    }

    return operandsStack.pop();
  }



  /**
   * 224. Basic Calculator
   *
   * Given a string s representing a valid expression, implement a basic calculator to evaluate it,
   * and return the result of the evaluation.
   *
   * Note: You are not allowed to use any built-in function which evaluates strings as mathematical
   * expressions, such as eval().
   */

  public int calculate(String s) {

    Deque<Integer> stack = new LinkedList<>();

    int result = 0;
    int number = 0;
    int sign = 1;

    for (int i = 0; i < s.length(); i++) {
      char curr = s.charAt(i);

      if (Character.isDigit(curr)) {
        number = (10 * number) + (curr - '0');
      } else if (curr == '+' || curr == '-') {
        result += sign * number; // sign defined on previous iteration
        sign = (curr == '+') ? 1 : -1;
        number = 0;
      } else if (curr == '(') {
        // store into stack "tmpRes" and "+/-" from "tmpRes +/- (...)" expr :
        stack.push(result);
        stack.push(sign);
        sign = 1;
        result = 0;
      } else if (curr == ')') {
        result += sign * number;
        number = 0;
        result *= stack.pop(); // update sign for 'b' (getting it from stack)
        result = stack.pop() + result;
      }
    }

    return (number == 0)
        ? result
        : result + sign * number;
  }
}
