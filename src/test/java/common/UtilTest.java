package common;

import java.util.Arrays;
import java.awt.Point;

import org.junit.Test;
import org.junit.Before;


import static org.junit.Assert.*;

public class UtilTest{

	private Point point1;
	private Point point2;
	private String testString1;
	private String testString2;
	private String testString3;
	private String testString4;
	private String testString5;
	private String testString6;
	private String testString7;
	private String testString8;
	private String testString9;
	private String testString10;
	private String testString11;
	private String testString12;
	private String testString13;
	private String testString14;
	private String testString15;
    private String testString16;    
	private String testString17; 
	
	@Before
	public void setup() {
		
		//strings to test isValid() on
		testString1 = "TESUJI (1, 3)"; //valid
		testString2 = "TESUJI (7, 9)"; //9 is out of bounds [0-8]
		testString3 = "TESUJI [(2, 6), h]"; //valid
		testString4 = "TESUJI [(8, 0), o]";	// 'o' != 'v'||'h'
		testString5 = "OUWETGEAGEA6969";	// not valid
		testString6 = "TTESUJI [(2, 6), h]";	// not valid
		testString7 = "IAM CANDYSUXX"; //valid
		testString8 = "IAM CANDY SUXX"; //name cannot contain ws
		testString9 = "GAME 2 gin ToNiC"; //valid
		testString10 = "GAME 3 gin ToNiC"; //not valid (3!=1,2)
		testString11 = "GAME 4 shampoo conditioner soap towel"; //valid
		testString12 = "GAME 4 shampoo conditioner soap"; //not valid nameP4?
		testString13 = "ATARI 3 [(2, 5), h]"; //valid
		testString14 = "MYOUSHU"; //valid
		testString15 = "IAM ___-33!#123@@#"; //only ws not allowed in name
        testString16 = ""; //invalid
		testString17 = "GOTE 3";
	}
	
	@Test
	public void testgetMoveString() throws Exception{
			
		Point epoint = new Point(1,3);
		assertTrue(Util.getMoveString(epoint).equals("(1, 3)"));
		assertTrue(Util.getMoveString(epoint, 'h').equals("[(1, 3), h]"));
	}
	
	@Test
	public void testgetCommand() throws Exception {
		assertTrue(Util.getCommand(testString1).equals("TESUJI"));
		assertTrue(Util.getCommand(testString17).equals("GOTE"));
		assertTrue(Util.getCommand(testString13).equals("ATARI"));
		assertTrue(Util.getCommand(testString11).equals("GAME"));
		
		try{//test for exception
			assertTrue(Util.getCommand(testString10).equals("sas"));
		}catch(IllegalArgumentException e){
			//exception has been thrown
			//else test will fail
		}
	}
	
	@Test
	public void testgetCoor() throws Exception {
		assertTrue(Arrays.equals(Util.getCoor(testString13), new int[]{2,5}));
		assertTrue(Arrays.equals(Util.getCoor(testString1), new int[]{1,3}));
	}
	
	
	
	@Test
	public void testisValid() throws Exception {

		assertTrue(Util.isValid(testString9));
		
		assertTrue(Util.isValid(testString1, "TESUJI"));
		
		assertTrue(Util.isValid(testString3));
		assertTrue(Util.isValid(testString3, "TE"));
		
		assertTrue(Util.isValid(testString7));
		assertTrue(Util.isValid(testString7, "IA"));
		
		assertTrue(Util.isValid(testString9));
		assertTrue(Util.isValid(testString9,"GAME"));
		
		try{
		assertTrue(Util.isValid(testString9,"iwjer"));//not a command name
		}catch(IllegalArgumentException e){
			//exception has been thrown
			//else test will fail
		}
		
		try{
		assertTrue(Util.isValid(testString9,"G"));//not a command name
		}catch(IllegalArgumentException e){
			//exception has been thrown
			//else test will fail
		}
		
		assertTrue(Util.isValid(testString11));
		assertTrue(Util.isValid(testString11,"GA"));
		
		
		assertTrue(Util.isValid(testString13));
		
		assertTrue(Util.isValid(testString14));
		
		assertTrue(Util.isValid(testString15));
		assertFalse(Util.isValid(testString11,"TESUJI"));
		
		assertFalse(Util.isValid(testString2));
		assertFalse(Util.isValid(testString2, "TESU"));
		
		assertFalse(Util.isValid(testString4));
		assertFalse(Util.isValid(testString5));
		assertFalse(Util.isValid(testString6));
		assertFalse(Util.isValid(testString8));
		assertFalse(Util.isValid(testString10));
		assertFalse(Util.isValid(testString12));
        assertFalse(Util.isValid(testString16));

		
	}
	
}
