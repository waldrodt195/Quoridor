package client;

import java.awt.Point;

/**
 * @author Brandon Williams
 * @ft.date   2/10/2016
 */
@Deprecated 
public class Pawn implements GamePiece {

  private Point position;

  /**
   * Constructs a pawn for the player at the given coordinates.
   *
   * @param y - the column coordinate of the pawn.
   * @param x - the row coordinate of the pawn.
   */
  public Pawn(int y, int x) {

    position = new Point(x, y); 
  }

  /**
   * @return The position of the Pawn
   */ 
  public Point[] getPosition() {

    return new Point[]{null, position};
  }
}
