package common;

import java.util.Arrays;
import java.awt.Point;

import org.junit.Test;
import org.junit.Before;


import static org.junit.Assert.*;

public class ParsedTest{

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
	private Parsed newParsed1;
	private Parsed newParsed2;
	private Parsed newParsed3;
	private Parsed newParsed4;
	
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
	public void testValid() throws Exception{
	
		newParsed1 = new Parsed(testString1);
		Point testPoint1 = new Point(1,3);
		assertTrue(newParsed1.valid);
		assertFalse(newParsed1.isWall);
		assertTrue(newParsed1.endPos.equals(testPoint1));
		assertTrue(newParsed1.c==1);
		
		newParsed2 = new Parsed(testString11);
		assertTrue(newParsed2.valid);
		assertTrue(newParsed2.p3.equals("soap"));
		
		newParsed3 = new Parsed(testString3);
		Point testPoint3 = new Point(2,6);
		assertTrue(newParsed3.valid);
		assertTrue(newParsed3.isWall);
		assertTrue(newParsed3.endPos.equals(testPoint3));
		assertTrue(newParsed3.wallPos=='h');
		
		newParsed4 = new Parsed(testString5);
		assertFalse(newParsed4.valid);
	}
	
}
