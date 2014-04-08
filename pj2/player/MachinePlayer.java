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

  public final static int EMPTY = 2;
  public final static int WHITE = 1;
  public final static int BLACK = 0;

  /* return the color of the player*/
  public int getColor(){
    return this.color;
  }

  public Board getBoard(){
    return this.board;
  }



  // Creates a machine player with the given color.  Color is either 0 (black)
  // or 1 (white).  (White has the first move.)
  public MachinePlayer(int color) {
    this.color = color;
    this.searchDepth = 1;
    this.board = new Board();
  }

  public MachinePlayer(int color, Board givenBoard, int searchDepth){
    this.color = color;
    this.board = givenBoard;
    this.searchDepth = searchDepth;
  }

  // Creates a machine player with the given color and search depth.  Color is
  // either 0 (black) or 1 (white).  (White has the first move.)

  /** 
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


  public int getHumanColor(){
    if (this.color == WHITE){
      return BLACK;
    }
    else{
      return WHITE;
    }
  }

  private Move blockWinningMove(){
    DList legalMoveList = this.board.legalMoveList(getHumanColor());
    Move blockMove;
    Chip possibleChip;
    Move possibleMove;
    Move undoMove;
    Move undoTryMove;
    try{
      DListNode walker = (DListNode)legalMoveList.front();
      while (walker.isValidNode()){
        Move tryMove = (Move)walker.item();
        if (tryMove.moveKind == Board.ADD){
          this.board.setBoard(tryMove, getHumanColor());
          if (this.board.getGraph().isWin(getHumanColor())){
            for (int i = tryMove.x1 - 1; i <= tryMove.x1 + 1; i++){
              for (int j = tryMove.y1 - 1; j <= tryMove.y1 + 1; j++){
                if ((i >= 0) && (i <= Board.DIMENSION - 1) && (j >= 0) && (j <= Board.DIMENSION - 1) && ((i != tryMove.x1) || (j != tryMove.y1))){
                  possibleMove = new Move(i, j);
                  if (this.board.isLegalMove(possibleMove, this.color)){
                    this.board.setElementAt(i, j, this.color);
                    if (this.board.getGraph().isWin(getHumanColor())){
                      this.board.setElementAt(i, j, EMPTY);
                      continue;
                    }
                    else{
                      blockMove = new Move(i, j);
                      this.board.setElementAt(i, j, EMPTY);
                      this.board.setBoard(tryMove, EMPTY);
                      return blockMove;
                    }
                  }
                }
              }
            }
          }
          this.board.setBoard(tryMove, EMPTY);
          walker = (DListNode)walker.next();
        }
        else{
          undoTryMove = new Move(tryMove.x2, tryMove.y2, tryMove.x1, tryMove.y1);
          this.board.setBoard(tryMove, getHumanColor());
          DList originalChips = this.board.getChips(this.color);
          DListNode iter = (DListNode) originalChips.front();
          while (iter.isValidNode()){
            possibleChip = (Chip) iter.item();
            if (this.board.getGraph().isWin(getHumanColor())){
              for (int i = tryMove.x1 - 1; i <= tryMove.x1 + 1; i++){
                for (int j = tryMove.y1 - 1; j <= tryMove.y1 + 1; j++){
                  if ((i >= 0) && (i <= Board.DIMENSION - 1) && (j >= 0) && (j <= Board.DIMENSION - 1) && ((i != tryMove.x1) || (j != tryMove.y1))){
                    possibleMove = new Move(i, j, possibleChip.getX(), possibleChip.getY());
                    if (this.board.isLegalMove(possibleMove, this.color)){
                      this.board.setBoard(possibleMove, this.color);
                      undoMove = new Move(possibleChip.getX(), possibleChip.getY(), i, j);
                      if (this.board.getGraph().isWin(getHumanColor())){
                        this.board.setBoard(undoMove, this.color);
                        continue;
                      }
                      else{
                        this.board.setBoard(undoTryMove, getHumanColor());
                        this.board.setBoard(undoMove, this.color);
                        return tryMove;
                      }
                    }
                  }
                }
              }
            }
          }
          this.board.setBoard(undoTryMove, EMPTY);
          walker = (DListNode)walker.next();
        }
      }
      return null;
    }
    catch(InvalidNodeException e){
        System.out.println(e);
    }
    return null;
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
/*
    currentScore = this.board.evaluate(this.color);
    if (currentScore == Board.WIN || currentScore == Board.LOSE){

    }
*/
    this.hashtable = new HashTableChained();
    // Move blockMove = blockWinningMove();
    // if (blockMove != null){
    //   this.board.setBoard(blockMove, this.color);
    //   return blockMove;
    // }
    Move bestMove = minimaxSearch(COMPUTER, 0, this.searchDepth, alpha, beta).getBestMove();
    this.board.setBoard(bestMove, this.color);

    this.hashtable.makeEmpty();

    return bestMove;
  } 

  // If the Move m is legal, records the move as a move by the opponent
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method allows your opponents to inform you of their moves.
  public boolean opponentMove(Move m) {

    if (this.board.isLegalMove(m, this.getHumanColor()) == true){
      this.board.setBoard(m, this.getHumanColor());
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
    if (this.board.isLegalMove(m, this.color) == true){
      this.board.setBoard(m, this.color);
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

  private Best minimaxSearch(boolean side, int depth, int maxDepth, int alpha, int beta){
    Best best = new Best();
    Best reply;
    int score;

    // implement hashtable for search
    Entry boardPair = this.hashtable.find(this.board);
    if (boardPair == null){
      score = this.board.evaluate(this.color);
      this.hashtable.insert(this.board, new Integer(score));
    }else{
      score = (int)((Integer)boardPair.value());
    }

    // Base Case
    if(score == Board.WIN){
      best.setBestScore(score - depth);
      best.setBestMove(new Move());

      return best;
    }
    if(score == Board.LOSE){
      best.setBestScore(score + depth);
      best.setBestMove(new Move());
      return best;
    }
    if (depth == maxDepth){
      best.setBestScore(score);
      return best;
    }

    if (side == COMPUTER){
      best.setBestScore(alpha);
    }
    else{
      best.setBestScore(beta);
    }

    DList legalMoveList = this.board.legalMoveList(this.getSideColor(side));
    try{
      DListNode walker = (DListNode)legalMoveList.front();
      while (walker.isValidNode()){
        Move tryMove = (Move)walker.item();
          
        // Change the board
        this.board.setBoard(tryMove, this.getSideColor(side));
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

  public static void main(String[] args){
    System.out.println("MachinePlayer Start Test here");
    test1();
    // test2();
    // test3();    
  }

  public static void test1 (){
    Board testBoard = new Board();
    testBoard.setElementAt(1, 1, BLACK);
    testBoard.setElementAt(2, 1, BLACK);
    testBoard.setElementAt(4, 1, BLACK);
    testBoard.setElementAt(5, 1, BLACK);
    testBoard.setElementAt(0, 2, WHITE);
    testBoard.setElementAt(1, 2, WHITE);
    testBoard.setElementAt(4, 2, WHITE);
    testBoard.setElementAt(1, 5, WHITE);
    testBoard.setElementAt(4, 5, WHITE);
    testBoard.setElementAt(1, 6, BLACK);


    MachinePlayer player = new MachinePlayer(WHITE, testBoard, 1);
    System.out.println(player.getBoard().toString());
    printScore(testBoard.evaluate(player.getColor()));
    Move move1 = player.chooseMove();
    System.out.println("(" + move1.x1 + ", " + move1.y1 + ")");
    System.out.println(player.getBoard().toString());
    printScore(testBoard.evaluate(player.getColor()));
  }

  public static void printScore(int score){
    if (score == Board.WIN){
      System.out.println("Board.WIN");
    }else if (score == Board.LOSE) {
      System.out.println("LOSE");
    }else{
      System.out.println(score);
    } 
  }

  private static void test2(){
    Board gameBoard = new Board();
    gameBoard.setElementAt(0, 2, WHITE);
    gameBoard.setElementAt(1, 4, WHITE);
    gameBoard.setElementAt(2, 1, WHITE);
    gameBoard.setElementAt(2, 2, WHITE);
    gameBoard.setElementAt(2, 5, WHITE);
    gameBoard.setElementAt(4, 1, WHITE);
    gameBoard.setElementAt(5, 1, WHITE);
    gameBoard.setElementAt(7, 1, WHITE);
    gameBoard.setElementAt(7, 3, WHITE);
    gameBoard.setElementAt(7, 5, WHITE);
    gameBoard.setElementAt(1, 0, BLACK);
    gameBoard.setElementAt(1, 1, BLACK);
    gameBoard.setElementAt(2, 7, BLACK);
    gameBoard.setElementAt(1, 3, BLACK);
    
    gameBoard.setElementAt(3, 3, BLACK);
    gameBoard.setElementAt(4, 0, BLACK);
    gameBoard.setElementAt(4, 7, BLACK);
    gameBoard.setElementAt(5, 2, BLACK);
    gameBoard.setElementAt(5, 6, BLACK);
    
    gameBoard.setElementAt(6, 2, BLACK);
    
    // gameBoard.setElementAt(2, 1, WHITE);
    System.out.println(gameBoard);
    Graph gameGraph = gameBoard.getGraph();
    System.out.println(gameGraph.toString());

    int score = gameBoard.evaluate(WHITE); 
    if (score == Board.WIN){
      System.out.println("WIN");
    }else if (score == Board.LOSE) {
      System.out.println("LOSE");
    }else{
      System.out.println(score);
    }
  }

  private static void test3(){
    Board board = new Board();
    board.setElementAt(0, 1, WHITE);
    board.setElementAt(0, 4, WHITE);
    board.setElementAt(1, 5, WHITE);
    board.setElementAt(4, 2, WHITE);
    board.setElementAt(5, 1, WHITE);
    board.setElementAt(2, 0, BLACK);
    // board.setElementAt(2, 3, BLACK);
    board.setElementAt(2, 7, BLACK);
    board.setElementAt(3, 1, BLACK);
    board.setElementAt(3, 5, BLACK);
    board.setElementAt(5, 3, BLACK);
    System.out.println(board);
    // int score = board.evaluate(BLACK);
    // if (score == Board.WIN){
    //   System.out.println("Board.WIN");
    // }
    // else if (score == Board.LOSE){
    //   System.out.println("LOSE");
    // }
    // else{
    //   System.out.println(score);
    // }
    MachinePlayer machine = new MachinePlayer(WHITE, board, 3);
    machine.chooseMove();
    System.out.println(board);
  }
}



