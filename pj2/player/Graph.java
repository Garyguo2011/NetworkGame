/* Graph.java */

package player;
import list.*;
import dict.*;


//test for git hub
//================================================================================
// (4) determineing whether a game board contains any networks for a given player
//================================================================================
/**
	* NetworkIdentifier, class that combine all connects into a graph. and DFS,
	* to find whether or not it is win network.
	* Also store the number of pair.
	**/

//test for git hub
//================================================================================
// (4) determineing whether a game board contains any networks for a given player
//================================================================================
/**
  * NetworkIdentifier, class that combine all connects into a graph. and DFS,
  * to find whether or not it is win network.
  * Also store the number of pair.
  **///test for git hub
//================================================================================
// (4) determineing whether a game board contains any networks for a given player
//================================================================================
/**
  * NetworkIdentifier, class that combine all connects into a graph. and DFS,
  * to find whether or not it is win network.
  * Also store the number of pair.
  **///test for git hub
//================================================================================
// (4) determineing whether a game board contains any networks for a given player
//================================================================================
/**
  * NetworkIdentifier, class that combine all connects into a graph. and DFS,
  * to find whether or not it is win network.
  * Also store the number of pair.
  **///test for git hub
//================================================================================
// (4) determineing whether a game board contains any networks for a given player
//================================================================================
/**
  * NetworkIdentifier, class that combine all connects into a graph. and DFS,
  * to find whether or not it is win network.
  * Also store the number of pair.
  **///test for git hub
//================================================================================
// (4) determineing whether a game board contains any networks for a given player
//================================================================================
/**
  * NetworkIdentifier, class that combine all connects into a graph. and DFS,
  * to find whether or not it is win network.
  * Also store the number of pair.
  **///test for git hub
//================================================================================
// (4) determineing whether a game board contains any networks for a given player
//================================================================================
/**
  * NetworkIdentifier, class that combine all connects into a graph. and DFS,
  * to find whether or not it is win network.
  * Also store the number of pair.
  **///test for git hub
//================================================================================
// (4) determineing whether a game board contains any networks for a given player
//================================================================================
/**
  * NetworkIdentifier, class that combine all connects into a graph. and DFS,
  * to find whether or not it is win network.
  * Also store the number of pair.
  **///test for git hub
//================================================================================
// (4) determineing whether a game board contains any networks for a given player
//================================================================================
/**
  * NetworkIdentifier, class that combine all connects into a graph. and DFS,
  * to find whether or not it is win network.
  * Also store the number of pair.
  **///test for git hub
//================================================================================
// (4) determineing whether a game board contains any networks for a given player
//================================================================================
/**
  * NetworkIdentifier, class that combine all connects into a graph. and DFS,
  * to find whether or not it is win network.
  * Also store the number of pair.
  **/


public class Graph{

  public final static int BLACK = 0;
  public final static int WHITE = 1;
  public final static int EMPTY = 2;

  private Board board;

  DList whiteGraph;
  int whiteNumOfEdges;

  DList blackGraph;
  int blackNumOfEdges;

  
  /**
  	* construct a graph that will be used for DFS.
  	**/
  public Graph(Board board){
  	this.board = board;
    this.whiteNumOfEdges = 0;
    this.blackNumOfEdges = 0;
    this.whiteGraph = this.buildGraph(WHITE);
  	this.blackGraph = this.buildGraph(BLACK);
  }

  private DList buildGraph(int color){
  	DList graph = this.board.getChips(color);
    DListNode walker2;
    if (graph.isEmpty()) {
      return graph;
    }
    int totalEdeges = 0;
    try{
      DListNode walker1 = (DListNode)graph.front();
      while(walker1.isValidNode() && walker1.next().isValidNode()){
        walker2 = (DListNode)walker1.next();
        while(walker2.isValidNode()){
          if (((Chip)walker1.item()).isConnected((Chip)walker2.item())){
            ((Chip)walker1.item()).addEdge((Chip)walker2.item());
            ((Chip)walker2.item()).addEdge((Chip)walker1.item());
            totalEdeges++;
          }
          walker2 = (DListNode)walker2.next();
        }
        walker1 = (DListNode)walker1.next();
      }
      if(color == WHITE){
        this.whiteNumOfEdges = totalEdeges;
      }else if (color == BLACK) {
        this.blackNumOfEdges = totalEdeges;
      }
    }catch(InvalidNodeException e){
      System.out.println(e);
    }
    return graph;
  }

  public int getWhiteNumOfEdges(){
    return this.whiteNumOfEdges;
  }

  public int getBlackNumOfEdges(){
    return this.blackNumOfEdges;
  }

  /**
		* Identify a network is a win for player (i.e DFS for playerGraph)
  	**/
  public boolean isWin(int color){
    if (color == WHITE) {
      return this.dfsWhite();
    }else if (color == BLACK) {
      return this.dfsBlack();
    }
    return false;
  }

  /**
		* Identify a network is a lise for player (i.e DFS for opponentGraph)
  	**/
  public boolean isLose(int color){
    if (color == WHITE) {
      return this.dfsBlack();
    }else if (color == BLACK) {
      return this.dfsWhite();
    }
    return false;
  }

  /**
		*
		*
  	**/
  private boolean dfsWhite(){
    try{
      DListNode walker = (DListNode)this.whiteGraph.front();
      while(walker.isValidNode()){
        if(((Chip)walker.item()).getX() == 0){
          if (depthFirstSearch(whiteGraph, (Chip)walker.item(), WHITE)){
            return true;
          }
        }
        walker = (DListNode)walker.next();
      }
    }catch(InvalidNodeException e){
      System.out.println(e);
    } 
    return false;
  }

  private boolean dfsBlack(){
    try{
      DListNode walker = (DListNode)this.blackGraph.front();
      while(walker.isValidNode()){
        if(((Chip)walker.item()).getY() == 0){
          if (depthFirstSearch(blackGraph, (Chip)walker.item(), BLACK)){
            return true;
          }
        }
        walker = (DListNode)walker.next();
      }

    }catch(InvalidNodeException e){
      System.out.println(e);
    }
    return false;
  }

  private boolean depthFirstSearch(DList givenGraph, Chip start, int color){
    try{
      DListNode walker = (DListNode)givenGraph.front();
      while(walker.isValidNode()){
        ((Chip)walker.item()).setVisited(false);
        walker = (DListNode)walker.next();
      }
      
      DList stack = new DList();
      stack.insertFront(start);
      while (!stack.isEmpty()){
        Chip v = (Chip)stack.pop();
        if (!v.getVisited()) {
          v.setVisited(true);

          // check if there is a win
          if(v.getX() == 7 && color == WHITE){
            return true;
          }else if (v.getY() == 7 && color == BLACK) {
            return true;
          }

          // for all edges from v to w in Graph put into stack
          walker = (DListNode)v.getEdges().front();
          while(walker.isValidNode()){
            stack.insertFront(walker.item());
            walker = (DListNode)walker.next();
          }
        }
      }
    }catch(InvalidNodeException e){
      System.out.println(e);
    }
    return false;
  }
}
