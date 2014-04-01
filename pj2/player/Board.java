/* Board.java */

package player;
import list.*;
import dict.*;


/**
 * Broad class that implements an 8x8 game board with three possible values
 * for each cell: 0, 1, or 2.
 */
public class Board {
  public final static int DIMENSION = 8;
  

  public final static int BLACK = 0;
  public final static int WHITE = 1;
  public final static int EMPTY = 2;

  public final static int ADD = 1;
  public final static int STEP = 2;

  public final static int WIN = Integer.MAX_VALUE;
  public final static int LOSE = Integer.MIN_VALUE;

  private int[][] grid;

  /**
   *  Invariants:  
   *  (1) grid.length == DIMENSION.
   *  (2) for all 0 <= i < DIMENSION, grid[i].length == DIMENSION.
   *  (3) for all 0 <= i, j < DIMENSION, grid[i][j] >= 0 and grid[i][j] <= 2.
   *  (4) grid[0][0], grid[0][7], grid[7][0], grid[7][7] == EMPTY
   *  (5) for all 1 <= x < DIMENSION - 1, grid[0][x], grid[7][x] != WHITE
   *  (6) for all 1 <= y < DIMENSION - 1, grid[ly][0], grid[y][7] != BLACK
   **/

  /**
   *  Construct a new board in which all cells are empty.
   */
  public Board(){
    this.grid = new int[DIMENSION][DIMENSION];
    for (int y = 0; y < DIMENSION; y++) {
      for (int x = 0; x < DIMENSION; x++) {
        grid[y][x] = EMPTY;
      }
    } 
  }

  /*
   * Constructor that Duplicate a game board base on the previous one
   * used by game tree search
   */
  public Board(Board givenBoard){
    this.grid = new int[DIMENSION][DIMENSION];
    for (int y = 0; y < DIMENSION; y++) {
      for (int x = 0; x < DIMENSION; x++) {
        grid[y][x] = givenBoard.elementAt(x, y);
      }
    } 
  }

  /**
   *  Set the cell (x, y) in the board to the given value.
   *  @param value to which the element should be set (normally 0, 1, or 2).
   *  @param x is the x-index.
   *  @param y is the y-index.
   *  @exception ArrayIndexOutOfBoundsException is thrown if an invalid index
   *  is given.
   **/
  public void setElementAt(int x, int y, int value) {
    grid[y][x] = value;
  }

  /*
   * Set game Bord according to input move, and mode
   */
  public void setBoard(Move curMove, int color){
    if (curMove.moveKind == ADD){
      this.setElementAt(curMove.x1, curMove.y1, color);
    }else if (curMove.moveKind == STEP) {
      this.setElementAt(curMove.x2, curMove.y2, EMPTY);
      this.setElementAt(curMove.x1, curMove.y1, color);
    }
  }

  /**
    * getChips() generate a list of chips with given color
    * 
    * @return a list of chips with given color
    **/
  public DList getChips(int color){
    DList chips = new DList();
    for (int j = 0; j < DIMENSION; j++) {
      for (int i = 0; i < DIMENSION; i ++) {
        if (this.grid[j][i] == color){
          chips.insertBack(new Chip(i, j, color, this));
        }        
      }
    }
    return chips;
  }


  /**
    * @return a number of chips of given color
    **/
  public int getNumOfChips(int color){
    int num = 0;
    for (int j = 0; j < DIMENSION; j++) {
      for (int i = 0; i < DIMENSION; i ++) {
        if (this.grid[j][i] == color){
          num++;
        }        
      }
    }
    return num;
  }

  public Graph getGraph(){
    return new Graph(this);
  }


  //=========================================================================
  //======== (5) Computing an evaluation function for a board ===============
  //=========================================================================  
  /**
    * evaluate() assigns a score to each board htat estimates how well your
    * MachinePlayer is doing.
    * 
    * Assign a maximum positive score to a win by your MachinePlayer
    * Assign a minimum negative score to a win by the Opponent
    * Assign an intermediate score to a board where neither player has completed
    * a network.
    * Assign a slightly higher score to a win in one move than to a win in three
    * moves
    *
    * Might count how many pairs of your chips can see each other, and subtract
    * the opponent's pairs
    * 
    * @return a score that represents how good is the current network to win.
    * 
    **/
  public int evaluate(int color){
    Graph graph = getGraph();
    if(this.getNumOfChips(color) < 6){
      if(color == WHITE){
        return 2 * (graph.getWhiteNumOfEdges() - graph.getBlackNumOfEdges());
      }else{
        return 2 * (graph.getBlackNumOfEdges() - graph.getWhiteNumOfEdges());
      }
    }

    boolean colorIsWin = graph.isWin(color);
    boolean colorIsLose = graph.isLose(color);  

    if(colorIsWin && colorIsLose){
      return LOSE;
    }else if (colorIsWin && !colorIsLose) {
      return WIN;
    }else if (!colorIsWin && colorIsLose) {
      return LOSE;
    }else{
      if(color == WHITE){
        return 2 * (graph.getWhiteNumOfEdges() - graph.getBlackNumOfEdges());
      }else{
        return 2 * (graph.getBlackNumOfEdges() - graph.getWhiteNumOfEdges());
      }
    }
  }

//========== From simpleBoard ==========
  /**
   *  Get the valued stored in cell (x, y).
   *  @param x is the x-index.
   *  @param y is the y-index.
   *  @return the stored value (between 0 and 2).
   *  @exception ArrayIndexOutOfBoundsException is thrown if an invalid index
   *  is given.
   */
  public int elementAt(int x, int y) {
    return grid[y][x];
  }

  /**
   *  Returns true if "this" Board and "board" have identical values in
   *    every cell.
   *  @param board is the second SimpleBoard.
   *  @return true if the boards are equal, false otherwise.
   */
  public boolean equals(Object board) {
    // Replace the following line with your solution.  Be sure to return false
    //   (rather than throwing a ClassCastException) if "board" is not
    //   a SimpleBoard.
    if(!(board instanceof Board)){
      return false;
    }
    for (int j = 0; j < DIMENSION; j++) {
      for (int i = 0; i < DIMENSION; i ++) {
        if (this.grid[j][i] != ((Board)board).elementAt(i, j)){
          return false;
        }        
      }
    }
    return true;
  }

  /**
   *  Returns a hash code for this Board.
   *  @return a number between Integer.MIN_VALUE and Integer.MAX_VALUE.
   */
  public int hashCode() {
    // Replace the following line with your solution.
    int code = 0;
    int base = 1;
    for (int i = 0; i < DIMENSION; i ++) {
      for (int j = 0; j < DIMENSION; j++) {
        code += (grid[i][j] * base + 1) * 107 * i * 97 * i * i 
              + j * 101 * j * (grid[i][j] * base + 1) * (grid[i][j] * base + 2);
        base *= 3;
      }
    }
    return code;
  }
}



