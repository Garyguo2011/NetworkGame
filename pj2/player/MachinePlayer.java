/* MachinePlayer.java */

package player;
import list.*;
import dict.*;

/**
 *  An implementation of an automatic Network player.  Keeps track of moves
 *  made by both players.  Can select a move for itself.
 */
public class MachinePlayer extends Player {

  private int color;

  private Board board;
  private int searchDepth;

  private HashTableChained hashtable;



  public final static boolean COMPUTER = true;
  public final static boolean HUMAN = false;

  public final static int MAXCHIPS = 10;
  public final static int EMPTY = 2;
  public final static int WHITE = 1;
  public final static int BLACK = 0;

  public final static int WIN = Integer.MAX_VALUE;
  public final static int LOSE = Integer.MIN_VALUE;

  /* return the color of the player*/
  public int getColor(){
    return this.color;
  }



  // Creates a machine player with the given color.  Color is either 0 (black)
  // or 1 (white).  (White has the first move.)
  public MachinePlayer(int color) {
    this.color = color;
    this.searchDepth = 2;
    this.board = new Board();
  }

  // Creates a machine player with the given color and search depth.  Color is
  // either 0 (black) or 1 (white).  (White has the first move.)

  /** 
    * searchDepth ONE => 
    * Your machinePlayter considers all teh moves and chooses
    * the one that yields the "best" board.
    *
    * SearchDepth TWO => 
    * you consider your opponent's response as well, and choose
    * the move that will yield the "best" board after your opponent makes the best
    * move available to it
    * 
    * SearchDepth THREE =>
    * you consider two MachinePlayer moves and one opponent move betweeen them
    **/
  public MachinePlayer(int color, int searchDepth) {
    this.color = color;
    this.searchDepth = searchDepth;
    this.board = new Board();
  }

  public MachinePlayer(int color, int searchDepth, Board board) {
    this.color = color;
    this.searchDepth = searchDepth;
    this.board = board;
  }

  public int getHumanColor(){
    if (this.color == WHITE){
      return BLACK;
    }
    else{
      return WHITE;
    }
  }


  // Returns a new move by "this" player.  Internally records the move (updates
  // the internal game board) as a move by "this" player.

  /** 
    *
    * Implement minimax algorithm for searching game tree.
    * NOT NEED TO IMPLEMENT A TREE DATA STRUCTRUE 
    *
    **/
  public Move chooseMove() {
    int alpha = Integer.MIN_VALUE; 
    int beta = Integer.MAX_VALUE;

    this.hashtable = new HashTableChained();
    Move bestMove = minimaxSearch(COMPUTER, this.searchDepth, alpha, beta).getBestMove();
    board.setBoard(bestMove, this.color);
    this.hashtable.makeEmpty();

    return bestMove;
  } 

  // If the Move m is legal, records the move as a move by the opponent
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method allows your opponents to inform you of their moves.
  public boolean opponentMove(Move m) {
    if (isLegalMove(m, this.getHumanColor(), this.board) == true){
      board.setBoard(m, this.getHumanColor());
      return true;
    }
    return false;
  }

  // If the Move m is legal, records the move as a move by "this" player
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method is used to help set up "Network problems" for your
  // player to solve.
  public boolean forceMove(Move m) {
    if (isLegalMove(m, this.color, this.board) == true){
      board.setBoard(m, this.color);
      return true;
    }
    return false;
  }

  private int getSideColor(boolean side){
    if(side == COMPUTER){
      return this.color;
    }else{
      return 1 - this.color;
    }
  }

  private Best minimaxSearch(boolean side, int depth, int alpha, int beta){
    Best best = new Best();
    Best reply;
    int score;
    int curColor;

    // implement hashtable for search
    Entry boardPair = this.hashtable.find(this.board);
    if (boardPair == null){
      score = this.board.evaluate(this.color);
      this.hashtable.insert(this.board, new Integer(score));
    }else{
      score = (int)((Integer)boardPair.value());
    }

    // Base Case
    if (depth == 0 || score == WIN || score == LOSE){
      if(score == WIN){
        score -= 10000 * (this.searchDepth - depth);
      }
      best.setBestScore(score);
      return best;
    }

    if (side == COMPUTER){
      best.setBestScore(alpha);
      curColor = this.color;
    }
    else{
      best.setBestScore(beta);
      curColor = getHumanColor();
    }

    DList legalMoveList = this.legalMoveList(this.getSideColor(side), this.board);
    try{
      DListNode walker = (DListNode)legalMoveList.front();
      while (walker.isValidNode()){

        // Change the board
        this.board.setBoard(((Move)walker.item()), curColor);
        // Recursive call
        reply = minimaxSearch(!side, depth - 1, alpha, beta);
        // Undo change
        this.board.setBoard(((Move)walker.item()), EMPTY);
  
        // MAXIMUM MODE
        if (side == COMPUTER && reply.getBestScore() > best.getBestScore()){
          best.setBestMove((Move)walker.item());
          best.setBestScore(reply.getBestScore());
          alpha = reply.getBestScore();
        }
        // MINIMUM MODE
        else if (side == HUMAN && reply.getBestScore() < best.getBestScore()){
          best.setBestMove((Move)walker.item());
          best.setBestScore(reply.getBestScore());
          beta = reply.getBestScore();
        }
        if (alpha >= beta){
          return best;
        }
        walker = (DListNode)walker.next();
      }
    }
    catch(InvalidNodeException e){
      System.out.println(e);
    }
    return best;
  }


//=========================================================================
//============= (1) determining whether a move is valid ===================
//=========================================================================  
  /** 
    * isLegalMove() determine whether a Move m is legal move or not.
    * 
    * @param m is a Move
    * @return true if m is legal m otherwise false.
    *
    **/
  public boolean isLegalMove(Move m, int color, Board board){
    if (this.legalTest1(m) == true && this.legalTest2(m, color) == true && this.legalTest3(m, board) == true && this.legalTest4(m, board, color) == true){
      return true;
    }
    return false;
  }

  /** 
    * legalTest1() 
    * whether a move is placed in any of the four corner.
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
    * legalTest2() 
    * whether a move is placed in a goal of the opposite color.
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
    * legalTest3() 
    * whether a move is placed in a square that is alread occupied
    **/
  private boolean legalTest3(Move testMove, Board testBoard){
    if (testBoard.elementAt(testMove.x1, testMove.y1) == EMPTY){
      return true;
    }
    else{
      return false;
    }
  }

  /** 
    * legalTest4()
    * A player may not have more than two chips in a connect group,
    * whether connected orthogonally or diagonally.
    **/
  private boolean legalTest4(Move testMove, Board testBoard, int testColor){
    int countNeighbors = 0;
    DList neighborList = new DList();
    int count = 0;
    int neighborX;
    int neighborY;
    for (int i = testMove.x1 - 1; i <= testMove.x1 + 1; i++){
      for (int j = testMove.y1 - 1; j <= testMove.y1 + 1; j++){
        if ((i >= 0) && (i <= 7) && (j >= 0) && (j <= 7) && (i != testMove.x1) && (j != testMove.y1)) {
          if (testBoard.elementAt(i, j) == testColor){
            countNeighbors++;
            neighborList.insertBack(i*10+j);
          }
        }
        count++;
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
              if ((i >= 0) && (i <= 7) && (j >= 0) && (j <= 7) && (((i != testMove.x1) && (j != testMove.y1)) || (i != neighborX) && (j != neighborY)))
                if (testBoard.elementAt(i, j) == testColor)
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

  //=========================================================================
  //============= (2) generating a list of all valid moves ==================
  //=========================================================================
  private DList legalMoveList(int color, Board board){
    DList legalList = new DList();
    if(board.getNumOfChips(color) < MAXCHIPS){
      for (int j = 0; j < Board.DIMENSION; j++){
        for (int i = 0; i < Board.DIMENSION; i++){
          Move testMove = new Move(i, j);
          if (isLegalMove(testMove, color, board) == true){
            legalList.insertBack(testMove);
          }
        }
      }  
    }else if(board.getNumOfChips(color) == MAXCHIPS){
      DList chips = board.getChips(color);
      try{
        DListNode walker = (DListNode)chips.front();
        while(walker.isValidNode()){

          for (int j = 0; j < Board.DIMENSION; j++){
            for (int i = 0; i < Board.DIMENSION; i++){
              Move testMove = new Move(i, j, ((Chip)walker.item()).getX(), ((Chip)walker.item()).getY());
              if (isLegalMove(testMove, color, board) == true){
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




