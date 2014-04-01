/* Best.java */

package player;

public class Best{
  private Move bestMove;
  private int bestScore;

  public Best(){
    this.bestMove = null;
    this.bestScore = 0;
  }

  public Best(Move m, int score){
    this.bestMove = m;
    this.bestScore = score;
  }

  protected void setBestMove(Move m){
    this.bestMove = m;
  }

  protected void setBestScore(int score){
    this.bestScore = score;
  }

  protected Move getBestMove(){
    return this.bestMove;
  }

  protected int getBestScore(){
    return this.bestScore;
  }
}
