/* MachinePlayer.java */

package player;
import list.*;


/**
 *
 *
 * Machine Player class represent the machine player in the game, which implement how to choose the next move 
 * using minimax search. Each machine player should have the current board, its color, the searchDepth for   
 * minimax search, and the hashtable for all the board has been already evaluated.
 *
 **/
public class MachinePlayer extends Player {

  private int color;

  private Board board;
  private int searchDepth;

  public final static boolean COMPUTER = true;
  public final static boolean HUMAN = false;

  public final static int EMPTY = 2;
  public final static int WHITE = 1;
  public final static int BLACK = 0;

  /* return the color of the player*/

  /**
  * Construct a new MachinePlayer with given board and search depth for test
  * Creates a machine player with the given color.  Color is either 0 (black)
  * or 1 (white).  (White has the first move.)
  * @param color the color that machine player use
  * @param givenBoard a game board contain given information
  * @param searchDepth a constant that control minimax depth
  **/
  public MachinePlayer(int color) {
    this.color = color;
    this.searchDepth = 3;
    this.board = new Board();
  }

  /**
    * Construct a new MachinePlayer with given board and search depth for test
    * @param color the color that machine player use
    * @param givenBoard a game board contain given information
    * @param searchDepth a constant that control minimax depth
    **/
  public MachinePlayer(int color, Board givenBoard, int searchDepth){
    this.color = color;
    this.board = givenBoard;
    this.searchDepth = searchDepth;
  }

  

  /** 
    * Creates a machine player with the given color and search depth.  Color is
    * either 0 (black) or 1 (white).  (White has the first move.)
    * searchDepth ONE => 
    * Your machinePlayer considers all teh moves and chooses
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

  /**
   *  getColor() gets the color of MachinePlayer color
   *
   *  @return color of MachinePlayer color
   *          
   **/
  public int getColor(){
    return this.color;
  }

  /**
   *  getBoard() gets the color of MachinePlayer board
   *
   *  @return color of MachinePlayer board
   *          
   **/
  public Board getBoard(){
    return this.board;
  }

  /**
   *  getHumanColor() gets the color of MachinePlayer.HUMAN
   *
   *  @return color of MachinePlayer.HUMAN
   *          
   **/
  public int getHumanColor(){
    if (this.color == WHITE){
      return BLACK;
    }
    else{
      return WHITE;
    }
  }

  /**
   *  getSideColor(boolean side) determines the color of the input side.
   *
   *  @param side is MachinePlayer.COMPUTER or MachinePlayer.HUMAN
   *  @return color of the input side
   *          
   **/
  private int getSideColor(boolean side){
    if(side == COMPUTER){
      return this.color;
    }else{
      return 1 - this.color;
    }
  }


  /**
   *  chooseMove() determines the next move made by machine player by calling minimax search.
   *  
   *
   *  @return the best move from the minimax search
   *          
   **/
  public Move chooseMove() {
    int alpha = Integer.MIN_VALUE; 
    int beta = Integer.MAX_VALUE;
    Move bestMove = minimaxSearch(COMPUTER, 0, this.searchDepth, alpha, beta).getBestMove();
    this.board.setBoard(bestMove, this.color);

    return bestMove;
  } 

  /**
   *  opponentMove(Move m) determines if the opponent's move m is legal and update the board
   *
   *  Unusual conditions:
   *    If the move is illegal, return false.
   *
   *  @param m is the move proposed by the opponent
   *  @return true if opponent's move is legal
   *          
   **/
  public boolean opponentMove(Move m) {

    if (this.board.isLegalMove(m, this.getHumanColor()) == true){
      this.board.setBoard(m, this.getHumanColor());
      return true;
    }
    return false;
  }

  /**
   *  forceMove(Move m) determines if the "this" player's move m is legal and update the board
   *
   *  Unusual conditions:
   *    If the move is illegal, return false.
   *
   *  @param m is the move proposed by the opponent
   *  @return true if "this" player's move is legal
   *          
   **/
  public boolean forceMove(Move m) {
    if (this.board.isLegalMove(m, this.color) == true){
      this.board.setBoard(m, this.color);
      return true;
    }
    return false;
  }

  /**
   *  minimaxSearch(boolean side, int depth, int maxDepth, int alpha, int beta) determines the best
   *  move the input side of the player can achieve by assigning scores to all the potential moves 
   *  using alpha-beta pruning.
   *
   *
   *  @param side is MachinePlayer.COMPUTER or MachinePlayer.HUMAN
   *  @param depth is the current search depth of the minimax search
   *  @param maxDepth is the maximum search depth one minimax search can reach
   *  @param alpha is the best already explored score along the path to the root for maximizer
   *  @param beta is the best already explored option along path to the root for minimizer
   *         
   *  @return Best object if the search find the maximum score with the proposed move.
   *          
   **/
  private Best minimaxSearch(boolean side, int depth, int maxDepth, int alpha, int beta){
    Best best = new Best();
    Best reply;
    int score;

    DList legalMoveList = this.board.legalMoveList(this.getSideColor(side));
    score = this.board.evaluate(this.color);

    try{
      // initilize move to any legal move.     
      if (!legalMoveList.isEmpty()) {
        best.setBestMove((Move)legalMoveList.front().item());
      }else{
        return best;
      }      
    }catch(InvalidNodeException e){
      System.out.println(e);
    }

    // Base Case
    if(score == Board.WIN){
      best.setBestScore(score - depth);
      return best;
    }
    if(score == Board.LOSE){
      best.setBestScore(score + depth);
      return best;
    }
    if (depth == maxDepth){
      best.setBestScore(score);
      return best;
    }

    //Start minimax
    if (side == COMPUTER){
      best.setBestScore(alpha);
    }
    else{
      best.setBestScore(beta);
    }
    
    try{
      DListNode walker = (DListNode)legalMoveList.front();
      while (walker.isValidNode()){
        Move tryMove = (Move)walker.item();
          
        // Change the board
        this.board.setBoard(tryMove, this.getSideColor(side));
    
        if(tryMove.x1 == 5 && tryMove.y1 == 6)

        reply = new Best();
        // Recursive call
        reply = minimaxSearch(!side, depth + 1, maxDepth, alpha, beta);

        // Undo change
        if (tryMove.moveKind == Board.STEP){
          Move undoMove = new Move(tryMove.x2, tryMove.y2, tryMove.x1, tryMove.y1);
          this.board.setBoard(undoMove, this.getSideColor(side));
        }else if(tryMove.moveKind == Board.ADD){
          this.board.setBoard(tryMove, EMPTY);
        }

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

}



