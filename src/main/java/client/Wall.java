package client;

import java.awt.Point;

@Deprecated
public class Wall implements GamePiece {

  private Point[] position;

  /**
   * Construts a wall that takes up two spaces. If it is a horizontal wall
   * it is placed at the given coordinates (x,y) and at (x+1, y) 
   * preventing movement from (x,y) to (x, y+1) and from (x+1, y) 
   * to (x+1, y+1).If it is a vertical wall it is placed at the given 
   * coordinates (x,y) and at (x, y+1) preventing movement from (x,y) 
   * to (x+1, y) and from (x, y+1) to (x+1, y+1)
   * 
   * @param y - The column coordinate to place the wall
   * @param x - The row coordiante to place the wall
   * @param direction -<p> Must be 'v' or 'h'. 
   * Indicates whether it is a vertical or horizontal wall</p>
   */
  public Wall(int y, int x, char direction) {

    position = new Point[2];

    position[0] = new Point(x, y);

    if(direction == 'v')
      position[1] = new Point(x, y+1);

    else if(direction == 'h')
      position[1] = new Point(x+1, y);
  }

  /**
   * @return the position of the wall
   */
  public Point[] getPosition() {

    return position;
  }
}
