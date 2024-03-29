/* Chip.java */

package player;
import list.*;
import dict.*;

//==============================================================================
// (3) Finding the chips (of the same color) that form connections with a chips
//==============================================================================
/**
  * Connection, A class is in unit of chips, and contain a DList to store its edges
  * (3) Finding the chips (of the same color) that form connections with a chip.
  */

/**
	* Chip, a class that represents a chip as Chip, and store its outdegree edges
	* into a DList
	**/
public class Chip{
	private int x;
	private int y;
	private int color;
	private DList edges;
	private Board board;
  
  // private Chip prev;
	private boolean visited;
  // private int depth;

  public final static int BLACK = 0;
  public final static int WHITE = 1;
  public final static int EMPTY = 2;

	public Chip(int x, int y, int color, Board board){
		this.x = x;
		this.y = y;
		this.color = color;
		this.board = board;

    // this.prev = null;
		this.visited = false;
    // this.depth = -1;

		this.edges = new DList();
	}

	public void setVisited(boolean visited){
		this.visited = visited;
	}

/*
  public void setPrev(Chip parent){
    this.prev = parent;
  }

  public void setDepth(int depth){
    this.depth = depth;
  }

  public int getDepth(){
    return this.depth;
  }

  public Chip getPrev(){
    return this.prev;
  }
*/

	public boolean getVisited(){
		return this.visited;
	}

	public int getX(){
		return this.x;
	}

	public int getY(){
		return this.y;
	}

	public int getColor(){
		return this.color;
	}

	public Board getBoard(){
		return this.board;
	}

	public DList getEdges(){
		return this.edges;
	}

	public int getNumOfEdges(){
		return this.edges.length();
	}

	public void addEdge(Chip other){
		this.edges.insertBack(other);
	}

  public boolean isStartGoal(){
    return (this.x == 0 && this.color == WHITE) || (this.y == 0 && this.color == BLACK);
  }

  public boolean isEndGoal(){
    return (this.x == 7 && this.color == WHITE) || (this.y == 7 && this.color == BLACK);
  }

	/**
		* remove a edges between this chip to other chip
		**/
	// public boolean removeEdge(Chip other){
	// 	try{
 //  		DListNode walker = (DListNode)this.edges.front();
 //  		while(walker.isValidNode()){
 //  			if (((Chip)walker.item()).equals(other)) {
 //  				walker.remove();
 //  				return true;
 //  			}
 //  			walker = (DListNode)walker.next();
 //  		}
 //  	}catch(InvalidNodeException e){
 //      System.out.println(e);
 //    }
 //    return false;
	// }

	/**
		* remove this chip and remove all edges from it.
		**/
	// public void remove(){
	// 	try{
 //  		DListNode walker = (DListNode)this.edges.front();
 //  		while(walker.isValidNode()){
 //  			DListNode walkerNext = (DListNode)walker.next();
 //  			((Chip)walker.item()).removeEdge(this);
 //  			walker.remove();
 //  			walker = walkerNext;
 //  		}
 //  		this.edges = null;
 //  	}catch(InvalidNodeException e){
 //      System.out.println(e);
 //    }
	// }

	public boolean equals(Chip other){
		return this.x == other.getX() && 
		       this.y == other.getY() &&
		       this.color == this.color &&
		       this.board.equals(other.getBoard());
	}



  /**
    * isConnected() determine whether or not two given Chips is connected or not.
    * DO NOT INCLUDE ITSELF
    * 
    * @param other another chip
    *
    * @return true is two Chip is connect, otherwise false
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
      //if this chip and other chip are in the same orthogonal, we see if there is some other
      //chip between them
      else if ((this.getX() - other.getX()) == (this.getY() - other.getY())){
        if (this.getX() < other.getX()){
          for (int i = this.getX() + 1; i < other.getX(); i++){
            if (board.elementAt(i, i) != EMPTY){
              return false;
            }
          }
          return true;
        }
        else{
          for (int i = other.getX() + 1; i < this.getX(); i++){
            if (board.elementAt(i, i) != EMPTY){
              return false;
            }
          }
          return true;
        }
      }
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

  public String toString(){
    String out = "";
    out += this.infoString() + ": ";
    try{
      DListNode walker = (DListNode)this.edges.front();
      while(walker.isValidNode()){
        out += ((Chip)walker.item()).infoString() + " - ";
        walker = (DListNode)walker.next();
      }
    }catch(InvalidNodeException e){
      System.out.println(e);
    }
    return out;
  }

  public String infoString(){
    return " [ " + x + y + ": " + color + " ] ";
  }


/*
  public boolean isStright(Chip v2){
    if (prev == null){
      return false;
    }

    if( prev.getX() == x && x == v2.getX()){
      return true;
    }else if (prev.getY() == y && y == v2.getY()) {
      return true;
    }
    else if ( ( prev.getX() - x == prev.getY() - y && x - v2.getX() == y - v2.getY() ) ||
              ( prev.getX() - x == y - prev.getY() && x - v2.getX() == v2.getY() - y ) ){
      return true;
    }else{
      return false;
    }
  }
*/  

  

}