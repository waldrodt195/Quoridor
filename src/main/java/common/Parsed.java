package common;

import java.util.Arrays;
import java.awt.Point;


/*
The idea is to create a Parsed object with a string parameter
The string will be parsed and the relevant information will be 
accessible through the object's public fields.

*/


@Deprecated
public class Parsed{
		public boolean valid; //if command is valid
		public String commandName; //command name
		public int pnumber; //player number
		public String pname; //used for IAM
		public String p1; public String p2;//player names
		public String p3; public String p4;
		public int c; public int r; // c=column; r=row
		public boolean isWall; //if wall true
		public Point endPos; // point of position, wall or pawn
		public char wallPos; // returns h for horiz or v
		

		

	
	//parses
		public Parsed(String input){
			
			
			if(!Util.isValid(input)){
				this.valid=false;
				System.out.println("Testing input: " + input);	
			}else{
			
				this.valid=true;
				String[] spl = input.split(" ");
				
				switch(spl[0]){
					case "IAM":
						this.pname = spl[1];
						this.commandName = "IAM";
						break;
					
					case "GAME":
						this.commandName = "GAME";
						this.p1 = spl[2];
						this.p2 = spl[3];
						
						if(spl.length>4){
							this.p3 = spl[4];
							this.p4 = spl[5];
						}
					break;
					
					case "TESUJI":
						this.commandName = "TESUJI";
						if(spl.length==4){
							this.isWall = true;
							this.c = Util.getCoor(input)[0];
							this.r = Util.getCoor(input)[1];
							this.wallPos = spl[3].charAt(0);
							this.endPos = new Point(c,r);
							
						}else{
							this.isWall = false;
							this.c = Util.getCoor(input)[0];
							this.r = Util.getCoor(input)[1];
							this.endPos = new Point(c,r);
							
						}
					break;
					
					case "ATARI":
						this.commandName = "ATARI";
						if(spl.length==5){
							this.isWall = true;
							this.pnumber=Integer.parseInt(spl[1]);
							this.c = Util.getCoor(input)[0];
							this.r = Util.getCoor(input)[1];
							this.wallPos = spl[4].charAt(0);
							this.endPos = new Point(c,r);
											
						}else{
							this.isWall = false;
							this.pnumber=Integer.parseInt(spl[1]);
							this.c = Util.getCoor(input)[0];
							this.r = Util.getCoor(input)[1];
							this.endPos = new Point(c,r);
							
						}
					break;
					
					case "GOTE":
						this.commandName = "GOTE";
						this.pnumber = Integer.parseInt(spl[1]);
					break;  
						
					case "KIKASHI":
						this.commandName = "KIKASHI";
						this.pnumber = Integer.parseInt(spl[1]);		
					break;	  
					
					default:
				}
			}
			
		}
		
	}
