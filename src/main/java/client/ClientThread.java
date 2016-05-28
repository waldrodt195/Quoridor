package client;
import java.net.Socket;
import java.util.*;
import java.io.*;

class ClientThread extends Thread{

    int playerNumber = 0;
    Socket clientSocket;
    String playerName = "";    
    public Player player;
    ClientThread(Socket s)throws Exception {
        clientSocket = s;
    }

    public String handShake() throws Exception{
        try{
            PrintStream sout = new PrintStream(clientSocket.getOutputStream());
            Scanner sin = new Scanner(clientSocket.getInputStream());
            sout.println("HELLO");
            String response = sin.nextLine();
            System.out.println(response);
	    playerName = response.substring(4);
            return playerName;
        }catch(IOException e){
            System.out.println(e);
        }
        return "Player name failure";
    }

    public Player getPlayer(){
	return player;
    }

    public void setPlayerNumber(int pn){
	playerNumber = pn;
    }

    public int getPlayerNumber(){
	return playerNumber;
    }

    public void write(String message) throws Exception{
        try{
            Scanner sin = new Scanner(clientSocket.getInputStream());
            PrintStream sout = new PrintStream(clientSocket.getOutputStream());
            sout.println(message);
            //String serverLine = sin.nextLine();
            //System.out.println(serverLine);
        }catch(IOException e){
            System.out.println(e);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    public String Myoushu(){
        try{
            Scanner sin = new Scanner(clientSocket.getInputStream());
            PrintStream sout = new PrintStream(clientSocket.getOutputStream());
            sout.println("MYOUSHU");
	    //going to implement Process.getInputStream() things here for handling gui clicks
            String serverLine = sin.nextLine();
	    return serverLine;
        }catch(IOException e){
            System.out.println(e);
        }catch(Exception e){
            System.out.println(e);
        }
        return "Failure to retrieve message from server";
    }

    public String getPlayerName(){
	return playerName;
    }

    public void createPlayer(int wallCount) {
        player = new Player(playerNumber, wallCount);
    }

    public void run(){
        try{
            Scanner sin = new Scanner(clientSocket.getInputStream());
            PrintStream sout = new PrintStream(clientSocket.getOutputStream());
            Scanner keyboard = new Scanner(System.in);		
        }catch(IOException e){
            System.out.println(e);
        }catch(Exception j){
            System.out.println(j);
        }
    }
}

