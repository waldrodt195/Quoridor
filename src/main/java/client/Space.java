package client;

import java.awt.Point;
import java.util.HashSet;

@SuppressWarnings("serial")
public class Space extends Point{
  
  // variables can be accessed by calling them
  // thus we do not need getters.
  public HashSet<Space> edges;
  public boolean occupied;
  public int distance; //used for pathing (distance between this node and source)
  public Space prev; //used for pathing
  /**
   * constructor
   * 
   * @param x - the x-coordinate of the space
   * @param y - the y-coordinate of the space
   */
  public Space(int x, int y) {
    super(x,y);

    edges = new HashSet<Space>(4);
    occupied = false;
  
    //pathing stuff
    distance = Integer.MAX_VALUE; //make this close to inf for pathfinding
    prev = null;
  }

	
	public String toString() {
      return String.format("(%d, %d) : %s", x, y, occupied ? "Y" : "N");
	}
  
}// end of class
