package server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.net.InetAddress;
import java.net.UnknownHostException;
import common.Parsed;

public class EchoServer {

    public final static int DEFAULT_PORT_NUMBER = 3939;
    public final static String DEFAULT_MACHINE_NAME = "localhost";
    
    public final static String ARG_PORT = "--port";
    public final static String ARG_DELAY = "--delay";
    public final static String MSG_HELLO = "HELLO";
    static String hostname = "localhost";
    
    //Player number for our move server. Handed to use by the client.
    private static int playerNumber;
    //Port number that our server will listen to connections on.
    private int portNumber;
    //Amount of delay in ms that we will set Thread.sleep() to.
    private int delay;
    //Instance of AI object.
    private AI ai;
    //Instance of EchoServer object.
    private static EchoServer fc;

    //EchoServer constructor. Sets portNumber and delay to what was provided.
    public EchoServer(int portNumber, int delay) {
        this.portNumber = portNumber;
	this.delay = delay;
    }

    // Handles the HELLO message from the client.
    // Responds to client with our team ID and hostname.
    public void handshake(String clientMessage, PrintStream cout){
        cout.println("IAM 4tr:" + hostname);
        System.out.format("Server saw \"%s\"\n", clientMessage); 
    }

    // Handles MYOUSHU message from client. Responds with TESUJI followed by
    // The move our AI decided to make.
    public void myoushu(PrintStream cout){
        System.out.println("Please make your move or place a wall");
        String m = ai.getMove();
        System.out.println(m);
        cout.println(m);
    }

    // Handles the GAME message from the client. If formatted correctly
    // the method sets our playerNumber to the one handed to us in the message
    // and then creates a new AI, also handing it our playerNumber and the
    // number of players.
    public void gameInit(String clientMessage){
        try{
            String[] message = clientMessage.split(" ");
            int pn = Integer.parseInt(clientMessage.substring(5,6));
            playerNumber = pn;
            System.out.println("Testing message.length: " + message.length);
            if(message.length == 4)
                ai = new AI(pn, 2, delay);
            else
                ai = new AI(pn, 4, delay);
        }catch(NumberFormatException e){
            System.out.println(e);
        }
        System.out.format("Server saw \"%s\"\n", clientMessage);
    }

    // Handles ATARI message from client. When ATARI is received, updates our AI
    // with wall placement and opponent move information.
    public void updateAI(String clientMessage){
        try{
            int pn = Integer.parseInt(clientMessage.substring(6,7));
            Parsed parsed = new Parsed(clientMessage);
            if(parsed.isWall){
                ai.placeWalls(parsed.c, parsed.r, parsed.wallPos);
             }else{
                ai.updatePlayerPosition(parsed.c , parsed.r , pn);
             }
        }catch(NumberFormatException e){
             System.out.println(e);
        }
        System.out.format("Server saw \"%s\"\n", clientMessage);
    }

    // Handles GOTE message from the client. Checks if it is our player number
    // being kicked, and if it is, we reset the connection so that we can handle
    // other requests to play.
    public void handleGOTE(String clientMessage, Scanner cin, PrintStream cout,
			   ServerSocket server) throws IOException{
        try{
            String[] message = clientMessage.split(" ");
            int pn = Integer.parseInt(clientMessage.substring(5,6));
            if(pn == playerNumber){
                resetConnection(cin,cout,server,clientMessage);
            }else{
                System.out.format("Server saw \"%s\"\n", clientMessage);
            }
        }catch(NumberFormatException e){
            System.out.println(e);
        }
    }

    // Resets our connections and then runs again so that we can handle other
    // requests to play against our move-server.
    public void resetConnection(Scanner cin, PrintStream cout,
                ServerSocket server, String clientMessage) throws IOException{

        System.out.format("Server saw \"%s\"\n", clientMessage);
        cout.close();
        cin.close();
        server.close();
        fc.run();
    }

    // Handles incoming connections and communications from client.
    // Runs until our move-server is kicked or a winner is reported from client.
    public void run() {
        try {
            ServerSocket server = new ServerSocket(portNumber);
            System.out.format("Server now accepting connections on port %d\n",
                              portNumber);
            Socket client;

            while ((client = server.accept()) != null) {
                System.out.format("Connection from %s\n", client);

                Scanner cin = new Scanner(client.getInputStream());
                PrintStream cout = new PrintStream(client.getOutputStream());
                String clientMessage = "";

                    while (cin.hasNextLine()) {
		        clientMessage = cin.nextLine();
                        if(clientMessage.equals(MSG_HELLO))
                            handshake(clientMessage, cout);
			else if(clientMessage.equals("MYOUSHU"))   
                            myoushu(cout);                            
                        else if(clientMessage.substring(0,4).equals("GAME"))
                            gameInit(clientMessage);
			else if(clientMessage.substring(0,5).equals("ATARI"))
                            updateAI(clientMessage);
                        else if(clientMessage.substring(0,4).equals("GOTE"))
                            handleGOTE(clientMessage, cin, cout, server);
			else if(clientMessage.substring(0,7).equals("KIKASHI"))
                            resetConnection(cin,cout,server,clientMessage);
			else
			    System.out.format("Server saw \"%s\"\n",clientMessage);
                    }

                if (!clientMessage.isEmpty()) {
                    System.out.format("Server saw \"%s\" and is exiting.\n",
              		              clientMessage);
                }
                resetConnection(cin,cout,server,clientMessage);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(1);
        }
    }

    private static void usage() {
        System.err.print("usage: java FirstServer [options]\n" +
            "       where options:\n" + "       --port port\n" +
	    "                           --delay (delay in ms)");
    }

    public static void main(String[] args) {
        int port = DEFAULT_PORT_NUMBER;
        int delay = 1;
        /* Parsing parameters. argNdx will move forward across the
         * indices; remember for arguments that have their own parameters, you
         * must advance past the value for the argument too.
         */
        try{
            InetAddress addr;
            addr = InetAddress.getLocalHost();
            hostname = addr.getHostName();
        }catch (UnknownHostException ex){
            System.out.println("Hostname can not be resolved");
        }
        int argNdx = 0;

        while (argNdx < args.length) {
             String curr = args[argNdx];

             if (curr.equals(ARG_PORT)) {
                 ++argNdx;
                 String numberStr = args[argNdx];
                 port = Integer.parseInt(numberStr);
	     }else if(curr.equals(ARG_DELAY)) {
	         ++argNdx;
		 String delayStr = args[argNdx];
                 delay = Integer.parseInt(delayStr);
             } else {
                 System.err.println("Unknown parameter \"" + curr + "\"");
                 usage();
                 System.exit(1);
             }

             ++argNdx;
        }

        fc = new EchoServer(port,delay);
        fc.run();
    }
}
