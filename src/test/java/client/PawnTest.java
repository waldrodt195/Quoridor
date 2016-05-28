package client;

import java.awt.Point;

import org.junit.Ignore;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertArrayEquals;

/**
 * @author Brandon Williams
 * @ft.date   2/10/2016
 */
@Deprecated
public class PawnTest {

  private Pawn testPawn;

  @Ignore
  public void setup() {

    testPawn = new Pawn(2,3);
  }

  @Ignore
  public void testPawnConstructor() throws Exception {

    assertNotNull(testPawn);
  }

  @Ignore
  public void testGetPosition() throws Exception {

    Point[] expectedVals = {null, new Point(3,2)};

    assertArrayEquals(expectedVals, testPawn.getPosition());
  }
}
