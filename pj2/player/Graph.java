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
      return this.dfsWhite();
    }else if (color == BLACK) {
      return this.dfsBlack();
    }
    return false;
  }

  /**
		* Identify a network is a lise for player (i.e bfs for opponentGraph)
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
        if(((Chip)walker.item()).isStartGoal()){
          if (depthFirstSearch(whiteGraph, (Chip)walker.item())){
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
        if( ((Chip)walker.item()).isStartGoal() ){
          if (depthFirstSearch(blackGraph, (Chip)walker.item())){
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

  private boolean depthFirstSearch(DList givenGraph, Chip start){
    try{
      DListNode walker = (DListNode)givenGraph.front();
      while(walker.isValidNode()){
        ((Chip)walker.item()).setVisited(false);
        walker = (DListNode)walker.next();
      }
    }catch(InvalidNodeException e){
      System.out.println(e);
    }
    return depthFirstSearch(null, start, 0);
  }

  private boolean depthFirstSearch (Chip prev, Chip start, int depth){
    if (depth >= 9){
      return false;
    }
    boolean isfind = false;
    
    start.setVisited(true);
    try{
      DListNode walker = (DListNode)start.getEdges().front();
      while(walker.isValidNode()){
        Chip child = (Chip)walker.item();

        if(!this.isStright(prev, start, child)){
          if(depth >= 4 && child.isEndGoal()){
            return true;
          }else if (!child.isEndGoal() && !child.isStartGoal()) {
            if (child.getVisited() == false){
              isfind = isfind || depthFirstSearch(start, child, depth + 1);
              if (isfind == true){
                return true;
              }
            }            
          }
        }
        walker = (DListNode) walker.next();
      }
    }catch(InvalidNodeException e){
      System.out.println(e);
    }
    return false;
  }

  private boolean isStright(Chip prev, Chip cur, Chip next){
    if (prev == null){
      return false;
    }

    if( prev.getX() == cur.getX() && cur.getX() == next.getX()){
      return true;
    }else if (prev.getY() == cur.getY() && cur.getY() == next.getY()) {
      return true;
    }
    else if ( ( prev.getX() - cur.getX() == prev.getY() - cur.getY() && cur.getX() - next.getX() == cur.getY() - next.getY() ) ||
              ( prev.getX() - cur.getX() == cur.getY() - prev.getY() && cur.getX() - next.getX() == next.getY() - cur.getY() ) ){
      return true;
    }else{
      return false;
    }    
  }

  public String toString(){
    String out = "";
    out += "WHITE GRAPH\n==========================\n";
    return out;
  }


/*
  private boolean depthFirstSearch(DList givenGraph, Chip start, int color){
    try{
      DListNode walker = (DListNode)givenGraph.front();
      while(walker.isValidNode()){
        ((Chip)walker.item()).setVisited(false);
        ((Chip)walker.item()).setDepth(-1);
        ((Chip)walker.item()).setPrev(null);
        walker = (DListNode)walker.next();
      }
      
      start.setDepth(0);
      DList stack = new DList();
      stack.insertFront(start);
      while (!stack.isEmpty()){
        Chip v = (Chip)stack.pop();

        if (!v.getVisited()) {
          v.setVisited(true);

          // for all edges from v to w in Graph put into stack
          walker = (DListNode)v.getEdges().front();
          while(walker.isValidNode()){
            if (v.isStright((Chip)walker.item())) {      
              // reach other goal at least through 6 nodes
              if(v.getDepth() >= 4 && ((Chip)walker.item()).isEndGoal()){
                return true;
              }else if (!((Chip)walker.item()).isEndGoal()) {
                if (!((Chip)walker.item()).getVisited()) {
                  ((Chip)walker.item()).setPrev(v);
                  ((Chip)walker.item()).setDepth(v.getDepth() + 1);
                  stack.insertFront(walker.item());
                }              
              }              
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

/*
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

*/  
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
