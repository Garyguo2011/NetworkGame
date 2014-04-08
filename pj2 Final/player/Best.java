/* Best.java */

package player;


/**
 *
 * Best class used inside in minimax game tree search. keep track the best score and best move druiing search
 *
 **/
public class Best{
  private Move bestMove;
  private int bestScore;
 
  /**
   *  Constructe a new Best to store the current bestscore and best move
   *
   *  @return a new best class
   *          
   **/  
  public Best(){
    this.bestMove = null;
    this.bestScore = 0;
  }
  
  /**
   *  Constructe a new Best to store the current bestscore and best move
   *
   * @param m is the move that needs to set to the bestMove field of this object
   * @param score is the value that needs to set to the bestScore field of this object 
   * @return a new best class
   *          
   **/
  public Best(Move m, int score){
    this.bestMove = m;
    this.bestScore = score;
  }

  /**
   *  setBestMove(Move m) set the bestMove field to the input Move m.
   *
   *  @param m is the move that needs to set to the bestMove field of this object
   *          
   **/
  protected void setBestMove(Move m){
    this.bestMove = m;
  }


  /**
   *  setBestScore(int score) set the bestScore field to the input score.
   *
   *  @param score is the value that needs to set to the bestScore field of this object
   *          
   **/
  protected void setBestScore(int score){
    this.bestScore = score;
  }


  /**
   *  getBestMove() gets the bestMove field of "this" object
   *
   *  @return this.bestMove the bestMove field of "this" object
   *          
   **/
  protected Move getBestMove(){
    return this.bestMove;
  }


  /**
   *  getBestScore() gets the bestScore field of "this" object
   *
   *  @return this.bestScore the bestScore field of "this" object
   *          
   **/
  protected int getBestScore(){
    return this.bestScore;
  }
}
