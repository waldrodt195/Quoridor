package client;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Brandon Williams
 * @ft.date   2/21/2016 - Last updated
 */
public class GameBoardTest {

  private GameBoard gb;

  @Before //This runs when the class is initialized
  public void setup() {

    gb = GameBoard.getInstance();
  }

  @Test
  public void testGameBoardConstructor() throws Exception {

    assertNotNull(gb);
  }

  @Test
  public void testGetGameBoard() throws Exception {

    ArrayList<Space> board = gb.getGameBoard();

    assertNotNull( board );

    //Also want to check if the board is of the correct size
    assertEquals("Board is of worng size.", 81, board.size());

    //Check to see that all the elements are not null
    for( int i = 0; i < board.size(); i++ ) {

      assertNotNull( "Index " + i + " of board is null" );
    }
  }
}
