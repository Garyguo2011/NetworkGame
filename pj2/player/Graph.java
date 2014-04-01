/* Graph.java */

package player;
import list.*;
import dict.*;

//================================================================================
// (4) determineing whether a game board contains any networks for a given player
//================================================================================
/**
	* NetworkIdentifier, class that combine all connects into a graph. and bfs,
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
  	* construct a graph that will be used for bfs.
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
		* Identify a network is a win for player (i.e bfs for playerGraph)
  	**/
  public boolean isWin(int color){
    if (color == WHITE) {
      return this.bfsWhite();
    }else if (color == BLACK) {
      return this.bfsBlack();
    }
    return false;
  }

  /**
		* Identify a network is a lise for player (i.e bfs for opponentGraph)
  	**/
  public boolean isLose(int color){
    if (color == WHITE) {
      return this.bfsBlack();
    }else if (color == BLACK) {
      return this.bfsWhite();
    }
    return false;
  }

  /**
		*
		*
  	**/
  private boolean bfsWhite(){
    try{
      DListNode walker = (DListNode)this.whiteGraph.front();
      while(walker.isValidNode()){
        if(((Chip)walker.item()).getX() == 0){
          if (breadthFirstSearch(whiteGraph, (Chip)walker.item(), WHITE)){
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

  private boolean bfsBlack(){
    try{
      DListNode walker = (DListNode)this.blackGraph.front();
      while(walker.isValidNode()){
        if(((Chip)walker.item()).getY() == 0){
          if (breadthFirstSearch(blackGraph, (Chip)walker.item(), BLACK)){
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

/*
  private boolean breadthFirstSearch(DList givenGraph, Chip start, int color){
    try{
      DListNode walker = (DListNode)givenGraph.front();
      while(walker.isValidNode()){
        ((Chip)walker.item()).setVisited(false);
        ((Chip)walker.item()).setPrev(null);
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
            if(this.trackBackStep(v) >= 6){
              return true;
            }else{
              v.setVisited(false);
              continue;
            }

          }else if (v.getY() == 7 && color == BLACK) {
            if(this.trackBackStep(v) >= 6){
              return true;  
            }else{
              v.setVisited(false);
              continue;
            }
            
          }

          // for all edges from v to w in Graph put into stack
          walker = (DListNode)v.getEdges().front();
          while(walker.isValidNode()){

            if (!((Chip)walker.item()).getVisited()) {
              ((Chip)walker.item()).setPrev(v);
              stack.insertFront(walker.item());  
            }
            walker = (DListNode)walker.next();
          }
        }
      }
    }catch(InvalidNodeException e){
      System.out.println(e);
    }
    return false;
  }
*/

  private boolean breadthFirstSearch(DList givenGraph, Chip start, int color){
    try{
      DListNode walker = (DListNode)givenGraph.front();
      while(walker.isValidNode()){
        ((Chip)walker.item()).setVisited(false);
        ((Chip)walker.item()).setPrev(null);
        ((Chip)walker.item()).setDist(-1);
        walker = (DListNode)walker.next();
      }
      
      DList queue = new DList();

      start.setDist(0);
      queue.insertBack(start);
      while (!queue.isEmpty()){
        Chip v = (Chip)queue.pop();

        // for all edges from v to w in Graph put into stack
        walker = (DListNode)v.getEdges().front();
        while(walker.isValidNode()){
          if (!isStright(v, (Chip)walker.item())){
            if(v.getDist() < 5 && ((Chip)walker.item()).getDist() == -1 && !this.isEndGoal((Chip)walker.item(), color)){
              ((Chip)walker.item()).setDist(v.getDist() + 1);
              ((Chip)walker.item()).setPrev(v);
              queue.insertBack(walker.item());
            }else if (v.getDist() >= 5 && this.isEndGoal((Chip)walker.item(), color)) {
              return true;
            }
          }
          walker = (DListNode)walker.next();
        }
      }
    }catch(InvalidNodeException e){
      System.out.println(e);
    }
    return false;
  }

  private boolean isEndGoal(Chip chip, int color){
    return (chip.getX() == 7 && color == WHITE) || (chip.getY() == 7 && color == BLACK);
  }

  private boolean isStright(Chip v1, Chip v2){
    if (v1.getPrev() == null){
      return false;

    }

    if( v1.getPrev().getX() == v1.getX() && v1.getX() == v2.getX()){
      return true;
    }else if (v1.getPrev().getY() == v1.getY() && v1.getY() == v2.getY()) {
      return true;
    }
    else if ((v1.getPrev().getX() - v1.getX() == v1.getPrev().getY() - v1.getY() &&
               v1.getX() - v2.getX() == v1.getY() - v2.getY()) ||
             (v1.getPrev().getX() - v1.getX() == v1.getY() - v1.getPrev().getY() &&
               v1.getX() - v2.getX() == v2.getY() - v1.getY()) ){
      return true;
    }else{
      return false;
    }
  }

/*
  private int trackBackStep (Chip start){
    int step = 0;
    if (start == null){
      return step;
    }
    Chip walker = start;
    while(walker.getPrev() != null){
      step++;
      walker = walker.getPrev();
    }
    return step;
  }
*/

}
