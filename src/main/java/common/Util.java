package common;


import java.awt.Point;
import java.util.regex.Pattern;


public class Util{
	
	//all regex values for protocol's command strings
	public static final String[] regexValues = 
					
					{"IAM [^\\s]+",
					"GAME [12] [^\\s]+ [^\\s]+",
					"GAME [1-4] [^\\s]+ [^\\s]+ [^\\s]+ [^\\s]+",
					"TESUJI \\[\\([0-8], [0-8]\\), [vh]\\]",
					"TESUJI \\([0-8], [0-8]\\)",
					"ATARI [1-4] \\[\\([0-8], [0-8]\\), [vh]\\]",
					"ATARI [1-4] \\([0-8], [0-8]\\)",
					"GOTE [1-4]",
					"KIKASHI [1-4]",
					"MYOUSHU"};
	
	public static final String[] commands = 
	{"IAM","GAME","TESUJI","ATARI","GOTE","KIKASHI","MYOUSHU"};
					
					

	//simple valid function, takes string to test as arg
	//returns true if valid, false if not
	public static boolean isValid(String toTest){
	
	for(String regex : regexValues){
			if(Pattern.matches(regex, toTest))
				return true;
		}
		
		return false;
	}
	
	//param: command that toTest has to be,
	//only two letters necessary, (T for TESUJI)
	//returns true if valid and corresponds to command
	//returns false if not
	public static boolean isValid(String toTest, String command)
								throws IllegalArgumentException{
	
		boolean err = true;
		
		for(String c : commands){
			if(c.contains(command)) {
				err=false;
			}
		}
		
		if(err||command.length()==1)
			throw new IllegalArgumentException(command +" is not a valid command name");

	
		if(isValid(toTest)){
			return command.charAt(1)==toTest.charAt(1);
		}else{
			return false;
		}
		
	}
	

	
	//inputs move;
	//outputs coordinates in int array: [c,r]
	//as move not position
	public static int[] getCoor(String input)
				throws IllegalArgumentException{
		
		int i = 9;
		int j = 8;
		int col;
		int row;
		
		
		if(!(getCommand(input).equals("ATARI")||
		getCommand(input).equals("TESUJI")))
			throw new IllegalArgumentException
			("no moves here...");
			
		if(getCommand(input).equals("ATARI")){
			i++;
			j++;
		}
		
		if(input.length()<15){
			col = Integer.parseInt(String.valueOf(input.charAt(j)));
			row = Integer.parseInt(String.valueOf(input.charAt(j+3)));
		}else{
		col = Integer.parseInt(String.valueOf(input.charAt(i)));
		row = Integer.parseInt(String.valueOf(input.charAt(i+3)));
		}
		
		int[] ret = {col,row};
		return ret;
	}
	
	
	//arg: input String corresponding to command
	//returns: what command is it
	//throws IllegalArgumentException if invalid
	public static String getCommand(String input)
				throws IllegalArgumentException{
		
		String ret ="";
		boolean err=true;
		
		for(int i=0; i<commands.length;i++){
			if(isValid(input, commands[i])){
			
				ret = commands[i];
				err = false;
			}
		}
		
		
		if(err)
			throw new IllegalArgumentException
							("Input string invalid");
		
		return ret;
		
		
		
	}
	
	
	/*
		usage: getMoveString will return the move string associated
		with a wall or pawn position.

		getMoveString(int c, int r, char wall)  --> wall char should be 'v' or 'h'
													corresponding with wall position
												    if anything else assumes pawn
		getMoveString(Point position, char wall) --> //
		
		getMoveString(Point position) --> assume pawn
		getMoveString(int c, int r)   --> assume pawn
	*/
	
	public static String getMoveString(int c, int r){
		
		return getMoveString(c, r, 'w');
		
		
	}
	
	public static String getMoveString(Point position){
		
		return getMoveString((int)position.getX(), (int)position.getY(), 'w');
		
		
	}
	
	
	public static String getMoveString(Point position, char wall){
		
		return getMoveString((int)position.getX(), (int)position.getY(), wall);
		
		
	}
	
	public static String getMoveString(int c, int r, char wall){
		
		String ss = "";
		
		if(wall=='h'||wall=='v'){
			ss= "[(" + c + ", " + r + "), " + wall + "]"; 
		}else{
			ss= "(" + c + "," + " " + r + ")";
		}
		
		if(isValid(ss))
			throw new IllegalArgumentException("position not valid");
		
		return ss;
	}

					
}
