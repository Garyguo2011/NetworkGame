/* Chip.java */

package player;
import list.*;


/**
 *
 * Chip class represents chips on the board, which has its x- and y- index, its color, the board it belongs   
 * to, a boolean indicates whether it is visited or not, and a Dlist to store its connections, which will be 
 * used by the graph class.
 *
 **/
public class Chip{
	private int x;
	private int y;
	private int color;
	private DList edges;
	private Board board;
  
  private Chip prev;
	private boolean visited;

  public final static int BLACK = 0;
  public final static int WHITE = 1;
  public final static int EMPTY = 2;
  
 /**
  * Construct a new chips with position and the board it locate
  * 
  * @param x location at x
  * @param y location at y
  * @param color the chip color
  * @param board the board that this chip located
  *
  * @return a new chip
  **/
	public Chip(int x, int y, int color, Board board){
		this.x = x;
		this.y = y;
		this.color = color;
		this.board = board;
		this.visited = false;
		this.edges = new DList();
	}

  /**
   *  setVisited(boolean visited) changes whether the chip is visited or not in DFS.
   *
   *  @param visited is the input boolean value to set isVisited field.
   *          
   **/
	public void setVisited(boolean visited){
		this.visited = visited;
	}


  /**
   *  getVisited() obtains whether "this" chip is visited or not.
   *
   *  @return this.visited indicates whether "this" chip is visited or not.
   *          
   **/
	public boolean getVisited(){
		return this.visited;
	}

  /**
   *  getX() obtains the x-index of "this" chip.
   *
   *  @return this.x indicates the x-index of "this" chip.
   *          
   **/
	public int getX(){
		return this.x;
	}


  /**
   *  getY() obtains the y-index of "this" chip.
   *
   *  @return this.y indicates the y-index of "this" chip.
   *          
   **/
	public int getY(){
		return this.y;
	}


  /**
   *  getColor() obtains the color of "this" chip.
   *
   *  @return this.color indicates the color of "this" chip.
   *          
   **/
	public int getColor(){
		return this.color;
	}

  /**
   *  getBoard() obtains the board that "this" chip belongs to.
   *
   *  @return this.board specifies the board that "this" chip belongs to.
   *          
   **/
	public Board getBoard(){
		return this.board;
	}


  /**
   *  getEdges() obtains a doubly linked list that contains all the chips that are connected to "this" chip.
   *
   *  @return this.edges is a doubly linked list that contains all the chips that are connected to "this" chip
   *          
   **/
	public DList getEdges(){
		return this.edges;
	}


  /**
   *  getNumOfEdges() obtains how many chips that are connected to "this" chip.
   *
   *  @return this.edges.length() specifies how many elements in the doubly linked list.
   *          
   **/
	public int getNumOfEdges(){
		return this.edges.length();
	}


  /**
   * addEdge(Chip other) set the input edge as an edge to "this" chip 
   *
   * @param other is the input chip that is an edge to "this" chip
   *
   *
   **/
	public void addEdge(Chip other){
		this.edges.insertBack(other);
	}

  /**
   * isStartGoal() determine whether "this" chip is in its own starting goal area.
   *
   * @return true if it is in the starting goal area, and false otherwise
   *
   *
   **/
  public boolean isStartGoal(){
    return (this.x == 0 && this.color == WHITE) || (this.y == 0 && this.color == BLACK);
  }

  /**
   * isEndGoal() determine whether "this" chip is in its own end goal area.
   *
   * @return true if it is in the end goal area, and false otherwise
   *
   *
   **/
  public boolean isEndGoal(){
    return (this.x == 7 && this.color == WHITE) || (this.y == 7 && this.color == BLACK);
  }


  /**
   * equals(Chip other) determines whether the input chip is the same as "this" chip
   *
   * @param other is the chip that needs to be compared with "this" chip
   * @return true if "this" chip is the same as the input chip, and false otherwise.
   *
   **/
	public boolean equals(Chip other){
		return this.x == other.getX() && 
		       this.y == other.getY() &&
		       this.color == this.color &&
		       this.board.equals(other.getBoard());
	}



  /**
   * isConnected(Chip other) determines whether the input chip is connected (in straght line with and other 
   *  chips in between) "this" chip
   *
   * @param other is the chip need to be determined with "this" chip
   * @return true if "this" chip is connected with the input chip
   *
   **/
  protected boolean isConnected(Chip other){
    //see if the input other chip is in the same board as this chip, and same color
    if(this.board.equals(other.getBoard()) && this.color == other.getColor()){
      //if both chips are in the same goal area, they cannot be connected.
      if ((this.getX() == 0 && other.getX() == 0) || (this.getX() == 7 && other.getX() == 7)){
        return false;
      }
      if ((this.getY() == 0 && other.getY() == 0) || (this.getY() == 7 && other.getY() == 7)){
        return false;
      }
      //if this chip and other chip have the same x value, we see if there is some other
      //chip between them
      if (this.getX() == other.getX()){
        if (this.getY() < other.getY()){
          for (int i = this.getY() + 1; i < other.getY(); i++){
            if (board.elementAt(this.getX(), i) != EMPTY){
              return false;
            }
          }
          return true;
        }
        else{
          for (int i = other.getY() + 1; i < this.getY(); i++){
            if (board.elementAt(this.getX(), i) != EMPTY){
              return false;
            }
          }
          return true;
        }
      }
      //if this chip and other chip have the same y value, we see if there is some other
      //chip between them
      else if (this.getY() == other.getY()){
        if (this.getX() < other.getX()){
          for (int i = this.getX() + 1; i < other.getX(); i++){
            if (board.elementAt(i, this.getY()) != EMPTY){
              return false;
            }
          }
          return true;
        }
        else{
          for (int i = other.getX() + 1; i < this.getX(); i++){
            if (board.elementAt(i, this.getY()) != EMPTY){
              return false;
            }
          }
          return true;
        }
      }
      //if this chip and other chip are in the same diagonal, we see if there is some other
      //chip between them
      else if ((this.getX() - other.getX()) == (this.getY() - other.getY())){
        if (this.getX() < other.getX()){
          int y = this.getY() + 1;
          for (int i = this.getX() + 1; i < other.getX(); i++){  
            if (board.elementAt(i, y) != EMPTY){
              return false;
            }
            y++;
          }
          return true;
        }
        else{
          int y = other.getY() + 1;
          for (int i = other.getX() + 1; i < this.getX(); i++){  
            if (board.elementAt(i, y) != EMPTY){
              return false;
            }
            y++;
          }
          return true;
        }
      }
      //if this chip and other chip are in another diagonal
      else if ((this.getX() - other.getX()) == (other.getY() - this.getY())){
        if (this.getX() < other.getX()){
          int y = this.getY() - 1;
          for (int i = this.getX() + 1; i < other.getX(); i++){
            if (board.elementAt(i, y) != EMPTY){
              return false;
            }
            y--;
          }
          return true;
        }
        else{
          int y = other.getY() - 1;
          for (int i = other.getX() + 1; i < this.getX(); i++){
            if (board.elementAt(i, y) != EMPTY){
              return false;
            }
            y--;
          }
          return true;
        }
      }
      //if not satify the above conditions, the chips are not connected.
      else{
        return false;
      }
    }
    return false;
  }
 
}