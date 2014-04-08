/* Board.java */

package player;
import list.*;

/**
 *
 * Board class contains a 2D array that implements an 8x8 game board with three possible values.
 *
 **/
public class Board {
  public final static int DIMENSION = 8;
  

  public final static int BLACK = 0;
  public final static int WHITE = 1;
  public final static int EMPTY = 2;

  public final static int ADD = 1;
  public final static int STEP = 2;

  public final static int WIN = 200;
  public final static int LOSE = -200;

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
   * @return a new Board
   */
  public Board(){
    this.grid = new int[DIMENSION][DIMENSION];
    for (int y = 0; y < DIMENSION; y++) {
      for (int x = 0; x < DIMENSION; x++) {
        grid[y][x] = EMPTY;
      }
    } 
  }

  /**
   * Constructor that Duplicate a game board base on the previous one
   * used by game tree search
   * @param givenBoard a board contain given information
   * @return  a new duplicated board
   **/
  public Board(Board givenBoard){
    this.grid = new int[DIMENSION][DIMENSION];
    for (int y = 0; y < DIMENSION; y++) {
      for (int x = 0; x < DIMENSION; x++) {
        grid[y][x] = givenBoard.elementAt(x, y);
      }
    } 
  }

  /**
   *  elementAt(int x, int y) gets the valued stored in grid(x, y).
   *
   *  @param x is the x-index.
   *  @param y is the y-index.
   *  @return the stored value (between 0 and 2).
   *  
   */
  public int elementAt(int x, int y) {
    return grid[y][x];
  }

  /**
   *  setElementAt(int x, int y, int value) sets the grid(x, y) in the board to the given value.
   *
   *  @param value to the color that occupies this position (WHITE, BLACK, EMPTY).
   *  @param x is the x-index.
   *  @param y is the y-index.
   *
   *
   **/
  public void setElementAt(int x, int y, int value) {
    grid[y][x] = value;
  }

  /**
   *  setBoard(Move curMove, int color) sets the board with the input move and its color.
   *
   *  @param curMove is the Move that needs to be set to the board.
   *  @param color is the color of the input move.
   *
   * 
   **/
  public void setBoard(Move curMove, int color){
    if (curMove.moveKind == ADD){
      this.setElementAt(curMove.x1, curMove.y1, color);
    }else if (curMove.moveKind == STEP) {
      this.setElementAt(curMove.x2, curMove.y2, EMPTY);
      this.setElementAt(curMove.x1, curMove.y1, color);
    }
  }


  /**
   *  getChips(int color) obtains all the present chips in the current board of the input color.
   *
   *  @param color specifies which color the obtained chips should be
   *  @return chips is a doubly linked list that contains all the chips in the board of the input color.
   *
   * 
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
   *  getNumOfChips(int color) obtains the number of chips in the current board of the input color.
   *
   *  @param color specifies which color the number of chips obtained should be
   *  @return num is the number of chips of the input color in the current board.
   *
   * 
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

  /**
   * getGraph() obtains a corresponding graph object to "this" board
   *
   * @return new Graph(this) is the graph corresponding to "this" board/
   *
   *
   **/
  public Graph getGraph(){
    return new Graph(this);
  }

 
  /**
    * evaluate(int color) assigns a score to each board htat estimates how well your
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
    *  @param color specifies which color in the board should be evaluated.s
    *  @return a score that represents how good is the current network to win.
    * 
    **/
  public int evaluate(int color){
    Graph graph = getGraph();
    /*
    if(this.getNumOfChips(color) + this.getNumOfChips(1 - color) < 12){
      if(color == WHITE){
        return 2 * (graph.getWhiteNumOfEdges() - graph.getBlackNumOfEdges());
      }else{
        return 2 * (graph.getBlackNumOfEdges() - graph.getWhiteNumOfEdges());
      }
    }
    */
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

  /**
   *  equals(Object board) returns true if "this" Board and "board" have identical values in
   *    every grid.
   *  @param board is the simple board that "this" board should compare to.
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
    * isLegalMove(Move m, int color) determine whether a Move m is legal move or not by calling legalTest1(), 
    * legalTest2(), legalTest3(), and legalTest4().
    *
    * @param m is a Move
    * @param color is the color that the input move belongs to
    * @return true if m is legal m otherwise false.
    *
    **/
  public boolean isLegalMove(Move m, int color){
    if (m.moveKind == STEP){
      if (m.x1 == m.x2 && m.y1 == m.y2)
        return false;
      this.setElementAt(m.x2, m.y2, EMPTY);
    }
    if (rangeTest(m) == true && this.legalTest1(m) == true && this.legalTest2(m, color) == true && this.legalTest3(m) == true && this.legalTest4(m, color) == true){
      this.setElementAt(m.x2, m.y2, color);
      return true;

    }
    this.setElementAt(m.x2, m.y2, color);
    return false;
  }

  /**
    * 
    * rangeTest() that test a move is on the range or not.
    * 
    * @param m a move that being test
    * @return false if a move out of range, otherwise true
    **/
  private boolean rangeTest(Move m){
    if (m.x1 < 0 || m.x1 > 7){
      return false;
    }

    if (m.y1 < 0 || m.y1 > 7){
      return false;
    }

    if (m.moveKind == STEP){
      if (m.x2 < 0 || m.x2 > 7){
        return false;
      }

      if (m.y2 < 0 || m.y2 > 7){
        return false;
      }
    }
    return true;
  }

  /** 
    * legalTest1(Move test) determines whether a move is placed in any of the four corner. 
    *             
    * @param testMove is the move that is being tested.
    * @return true if the move is not in the four corners, and false otherwise.
    **/
  private boolean legalTest1(Move testMove){
    if (testMove.x1 == 0 && testMove.y1 == 0){
      return false;
    }
    if (testMove.x1 == 0 && testMove.y1 == 7){
      return false;
    }
    if (testMove.x1 == 7 && testMove.y1 == 0){
      return false;
    }
    if (testMove.x1 == 7 && testMove.y1 == 7){
      return false;
    }
    return true;
  }  

  /** 
    * legalTest2(Move testMove, int testColor) determines whether a move is placed in a goal of the opposite 
    * color.
    * 
    * @param testMove is the move that is being tested
    * @param testColor is the color that the testMove belongs to
    * @return true if the move is not in opponent's goal area, and false otherwise.
    *
    **/
  private boolean legalTest2(Move testMove, int testColor){
    if (testColor == WHITE){
      if (testMove.y1 == 0 || testMove.y1 == 7){
        return false;
      }
      return true;
    }
    if (testColor == BLACK){
      if (testMove.x1 == 0 || testMove.x1 == 7){
        return false;
      }
      return true;
    }
    return false;
  }

  /** 
    * legalTest3(Move testMove) determines whether a move is placed in a square that is alread occupied
    * 
    * @param testMove is the move that is being tested
    * @return true if the move is not placed in a square that is already occupied, and false otherwise.
    *
    **/
  private boolean legalTest3(Move testMove){
    if (this.elementAt(testMove.x1, testMove.y1) == EMPTY){
      return true;
    }
    else{
      return false;
    }
  }

  /** 
    * legalTest4() determines whether this move will form a chain with the other two moves
    * 
    * @param testMove is the move that is being tested
    * @param testColor is the color that the testMove belongs to
    * @return true if this move does not form a chain with the other two moves, and false otherwise.
    *
    **/
  private boolean legalTest4(Move testMove, int testColor){
    int countNeighbors = 0;
    DList neighborList = new DList();
    int neighborX;
    int neighborY;
    for (int i = testMove.x1 - 1; i <= testMove.x1 + 1; i++){
      for (int j = testMove.y1 - 1; j <= testMove.y1 + 1; j++){
        if ((i >= 0) && (i <= DIMENSION - 1) && (j >= 0) && (j <= DIMENSION - 1) && ((i != testMove.x1) || (j != testMove.y1))) {
          if (this.elementAt(i, j) == testColor){
            countNeighbors++;
            neighborList.insertBack(i*10+j);
          }
        }
      }
    }
    if (countNeighbors >= 2){
      return false;
    }
    else if (countNeighbors == 1){
      try{
        DListNode walker = (DListNode)neighborList.front();
        while(walker.isValidNode()){
          neighborX = ((Integer) walker.item()) / 10;
          neighborY = ((Integer) walker.item()) % 10;
          for (int i = neighborX - 1; i <= neighborX + 1; i++)
            for (int j = neighborY - 1; j <= neighborY + 1; j++)
              if ((i >= 0) && (i <= DIMENSION - 1) && (j >= 0) && (j <= DIMENSION - 1) && (((i != testMove.x1) || (j != testMove.y1)) && ((i != neighborX) || (j != neighborY))))
                if (this.elementAt(i, j) == testColor)
                  return false;
          walker = (DListNode)walker.next();
        }
        return true;
      }
      catch(InvalidNodeException e){
        System.out.println(e);
      }
    }
    else{
      return true;
    }
    return false;
  }

  /** 
    * legalMoveList(int color) generates all the legal move of the input color in the current board in a 
    * doubly linked list. 
    *
    * @param color spcifies which color's legal moves need to be generated
    * @return legalList is a doubly linked list that contains all the legal moves of the input color
    *
    **/
  protected DList legalMoveList(int color){
    DList legalList = new DList();

    DList chips = this.getChips(color);
    if(chips.length() < 10){
      for (int j = 0; j < DIMENSION; j++){
        for (int i = 0; i < DIMENSION; i++){
          Move testMove = new Move(i, j);
          if (isLegalMove(testMove, color) == true){
            legalList.insertBack(testMove);
          }
        }
      }  
    }else if(chips.length() == 10){
      try{
        DListNode walker = (DListNode)chips.front();
        while(walker.isValidNode()){

          for (int j = 0; j < DIMENSION; j++){
            for (int i = 0; i < DIMENSION; i++){
              Move testMove = new Move(i, j, ((Chip)walker.item()).getX(), ((Chip)walker.item()).getY());
              if (isLegalMove(testMove, color) == true){
                legalList.insertBack(testMove);
              }
            }
          }              

          walker = (DListNode)walker.next();
        }
      }catch(InvalidNodeException e){
        System.out.println(e);
      }    
    }
    return legalList;
  }
}



