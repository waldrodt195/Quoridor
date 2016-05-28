package client;

import java.awt.Point;

import org.junit.Before;
import org.junit.Ignore;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertArrayEquals;

/**
 * @author Brandon Williams
 * @ft.date   2/10/2016
 */
@Deprecated
public class WallTest {

  private Wall testWall;

  @Before
  public void setup() {

    testWall = new Wall(2,3,'h');
  }

  @Ignore
  public void testWallConstructor() throws Exception {

    assertNotNull(testWall);
  }

  @Ignore
  public void testWallGetPosition() throws Exception {

    Point[] expectedVals = {new Point(3,2), new Point(4,2)};

    assertArrayEquals(expectedVals, testWall.getPosition());
  }
}
