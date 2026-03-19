package org.tms.common.auth.configuration.global.algorithms.let_code;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class GraphUtil {

  /**
   * 200. Number of Islands
   * <p>
   * Given an m x n 2D binary grid grid which represents a map of '1's (land) and '0's (water),
   * return the number of islands. An island is surrounded by water and is formed by connecting
   * adjacent lands horizontally or vertically. You may assume all four edges of the grid are all
   * surrounded by water.
   * <p>
   * POPULAR TASK!
   */

  /**
   * Base option (not refactored) :
   */
  public static int numIslands(char[][] grid) {
    int counter = 0;
    Queue<String> children = new LinkedList<>();
    Set<String> visited = new HashSet<>();

    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[0].length; j++) {
        String token;

        if (grid[i][j] == '1' && !visited.contains(token = String.valueOf(i) + j)) {
          counter++;

          // BFS is starting from here :
          children.add(token);
          visited.add(token);

          while (!children.isEmpty()) {
            token = children.poll();

            int iCurr = token.charAt(0) - '0';
            int jCurr = token.charAt(1) - '0';

            // Check <- direction :
            if (jCurr - 1 >= 0
                && grid[iCurr][jCurr - 1] == '1'
                && !visited.contains(token = String.valueOf(iCurr) + (jCurr - 1))) {
              children.add(token);
              visited.add(token);
            }

            // Check ^ direction :
            if (iCurr - 1 >= 0
                && grid[iCurr - 1][jCurr] == '1'
                && !visited.contains(token = String.valueOf(iCurr - 1) + jCurr)) {
              children.add(token);
              visited.add(token);
            }

            // Check -> direction :
            if (jCurr + 1 < grid[0].length
                && grid[iCurr][jCurr + 1] == '1'
                && !visited.contains(token = String.valueOf(iCurr) + (jCurr + 1))) {
              children.add(token);
              visited.add(token);
            }

            // Check "down" direction :
            if (iCurr + 1 < grid.length
                && grid[iCurr + 1][jCurr] == '1'
                && !visited.contains(token = String.valueOf(iCurr + 1) + jCurr)) {
              children.add(token);
              visited.add(token);
            }
          }
        }
      }
    }

    return counter;
  }

  /**
   * The same solution but a little bit optimised (refactored) :
   */
  public static int numIslands_optimised(char[][] grid) {
    int counter = 0;

    Queue<Long> neighbours = new LinkedList<>();
    Set<Long> visited = new HashSet<>();

    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[0].length; j++) {

        if (grid[i][j] == '1' && !visited.contains(toLong(i, j))) {
          counter++;
          bfs(grid, neighbours, visited, toLong(i, j));
        }
      }
    }

    return counter;
  }

  private static void bfs(char[][] grid, Queue<Long> neighbours, Set<Long> visited, long root) {
    neighbours.add(root);
    visited.add(root);

    while (!neighbours.isEmpty()) {
      long currPosition = neighbours.poll();
      int currI = parseI(currPosition);
      int currJ = parseJ(currPosition);

      if (currJ - 1 >= 0) checkNeighbour(grid, neighbours, visited, currI, currJ - 1);
      if (currI - 1 >= 0) checkNeighbour(grid, neighbours, visited, currI - 1, currJ);
      if (currJ + 1 < grid[0].length) checkNeighbour(grid, neighbours, visited, currI, currJ + 1);
      if (currI + 1 < grid.length) checkNeighbour(grid, neighbours, visited, currI + 1, currJ);
    }
  }

  private static void checkNeighbour(char[][] grid, Queue<Long> neighbours, Set<Long> visited, int i, int j) {
    long neighbourPosition;
    if (grid[i][j] == '1' && !visited.contains(neighbourPosition = toLong(i, j))) {
      neighbours.add(neighbourPosition);
      visited.add(neighbourPosition);
    }
  }

  private static long toLong(int i, int j) {
    return ((long) i << 32) | ((long) j & 0xFFFFFFFFL);
  }

  private static int parseI(long packed) {
    return (int) (packed >>> 32);
  }

  private static int parseJ(long packed) {
    return (int) packed;
  }
}
