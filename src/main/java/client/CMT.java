package client;

import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.awt.Point;
import java.util.*;
import java.io.*;
import java.util.concurrent.CopyOnWriteArrayList;
import common.Parsed;
import common.Interpreter;
import javafx.application.Application;
import client.gui.Main;

public class CMT {

  static String hostname = "localhost";
  static String names = "";
  static String move = "";
  static int playerNumber;
  static int playerCount;
  static int delay = 1;
  static GuiThread gt;
  private static Interpreter pr = new Interpreter();
  static Main gui = null;

  private static ArrayList<Player> playerList = new ArrayList<Player>();
  private static ArrayList<String> playerNames = new ArrayList<String>();
  private static CopyOnWriteArrayList<ClientThread> threadList
  = new CopyOnWriteArrayList<ClientThread>();

  public static void main(String[] args) throws Exception {

    playerNumber = 1;
    //playerCount = args.length;
    Socket clientSocket;

    try {
      InetAddress addr;
      addr = InetAddress.getLocalHost();
      hostname = addr.getHostName();
    } catch (UnknownHostException ex) {
      System.out.println("Hostname can not be resolved");
    }

    // loops through args[], ensures that the hostname:portnumber entered are
    // valid and creates a thread to handle each move-server entered at the
    // command line. Adds the thread to our list of threads (threadList) and
    // starts each thread.
    for (int i = 0; i < args.length; i++) {
      if(args[i].equals("--delay")){
        try{
          delay = Integer.parseInt(args[i+1]);
          i++;
        }catch(NumberFormatException e){
          System.out.println(e);
        }
      }else{
        String[] temp = args[i].split(":");
        clientSocket = errorCheck(temp);
        playerCount++;
        ClientThread client = new ClientThread(clientSocket);
        threadList.add(client);
        client.start();
      }
    }

    // Creates a thread to run / launch the GUI. Facilitates sending messages
    // From the client to the GUI because of javafx's run-loop.
    new Thread() {
      @Override
      public void run() {
        javafx.application.Application.launch(client.gui.Main.class);
      }
    }.start(); 
    
    // HELLO -> IAM Handshake
    Handshake();

    // Build list of names
    nameBuilder();

    // Initialize Game of 4 or 2 players    
    if(playerCount == 4)
      initGame4();
    else
      initGame();

    // Runs Play loop (MYOUSHU -> TESUJI -> ATARI)
    Play();

    // Retrieving gui object after run-loop has started and startup test is done
    // Facilitates sending messages from client to gui due to javafx's run-loop.
    gui = Main.waitForStartUp();
  }

  // Builds string containing the names of all connected players as per protocol
  public static void nameBuilder() {
    for (int i = 0; i < playerNames.size(); i++) {
      names += playerNames.get(i) + " ";
    }
  }

  // Completes the HELLO -> IAM handshake for each connected client
  public static void Handshake() throws Exception {
    int counter = 1;
    for (ClientThread c : threadList) {
      playerNames.add(c.handShake() + counter);
      counter++;
    }
  }

  // Initializes a game instance for two connected move-servers.
  // Creates a player for each move-server, gives it the appropriate player #,
  // adds it to the list of players and sends GAME message to move-servers
  // as per protocol. Also hands list of players & number of players to the GUI.
  public static void initGame() throws Exception {
    int count = 0;
    for (ClientThread c : threadList) {
      c.write("GAME " + playerNumber + " " + names);
      c.setPlayerNumber(playerNumber);
      c.createPlayer(10);
      playerList.add(c.getPlayer());
      if(count == 0)
        playerNumber = 2;
      count++;
    }

    gui.setPlayers(playerList);
    gui.setPlayerCount(playerCount);
  }

  // Initializes a game instance for four connected move-servers.
  // Creates a player for each move-server, gives it the appropriate player #,
  // adds it to the list of players and sends GAME message to move-servers
  // as per protocol. Also hands list of players & number of players to the GUI.
  public static void initGame4() throws Exception {
    int count = 0;
    for (ClientThread c : threadList) {
      c.write("GAME " + playerNumber + " " + names);
      c.setPlayerNumber(playerNumber);
      c.createPlayer(5);
      playerList.add(c.getPlayer());
      if(count == 0)
        playerNumber = 4;
      if(count == 1)
        playerNumber = 2;
      if(count == 2)
        playerNumber = 3;
      count++;
    }

    gui.setPlayers(playerList);
    gui.setPlayerCount(playerCount);
  }

  // Workhorse of the program. Continously loops through list of threads
  // that are handling each connected move-server until a winner has been found.
  // Completes the MYOUSHU -> TESUJI -> ATARI loop as per protocol.
  // Writes MYOUSHU to each connected move-server, checks if their TESUJI
  // response is valid via Parsed Object (smh.) If it is, we then check if it is
  // a wall or if it is a move. If it is a wall, we update the gameboard / GUI,
  // attempt to placeWall and ATARI the message. If it is a move, we attempt to 
  // move the pawn for the connected move-server. If a move is not valid, we
  // GOTE (kick) the player from the game. Upon each iteration, we check if a 
  // player has satisfied a win condition via KIKASHI. If they have, we tell the
  // Connected move-servers who won and return to main to end the game.
  public static void Play() throws Exception {
    String tesuji = "";
    String moveResult = "";
    while (true) {
      for (ClientThread c : threadList) {
        c.sleep(delay);
        tesuji = c.Myoushu();
        pr.parse(tesuji);
        if (pr.isValid()) {
          if (pr.isWall()) {
            GameBoard gb = GameBoard.getInstance();
            moveResult = c.getPlayer().placeWall(pr.getX(), pr.getY(), pr.getWallDirection());
            if(!moveResult.equals("GOTE")){

              gui.AtariWall(gb.wallsMap);
              AtariWall(tesuji.substring(7), c.getPlayerNumber());
              //gui.AtariWall(gb.wallsMap);
            }else if(moveResult.equals("GOTE")){
              Gote(c,tesuji);
            }
          }else {
            moveResult = c.getPlayer().movePawn(pr.getX(), pr.getY());
            
            if(!moveResult.equals("GOTE"))
              Atari(tesuji.substring(7), c.getPlayerNumber());
            else if(moveResult.equals("GOTE")){
              Gote(c,tesuji);
            }

          }
          if(moveResult.equals("KIKASHI")) {
            Kikashi(c.getPlayerNumber());
            return;
          }
        }else {
          Gote(c,tesuji);
        }
      }
    }
  }

  // Method for sending KIKASHI message to each connected move-server should
  // a player satisfy a win condition.
  public static void Kikashi(int pn) throws Exception{
    for(ClientThread c : threadList)
      c.write("KIKASHI " + pn);
    System.out.println("KIKASHI " + pn);
  }

  // Method for sending ATARI (broadcast) message to each connected move-server.
  // Used for alerting move-servers to other move-server's moves. Also updates
  // GUI with move information for the specific player.
  public static void Atari(String message, int pn) throws Exception {
    int count = 0;
    for (ClientThread c : threadList) {
      c.write("ATARI " + pn + " " + message);
      if (count == 0) {
        gui.setCurrentPlayer(pn);
        Point dest = new Point(pr.getX(), pr.getY());
        gui.Atari(dest);
      }
      count++;
    }
    gui.setPlayerCount(playerCount);

  }

  // Separate method for sending ATARI message to each connected move-server.
  // Used for alerting move-servers to other move-servers wall placements.
  // Might refactor AtariWall and Atari into single method at some point.
  public static void AtariWall(String message, int pn) throws Exception {
    int count = 0;
    for (ClientThread c : threadList) {
      c.write("ATARI " + pn + " " + message);
      if (count == 0) {
        gui.setCurrentPlayer(pn);
        Point dest = new Point(pr.getX(), pr.getY());
      }
      count++;
    }
    gui.setPlayerCount(playerCount);
  } 

  // Method for sending GOTE message to each connected move-server so as to
  // alert them of a player being kicked for sending an invalid move/wall or
  // not adhering to protocol. After sending message to each move-server, the
  // player is removed and the thread handling them is pruned from threadList. 
  public static void Gote(ClientThread g, String tesuji) throws Exception {
    for (ClientThread c : threadList) {
      c.write("GOTE " + g.getPlayerNumber());
    }
    System.out.println("Kicking Player#: " + g.getPlayerNumber() +
                       " for " + tesuji);
    gui.setPlayerCount(--playerCount);
    gui.gote(g.getPlayerNumber());
    threadList.remove(g);
  }

  // Ensures that the ports entered are valid. Returns Socket open on provided
  // Port and host name (unless defaulted.) 
  public static Socket errorCheck(String[] temp) {
    Socket ec = new Socket();
    int portNumber = 0;
    try {
      portNumber = Integer.parseInt(temp[1]);
      hostname = temp[0];
      if (portNumber < 1024 || portNumber > 65535) {
        System.out.println();
        throw new IndexOutOfBoundsException("Port number must be "
            + "between 1024 and 65535 inclusive");
      }
      ec = new Socket(hostname, portNumber);
    } catch (NumberFormatException e) {
      System.out.println("\nPort number must be an Integer");
      System.out.println("You entered: " + temp[0]);
      System.out.println("Exiting...");
      System.exit(0);
    } catch (IOException e) {
      System.out.println(e);
      System.exit(0);
    }
    return ec;
  }
}
