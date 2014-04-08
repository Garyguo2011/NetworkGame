/* Graph.java */

package player;
import list.*;

/**
 *
 * Graph class Store all connections of a game board into a doubly linked list of each color, each node is a 
 * chip object so that we can perform DFS to find all the networks. It also knows which board it corresponds 
 * to.
 *
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
    * @param board the board that used to build graph
    * @return a graph that contain all connection
  	**/
  public Graph(Board board){
  	this.board = board;
    this.whiteNumOfEdges = 0;
    this.blackNumOfEdges = 0;
    this.whiteGraph = this.buildGraph(WHITE);
  	this.blackGraph = this.buildGraph(BLACK);
  }

  /**
   *
   * buildGraph(int color) obtains a doubly linked list that is the graph of the input color in the board.
   *
   * @param color specifies to build the graph
   * @return graph a doubly linked list that contains the graph of the input color
   *
   **/
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
   *
   * isWin(int color) determines whether chips of the input color can win in the current board
   *
   * @param color specifies which side of chips needs to be determined.
   * @return true if the chips of the input color can win in the current board, and false otherwise
   *
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
   *
   * isLose(int color) determines whether chips of the input color will lose (opponent win) in the current   * board
   *
   * @param color specifies which side of chips needs to be determined.
   * @return true if the chips of the input color will lose in the current board, and false otherwise
   *
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
   * dfsWhite() finds all the chips in WHITE color's starting goal area and start implementing DFS to        * determine if there is network in the board
   *
   * @return true if there is a network of WHITE color in the current board, and false otherwise
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

  /**
   *
   * dfsBlack() finds all the chips in BLACK color's starting goal area and start implementing DFS to        * determine if there is network in the board
   *
   * @return true if there is a network of BLACK color in the current board, and false otherwise
   *
   *
   **/
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

  /**
   *
   * depthFirstSearch(DList givenGraph, Chip start) is the initialization of the DFS (set all chip in the    * graph to unvisited).
   *
   * @param givenGraph is a doubly linked list contains all the chips in the current board
   * @param start is a chip at the starting goal area.
   * @return the result of the actual implementation of DFS (next method)
   *
   **/
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


  /**
   *
   * depthFirstSearch (Chip prev, Chip start, int depth) is the actual implementation of DFS which process   * the graph recursively.
   *
   * @param prev is the previous processed chip in DFS (father of the processing chip)
   * @param start is the current processing chip
   * @param depth is the DFS search level for each recursive process.
   * @return true if DFS can find a network in the graph, and false otherwise.
   *
   **/
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
    start.setVisited(false);
    return false;
  }

  /**
   * isStright(Chip prev, Chip cur, Chip next) determines whether the three input chips form a straight line.
   *
   * @param prev is the first input chip
   * @param cur is the second (middle) input chip
   * @param next is the third input chip
   * @return true if the three input chips form a straight line (both orthogonal and diagonal), and false    * otherwise
   *
   **/
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
}
