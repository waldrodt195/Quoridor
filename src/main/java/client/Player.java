package client;

import java.awt.Point;
import java.util.ArrayList;

public class Player {


    private static GameBoard gameBoard;
    private int playerNumber;

    public int wallCount;

    private static boolean isFirstInstance = true;

    //Keeps track of all players win positions 
    private static ArrayList<Point>[] winPositions;
 

    //Keeps track of all players pawn positions
    private static Point[] pawnPos;


    /**
     * 
     * @param playerNumber The number of the player
     * @param wallCount How many walls is the player allowed to place
     */
    @SuppressWarnings("unchecked")
    public Player (int playerNumber, int wallCount) {

        this.wallCount = wallCount;
        this.playerNumber = playerNumber;

        if(isFirstInstance) {

            isFirstInstance = false;

            if(wallCount == 5){

                pawnPos = new Point[4];
                winPositions = (ArrayList<Point>[]) new ArrayList[4];
            }

            else if( wallCount == 10 ) {

                pawnPos = new Point[2];
                winPositions = (ArrayList<Point>[]) new ArrayList[2];

            }

            for(int i = 0; i < winPositions.length; i++)
                winPositions[i] = new ArrayList<Point>(9);

            Player.gameBoard = new GameBoard();
        }
         
        setInitPosWinPos();
    }

    /**
     *
     * @param column - The column position that the pawn
     *        will move to
     *
     * @param row - The row position that the pawn will
     *        move to.
     *
     * @return The opCode to be sent to the servers. 
     *         If a valid move was made "ATARI" is
     *         returned. "KIKASHI" if it is a winning
     *         move or "GOTE" if it was an illegal
     *         move
     */
    public String movePawn(int column, int row) {

        Space movePos = new Space(column, row);

        if(!isValidMove(movePos)){

            gameBoard.removePawn(pawnPos[playerNumber - 1]);

            return "GOTE";
        }

        gameBoard.movePawn(pawnPos[playerNumber - 1], movePos);

        pawnPos[playerNumber - 1] = movePos;

        return hasWon();
    }

    public String placeWall(int column, int row, char direction) {

        Point placementPos = new Point(column, row);

	if(wallCount < 1)
	    return "GOTE";

        if(!isValidWallPlacement(placementPos, direction)){
            gameBoard.removePawn(pawnPos[playerNumber - 1]);
            return "GOTE";
        }
        gameBoard.placeWall(placementPos, direction);

        wallCount--;

        return "ATARI";
    }

    /**
     * @return the position of the current player
     */
    public Point getCurrentPos(){
        return pawnPos[playerNumber - 1];
    }


    /**
     * For testing purposes only
     */
    public void resetBoard() {

        isFirstInstance = true;
    }
 
    @Override
    public String toString(){

        return String.format("Player number: %s\n"
                             + "Win Positions: %s\n"
                             + "Current Position: %s", playerNumber,
                             winPositions[playerNumber-1], pawnPos[playerNumber-1]);
    }

    /**
     * Sets the initial position of the pawn based on
     * on the player number
     */
    private void setInitPosWinPos() {

        Point initPoint = null;

        if(playerNumber == 1 && pawnPos[0] != new Point(4,0)) {
            pawnPos[0] = initPoint =new Point(4, 0);
            gameBoard.placePawn(initPoint);

            for(int i = 0; i < 9; i++)
                winPositions[0].add(new Point(i, 8));
        }
        else if(playerNumber == 2 && pawnPos[1] != new Point(4,8))  {
            pawnPos[1] = initPoint = new Point(4, 8);
            gameBoard.placePawn(initPoint);

            for(int i = 0; i < 9; i++)
                winPositions[1].add(new Point(i, 0));

        }
        else if(playerNumber == 3 && pawnPos[2] != new Point(0,4)) {
            pawnPos[2] = initPoint = new Point(0, 4);
            gameBoard.placePawn(initPoint);

            for(int i = 0; i < 9; i++)
                winPositions[2].add(new Point(8, i));

        }
        else if(playerNumber == 4 && pawnPos[3] != new Point(8,4)) {
            pawnPos[3] = initPoint = new Point(8, 4);
            gameBoard.placePawn(initPoint);

            for(int i = 0; i < 9; i++)
                winPositions[3].add(new Point(0, i));

        }

        //System.out.printf("\n%s\n\n", this);
    }

    /**
     * Checks if the requested position is a valid move
     *
     * @param movePos The requested move to validate
     */
    private boolean isValidMove(Space movePos) {

        /*System.out.printf("Current position for player %s: %s\n\n", 
                          playerNumber, pawnPos[playerNumber - 1]);

                          System.out.println("Position to move to: " + movePos);*/

        ArrayList<Space> validMoves = 
            getValidMoves(gameBoard.getSpaceAt(pawnPos[playerNumber - 1].x, 
                                               pawnPos[playerNumber - 1].y), 
                          new ArrayList<Space>(10), 
                          new ArrayList<Space>(4) );

        /*System.out.printf("All valid positions for player %d are: %s\n\n", 
          playerNumber, validMoves);  */

        if(validMoves.contains(movePos)) 		
            return true;

        return false;
    }

    /**
     * 
     * @param currentPos
     * @param validPos
     * @param visitedSpaces
     * @return
     */
    private ArrayList<Space> getValidMoves(Space currentPos, 
                                           ArrayList<Space> validPos, 
                                           ArrayList<Space> visitedSpaces) {

        visitedSpaces.add(currentPos);

        for(Space spc : currentPos.edges) {

            //System.out.printf("%s\n%s\n%s\n\n", currentPos, validPos, visitedSpaces);
            // Haven't visited yet
            if( !visitedSpaces.contains(spc)) {

                //System.out.printf("Space: %s unvisited\n", spc);

                if(spc.occupied){
                    //System.out.printf("Space: %s is occupied\n", spc);
                    getValidMoves(spc, validPos, visitedSpaces);
                }
                else
                    //System.out.printf("Space: %s is valid position\n", spc);
                    validPos.add(spc);
            }

        }

        return validPos;
    }

    /**
     * @param wallPos The position to place the wall on the board
     * @param direction The direction of the wall
     * @return False if the wall placed is intersecting another
     * wall or the same wall is placed. Else, return true.
     */
    public static boolean isValidWallPlacement(Point wallPos, char direction) {


        if (wallPos.x >= 8 || wallPos.y >= 8) {
         
            System.err.println("Attempted to place a wall off the board.");

            return false;
        }

        try { 

            Space[] spaces = new Space[] { 
                gameBoard.getSpaceAt(wallPos.x, wallPos.y), // the current space
                gameBoard.getSpaceAt(wallPos.x+1, wallPos.y), // the space to the right
                gameBoard.getSpaceAt(wallPos.x, wallPos.y+1), //the space below 
                gameBoard.getSpaceAt(wallPos.x+1, wallPos.y+1), //the space to the right and below
            };

            if( direction == 'v' ) { // A vertical wall

                System.out.println("Checing Vertical Wall");

                // If this position and the position above or below of it already has a 
                // vertical wall an invalid move was made.

                if( !spaces[0].edges.contains(spaces[1]) || 
                    spaces[0].edges.contains(spaces[1]) && 
                    !spaces[2].edges.contains(spaces[3]) ) {

                    System.out.println("Bad Move! Overlapping  wall placed");

                    return false;
                }

                //Check for intersecting Walls
                else if( !spaces[0].edges.contains(spaces[2]) && 
                         !spaces[1].edges.contains(spaces[3]) ) {

                    System.out.println("Possible intersection found");

                    Space[] spacesToCheck = {

                        gameBoard.getSpaceAt(spaces[0].x - 1, spaces[0].y),
                        gameBoard.getSpaceAt(spaces[0].x - 1, spaces[0].y + 1),
                        gameBoard.getSpaceAt(spaces[1].x + 1, spaces[1].y),
                        gameBoard.getSpaceAt(spaces[1].x + 1, spaces[1].y + 1)
                    };

                    for(Space s : spacesToCheck) {

                        System.out.println(s);
                        System.out.println(s.edges + "\n");
                    }

                    // If the two spaces are not two different walls
                    // then it is an intersecting wall
                    if( spacesToCheck[0].edges.contains(spacesToCheck[1]) ||
                        spacesToCheck[2].edges.contains(spacesToCheck[3]) ) {

                        System.out.println("Bad Move! Intersecting wall placed");

                        return false;
                    }
                }

            } else { //A horizontal wall 

                // If this position and the position left or right 
                // of it already has a horizontal wall, an invalid 
                // move was made.


                if( !spaces[0].edges.contains(spaces[2]) || 
                    spaces[0].edges.contains(spaces[2]) && 
                    !spaces[1].edges.contains(spaces[3]) ){
       
                    System.out.println("Bad Move! Overlapping  wall placed");
                
                    return false;
                }

                 //Check for intersecting Walls
                else if( !spaces[0].edges.contains(spaces[3]) && 
                         !spaces[2].edges.contains(spaces[4]) ) {

                    System.out.println("Possible intersection found");

                    Space[] spacesToCheck = {

                        gameBoard.getSpaceAt(spaces[0].x, spaces[0].y - 1),
                        gameBoard.getSpaceAt(spaces[0].x + 1, spaces[0].y - 1),
                        gameBoard.getSpaceAt(spaces[1].x, spaces[1].y + 1),
                        gameBoard.getSpaceAt(spaces[1].x + 1, spaces[1].y + 1)
                    };

                    for(Space s : spacesToCheck) {

                        System.out.println(s);
                        System.out.println(s.edges + "\n");
                    }
                }       
            }

        } catch(IndexOutOfBoundsException ex) {

            //Do Nothing
        }

        return !doesWallBlockPath(wallPos, direction);
    }

    public static boolean doesWallBlockPath(Point wallPos, char direction) {

        gameBoard.placeWall(wallPos, direction);

        for(int i = 0; i < pawnPos.length; i++){

            //initialization
            ArrayList<Space> q = new ArrayList<Space>();
            ArrayList<Space> visited = new ArrayList<Space>();

            boolean isPathBlocked = true;
  
            Space current = gameBoard.getSpaceAt(pawnPos[i].x, pawnPos[i].y);

            current.prev = null;
            q.add(current);
            visited.add(current);

            while (!q.isEmpty()) { //main loop

                //get the set of neighbour nodes
                for (Space a : current.edges) {
                    //if the node we are checking has not been visited
                    if (!visited.contains(a)) {
                        //we add it to the queue
                        q.add(a);
                        //add it to visited list
                        visited.add(a);
                        //make the previous node the current node
                        a.prev = current;

                        if(winPositions[i].contains(a)){
                            
                             isPathBlocked = false;
                        }
                    }// end if !viseted...
                } // end for each Space...
        
                q.remove(current);

                //see if the queue is empty
                if (!q.isEmpty()) {
                    //if it isnt then get the next node from the queue
                    current = q.get(0);
                }

            } // end while !q...
      
            if(isPathBlocked) {

                gameBoard.removeWall(wallPos, direction);
                
                return true;
            }
            
        } // end for int i...         

        return false;
    }

    /**
     * Checks to see if the player has won
     * 
     * @return "KIKASHI" if the pawn is in the appropriate
     * win position else return "ATARI" (Message for a legal 
     * pawn move).
     */
    private String hasWon() {

        return winPositions[playerNumber - 1]
            .contains(pawnPos[playerNumber - 1]) ? "KIKASHI" : "ATARI";
    }
}
