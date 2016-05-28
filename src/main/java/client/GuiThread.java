package client;
import client.gui.Main;
import java.awt.Point;

public class GuiThread extends Thread{
    
    GuiThread(){
    }
    public void run(){
        //String [] args = new String [0];
        //client.gui.Main.main(args);
	javafx.application.Application.launch(client.gui.Main.class);
    }

    Main gui = Main.waitForStartUp();
    public void Atari(Point dest){
	gui.Atari(dest);
    }
}
