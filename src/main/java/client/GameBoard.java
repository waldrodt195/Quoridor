package client;

import java.util.ArrayList;
import java.awt.Point;
import java.util.HashSet;
import java.util.HashMap;


public class GameBoard {

    private static GameBoard instance = null;
    private ArrayList<Space> gameBoard;
    public static HashMap<Point, Character> wallsMap;

    /**
     * Singleton implementation of GameBoard
     *
     * @return A new GameBoard on first execution or the GameBoard created on
     * the first execution.
     */
    public static GameBoard getInstance() {

        if (instance == null) {
            return instance = new GameBoard();
        }

        return instance;
    }

    /**
     * Constructs an empty game board
     */
    public GameBoard() {

        //gameBoard = new GamePiece[9][9];
        gameBoard = new ArrayList<Space>();
        wallsMap = new HashMap<>();
        Space s; //temp space variable
        //create all the spaces objects
        for (int i = 0; i < 9; i++) {
            for (int p = 0; p < 9; p++) {
                s = new Space(p, i);
                gameBoard.add(s);
            }
        }

        //now we establish the connections
        for (int i = 0; i < 9; i++) {
            for (int p = 0; p < 9; p++) {

                s = gameBoard.get(p + i * 9); //using that temp variable we can get each node

                //here we will check if we are adding connections on the top row.
                if (i == 0) {
                    //if we are here we will not add a connection to the up pointer

                    if (p == 0) {
                        //here we are on the right hand side of the board
                        s.edges.add(gameBoard.get(p + (i * 9) + 1)); //here we get the node next to the current one
                        s.edges.add(gameBoard.get(p + (i * 9) + 9)); //get the node directly below the current node
                    } else if (p == 8) {
                        //here we are at the left hand side of the board
                        s.edges.add(gameBoard.get(p + (i * 9) + 9)); //get the node directly below the current node
                        s.edges.add(gameBoard.get(p + (i * 9) - 1)); //get the node that is just before the current node
                    } else {
                        //here we are in the middle of the board
                        s.edges.add(gameBoard.get(p + (i * 9) + 9)); //get the node directly below this node
                        s.edges.add(gameBoard.get(p + (i * 9) - 1)); //get the node that is just before the current node
                        s.edges.add(gameBoard.get(p + (i * 9) + 1)); //here we get the node next to the current one
                    }

                } else if (i == 8) { //here we are looking at the bottom row of the board    

                    if (p == 0) {
                        //here we are on the right hand side of the board
                        s.edges.add(gameBoard.get(p + (i * 9) + 1)); //here we get the node next to the current one
                        s.edges.add(gameBoard.get(p + (i * 9) - 9)); //get the node directly above the current node
                    } else if (p == 8) {
                        //here we are at the left hand side of the board
                        s.edges.add(gameBoard.get(p + (i * 9) - 9)); //get the node directly above the current node
                        s.edges.add(gameBoard.get(p + (i * 9) - 1)); //get the node that is just before the current node
                    } else {
                        //here we are in the middle of the board
                        s.edges.add(gameBoard.get(p + (i * 9) - 9)); //get the node directly above this node
                        s.edges.add(gameBoard.get(p + (i * 9) - 1)); //get the node that is just before the current node
                        s.edges.add(gameBoard.get(p + (i * 9) + 1)); //here we get the node next to the current one
                    }
                } else //here we are looking at the middle of the board
                {
                    if (p == 0) {
                        //here we are on the right hand side of the board
                        s.edges.add(gameBoard.get(p + (i * 9) + 1)); //here we get the node next to the current one
                        s.edges.add(gameBoard.get(p + (i * 9) - 9)); //get the node directly above the current node
                        s.edges.add(gameBoard.get(p + (i * 9) + 9)); //get the node directly below this node
                    } else if (p == 8) {
                        //here we are at the left hand side of the board
                        s.edges.add(gameBoard.get(p + (i * 9) - 9)); //get the node directly above the current node
                        s.edges.add(gameBoard.get(p + (i * 9) - 1)); //get the node that is just before the current node
                        s.edges.add(gameBoard.get(p + (i * 9) + 9)); //get the node directly below this node
                    } else {
                        //here we are in the middle of the board
                        s.edges.add(gameBoard.get(p + (i * 9) - 9)); //get the node directly above this node
                        s.edges.add(gameBoard.get(p + (i * 9) - 1)); //get the node that is just before the current node
                        s.edges.add(gameBoard.get(p + (i * 9) + 1)); //here we get the node next to the current one
                        s.edges.add(gameBoard.get(p + (i * 9) + 9)); //get the node directly below this node
                    }
                }
            }
        }
    }

    /**
     * @return The gameGame board data structure
     */
    public ArrayList<Space> getGameBoard() {

        return gameBoard;
    }

    /**
     *
     * @param row The row of the space to get
     * @param column The column of the space to get
     * @return The space at (row, column)
     */
    public Space getSpaceAt(int column, int row) {

        return gameBoard.get(column + (row * 9));
    }

    /**
     *
     * @param currentPos The current position of the pawn to move
     * @param newPos The position to move the pawn to
     */
    public void movePawn(Point currentPos, Point newPos) {

        getSpaceAt(currentPos.x, currentPos.y).occupied = false;
        getSpaceAt(newPos.x, newPos.y).occupied = true;
    }

    public void placePawn(Point position) {

        getSpaceAt(position.x, position.y).occupied = true;
    }

    /**
     *
     * @param wallPos The position to place the wall
     * @param direction The direction of the wall
     */
    public void placeWall(Point wallPos, char direction) {

        wallsMap.put(wallPos, direction);
        if (direction == 'v') {
            //Point temp = new Point(wallPos.x, wallPos.y + 1);
            //wallsMap.put(temp, new Character(direction));

            getSpaceAt(wallPos.x + 1, wallPos.y).edges
                    .remove(getSpaceAt(wallPos.x, wallPos.y));

            getSpaceAt(wallPos.x, wallPos.y).edges
                    .remove(getSpaceAt(wallPos.x + 1, wallPos.y));

            getSpaceAt(wallPos.x + 1, wallPos.y + 1).edges
                    .remove(getSpaceAt(wallPos.x, wallPos.y + 1));

            getSpaceAt(wallPos.x, wallPos.y + 1).edges
                    .remove(getSpaceAt(wallPos.x + 1, wallPos.y + 1));
        } else {
            //Point temp = new Point(wallPos.x + 1, wallPos.y);
            //wallsMap.put(temp, new Character(direction));

            getSpaceAt(wallPos.x, wallPos.y + 1).edges
                    .remove(getSpaceAt(wallPos.x, wallPos.y));

            getSpaceAt(wallPos.x, wallPos.y).edges
                    .remove(getSpaceAt(wallPos.x, wallPos.y + 1));

            getSpaceAt(wallPos.x + 1, wallPos.y + 1).edges
                    .remove(getSpaceAt(wallPos.x + 1, wallPos.y));

            getSpaceAt(wallPos.x + 1, wallPos.y).edges
                    .remove(getSpaceAt(wallPos.x + 1, wallPos.y + 1));
        }

    }

    /**
     *
     * @param pawnPos The position of the pawn to be removed
     */
    public void removePawn(Point pawnPos) {

        getSpaceAt(pawnPos.x, pawnPos.y).occupied = false;
    }

    /**
     *
     * @param wallPos The position to remove the wall
     * @param direction The direction of the wall
     */
    public void removeWall(Point wallPos, char direction) {

        wallsMap.remove(wallPos, direction);

        if (direction == 'v') {
            //Point temp = new Point(wallPos.x, wallPos.y + 1);
            //wallsMap.put(temp, new Character(direction));

            getSpaceAt(wallPos.x  , wallPos.y).edges
                    .add(getSpaceAt(wallPos.x + 1, wallPos.y));

            getSpaceAt(wallPos.x, wallPos.y + 1 ).edges
                    .add(getSpaceAt(wallPos.x + 1, wallPos.y + 1));

            getSpaceAt(wallPos.x + 1, wallPos.y + 1).edges
                    .add(getSpaceAt(wallPos.x, wallPos.y + 1));

            getSpaceAt(wallPos.x + 1, wallPos.y).edges
                    .add(getSpaceAt(wallPos.x, wallPos.y));
        } else {
            //Point temp = new Point(wallPos.x + 1, wallPos.y);
            //wallsMap.put(temp, new Character(direction));

            getSpaceAt(wallPos.x, wallPos.y).edges
                    .add(getSpaceAt(wallPos.x, wallPos.y + 1));

            getSpaceAt(wallPos.x + 1, wallPos.y).edges
                    .add(getSpaceAt(wallPos.x + 1, wallPos.y + 1));

            getSpaceAt(wallPos.x, wallPos.y + 1).edges
                    .add(getSpaceAt(wallPos.x, wallPos.y));

            getSpaceAt(wallPos.x + 1, wallPos.y + 1).edges
                    .add(getSpaceAt(wallPos.x + 1, wallPos.y));
        }
    }

    /**
     * @param space The current position of the pawn
     * @return The list of valid Spaces for the pawn to move to
     */
    public ArrayList<Space> getValidPlayerJumpMoves(Space space) {
        ArrayList<Space> validSpaces = new ArrayList<>();

        Space occ = getSpaceAt(space.x, space.y);
        HashSet<Space> connectedNodes = occ.edges;

        for (Space v : connectedNodes) {
            if (!v.occupied) {
                validSpaces.add(v);
            }
        }

        return validSpaces;
    }

    public boolean isWallPlacementValid(Point wall, char dir) {
        //true if the point is not in the map and if the key at that point is not the same
        //boolean base = (wallsMap.containsKey(wall) && wallsMap.get(wall) == dir); //the base if we have 

        if((wall.x > 7 || wall.y > 7) || (wall.x < 0 || wall.y < 0)) 
            return false;
        
        
        boolean base = (wallsMap.containsKey(wall));

        //if we are trying to place a wall where one was placed perv we say that 
        //the placement is invalid
        if (base) {
            return false;
        }

        //see if the walls are with in the game board
        if ((dir == 'v') && !((wall.y <= 7) && (wall.x <= 7))) {
            return false;
        } else if ((dir == 'h') && !((wall.x <= 7) && (wall.y <= 7))) {
            return false;
        }
        
        //if we are here then we where we are placeing a wall we need to check 
        //in the direction, if vert the above and below the point 
        //if horz then to the left and the right of the point
        switch (dir) {
            case 'v':        
                Point below = new Point(wall.x, wall.y - 1);
                Point above = new Point(wall.x, wall.y + 1);
                
                if (wallsMap.containsKey(above)) {
                    return false;
                } else if (wallsMap.containsKey(below)) {
                    return false;
                } else {
                    return true;
                }
            case 'h':
                Point right = new Point(wall.x + 1, wall.y);
                Point left = new Point(wall.x-1, wall.y);
                
                if (wallsMap.containsKey(right)) {
                    return false;
                } else if (wallsMap.containsKey(left)) {
                    return false;
                } else {
                    return true;
                }

            default:
                System.out.println("what the shit?!?!?!?!?!?!?");
                return false;

        }
    }
}
