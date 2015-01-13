import java.util.HashSet;
import java.util.ArrayList;
import java.util.Arrays;

class Point {
  int x, y;

  Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  int getX() {
    return x;
  }

  int getY() {
    return y;
  }
}

class Sudoku {

  int[][] grid;
  ArrayList<HashSet<Integer>> rowSets;
  ArrayList<HashSet<Integer>> colSets;
  ArrayList<ArrayList<HashSet<Integer>>> blockSets;
  ArrayList<ArrayList<HashSet<Integer>>> attemptGrid;
  ArrayList<Point> visited;
  HashSet<Integer> baseAttemptGrid;

  Sudoku(int[][] grid) {
    this.grid = grid;
    rowSets = new ArrayList<HashSet<Integer>>(9);
    for (int i = 0; i<9; i++)
      rowSets.add(new HashSet<Integer>(9));
    colSets = new ArrayList<HashSet<Integer>>(9);
    for (int i = 0; i<9; i++)
      colSets.add(new HashSet<Integer>(9));
    blockSets = new ArrayList<ArrayList<HashSet<Integer>>>(3);
    for (int i = 0; i<3; i++) {
      blockSets.add(new ArrayList<HashSet<Integer>>(3));
      ArrayList<HashSet<Integer>> arr = blockSets.get(i);
      for (int j = 0; j<3; j++) {
        arr.add(new HashSet<Integer>(3));
      }
    }
    for (int m = 0; m<9; m++) {
      for (int n = 0; n<9; n++) {
        if (grid[m][n] != 0) {
          rowSets.get(m).add(grid[m][n]);
          colSets.get(n).add(grid[m][n]);
          blockSets.get(m/3).get(n/3).add(grid[m][n]);
        }
      }
    }
    baseAttemptGrid = new HashSet<Integer>(9);
    for (int i = 1; i<=9; i++)
      baseAttemptGrid.add(i);
    attemptGrid = new ArrayList<ArrayList<HashSet<Integer>>>(9);
    for (int i = 0; i<9; i++) {
      ArrayList<HashSet<Integer>> arr = (new
        ArrayList<HashSet<Integer>>(9));
      attemptGrid.add(arr);
      for (int j=0; j<9; j++) {
        arr.add(new HashSet<Integer>(baseAttemptGrid));
      }
    }
    visited = new ArrayList<Point>();
  }

  boolean canPlace(int number,int m,int n) {
    if (!(rowSets.get(m).contains(number)))
      if (!(colSets.get(n).contains(number)))
        if (!(blockSets.get(m/3).get(n/3).contains(number)))
          return true;
    return false;
  }

  void place(int number,int m,int n) {
    grid[m][n] = number;
    rowSets.get(m).add(number);
    colSets.get(n).add(number);
    blockSets.get(m/3).get(n/3).add(number);
  }

  boolean solvePoint(int m, int n) {
    for (Integer guess: attemptGrid.get(m).get(n)) {
      if (canPlace(guess,m,n)) {
        place(guess,m,n);
        visited.add(new Point(m,n));
        attemptGrid.get(m).get(n).remove(guess);
        return true;
      }
    }
    return false;
  }

  int[][] solve() {
    int m = 0;
    int n = 0;
    while (true) {
      if (grid[m][n] == 0) {
        if (solvePoint(m,n)) {
          m += 1;
          if (m > 8) {
            m = 0;
            n += 1;
          }
          if (n > 8)
            return grid;
        } else {
          attemptGrid.get(m).set(n,(new
            HashSet<Integer>(baseAttemptGrid)));
          Point oldp = visited.remove(visited.size()-1);
          int oldm = oldp.getX();
          int oldn = oldp.getY();
          int oldNumber = grid[oldm][oldn];
          grid[oldm][oldn] = 0;
          Integer iOldNumber = new Integer(oldNumber);
          rowSets.get(oldm).remove(iOldNumber);
          colSets.get(oldn).remove(iOldNumber);
          blockSets.get(oldm/3).get(oldn/3).remove(iOldNumber);
          m = oldm;
          n = oldn;
        }
     } else {
       m += 1;
       if (m > 8) {
         m = 0;
         n += 1;
       }
       if (n > 8)
         return grid;
     }
    }
  }

  static int[][] makeGrid() {
    int[][] grid = new int[9][9];
    for (int m = 0; m<9; m++) {
      for (int n = 0; n<9; n++) {
        grid[m][n] = 0;
      }
    }
    return grid;
  }

  static void prettyPrint(int[][] grid) {
    StringBuffer sb = new StringBuffer(81);
    for (int i = 0; i<9; i++)
      sb.append(Arrays.toString(grid[i])+"\n");
    System.out.println(sb.toString());
  }

  static int[][] sum2d(int[][] o1, int[][]o2) {
    int[][] result = Sudoku.makeGrid();
    for (int i = 0; i<9; i++) {
      for (int j = 0; j<9; j++) {
        result[i][j] += o1[i][j]+o2[i][j];
      }
    }
    return result;
  }

  public static void main(String[] args) {
    int[][] result = Sudoku.makeGrid();
    for (int i = 0; i<9; i++) {
      for (int m = 0; m<9; m++) {
        for (int n = 0; n<9; n++) {
          int[][] grid = Sudoku.makeGrid();
          grid[m][n] = i;
          Sudoku sudoku = new Sudoku(grid);
          result = Sudoku.sum2d(sudoku.solve(),result);
        }
      }
    }
    Sudoku.prettyPrint(result); //Dont optimize away
  }

}
