package client;

import java.awt.Point;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Matchers;
import org.mockito.Mockito;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;

/**
 * @author Brandon
 * @ft.date 5/1/16
 * @ft.edited Brandon
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Player.class)
public class PlayerTest {
    

    private Player player;
    private Player p2;
    
    @Before //This runs before every test
    public void setup() {

        player = PowerMockito.spy(new Player(1, 10));
        p2 = PowerMockito.spy(new Player(2, 10));
    }

    @After
    public void tearDown() {

        player.resetBoard();
    }
    
    @Test
    public void testPlayerConstructor() throws Exception {
    
        Assert.assertNotNull(player);

        int playerNumber = Whitebox.getInternalState(player, "playerNumber");
        Assert.assertEquals(1, playerNumber);

        int wallCount = Whitebox.getInternalState(player, "wallCount");
        Assert.assertEquals(10, wallCount);
    }

    @Test
    public void testMovePawn() throws Exception {
        
        /* System.out.println("Move Pawn Test");
        
        System.out.printf("\np1: %s\n"
        + "\np2: %s\n", player, p2);  */

        Assert.assertEquals("ATARI", player.movePawn(4,1));
        Assert.assertEquals("ATARI", player.movePawn(3,1));
        Assert.assertEquals("ATARI", player.movePawn(3,0));
        Assert.assertEquals("ATARI", player.movePawn(4,0));
    }

    @Test
    public void testPawnJump() throws Exception
    {    
        /*System.out.println("Test Pawn Jump\n");

        System.out.printf("\np1: %s\n"
        + "\np2: %s\n", player, p2);  */
        
        //Move pawns so they are next to each other
        for (int i = 1; i < 4; i++) {

            Assert.assertEquals("ATARI",player.movePawn(4, i));
            Assert.assertEquals("ATARI",p2.movePawn(4, 8-i));
        }
        Assert.assertEquals("ATARI", player.movePawn(4,4));

        //Test player pawn jumps
        Assert.assertEquals("ATARI", player.movePawn(4,6));    
        Assert.assertEquals("ATARI", player.movePawn(5,5));
        Assert.assertEquals("ATARI", player.movePawn(3,5));
        Assert.assertEquals("ATARI", player.movePawn(4,4));

        //Test p2 pawn jumps
        Assert.assertEquals("ATARI", p2.movePawn(4,3));
        Assert.assertEquals("ATARI", p2.movePawn(3,4));
        Assert.assertEquals("ATARI", p2.movePawn(4,5));
        Assert.assertEquals("ATARI", p2.movePawn(5,4));
    }

    @Test
    public void testHasWon() throws Exception {

        // System.out.println("Has Won Test");  
    
        String actualResult = "";

        //move to win position
        for(int i = 1; i < 8; i++) {

            actualResult = player.movePawn(4, i);
        }

        //must move to this position because of player 2
        Assert.assertEquals("KIKASHI", player.movePawn(3,8));
    }

    @Test
    public void testPlaceWall() throws Exception{

        // Test that you cannot place walls at the bottom or right 
        // edge of the board
        Assert.assertEquals("GOTE", player.placeWall(4,8,'v'));
        Assert.assertEquals("GOTE", player.placeWall(4,8,'h'));
        Assert.assertEquals("GOTE", player.placeWall(8,4,'v'));
        Assert.assertEquals("GOTE", player.placeWall(8,4,'h'));

        int actualWallCount = Whitebox.getInternalState(player, "wallCount");
        Assert.assertEquals(10, actualWallCount);


        Assert.assertEquals("ATARI", player.placeWall(4,0,'h'));

        actualWallCount = Whitebox.getInternalState(player, "wallCount");
        Assert.assertEquals(9, actualWallCount);

        Assert.assertEquals("ATARI", player.placeWall(5,1,'v'));
        Assert.assertEquals("ATARI", player.placeWall(3, 0, 'v'));
        Assert.assertEquals("ATARI", player.placeWall(6, 1, 'h'));
    

        actualWallCount = Whitebox.getInternalState(player, "wallCount");
        Assert.assertEquals(6, actualWallCount);

        //Test that you cannot place intersecting walls
        Assert.assertEquals("GOTE", player.placeWall(4,0,'v'));
        //or the same wall
        Assert.assertEquals("GOTE", player.placeWall(4,0,'h'));
        //or an overlapping wall
        Assert.assertEquals("GOTE", player.placeWall(5,0,'h'));
        Assert.assertEquals("GOTE", player.placeWall(3,0,'h'));
        Assert.assertEquals("GOTE", player.placeWall(5,0,'v'));
        //Wall cuts off path to end
        Assert.assertEquals("GOTE", player.placeWall(7, 0, 'v'));

        actualWallCount = Whitebox.getInternalState(player, "wallCount");
        Assert.assertEquals(6, actualWallCount);
     
	/*        PowerMockito.verifyPrivate(player, Mockito.times(14))
		  .invoke("isValidWallPlacement", Matchers.any(), Matchers.anyChar());*/
    }
}
