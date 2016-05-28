package common;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Jade Kevin Betsami
 * @ft.edited Brandon Williams
 * @ft.date   04/7/2016
 */

public class InterpreterTest {


  @Test
  public void testParseString() throws Exception {

    assertTrue("Tesuji validation error",
        Interpreter.isValidString("TESUJI (1, 3)"));

    assertTrue("Tesuji validation error",
        Interpreter.isValidString("TESUJI (1,9)"));

    assertTrue("Tesuji validation error",
        Interpreter.isValidString("TESUJI [(2, 6), h]"));

    assertTrue("Tesuji validation error", 
        Interpreter.isValidString("TESUJI [(5,4),v]"));

    assertTrue("Atari validation error",
        Interpreter.isValidString("ATARI 1 (1, 3)"));

    assertFalse("Atari validation error",
        Interpreter.isValidString("ATARI 5 (1, 3)"));

    assertTrue("Atari validation error",
        Interpreter.isValidString("ATARI 3 [(1, 3), h]"));
    
    assertTrue("Atari validation error",
        Interpreter.isValidString("ATARI 3 [(1, 3), h]"));
    
  }
}