package server;
import server.AI;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Matchers;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.net.Socket;
import java.net.UnknownHostException;
import java.io.IOException;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EchoServer.class)
public class ServerTest {

  private EchoServer fc;
  private AI ai;  

  @Before // Runs when class is initialized
  public void setup() {
    fc = PowerMockito.spy(new EchoServer(55555,1));
  }

  @Test
  public void testIfServerConstructed() {
    Assert.assertNotNull(fc);

    int port = Whitebox.getInternalState(fc, "portNumber");
    Assert.assertEquals(55555, port);

    int delay = Whitebox.getInternalState(fc, "delay");
    Assert.assertEquals(1, delay);
    
  }

  @Test
  public void testGameInit() {
    String gameMessage = "GAME 2 4tr:test1 4tr:test2";
    fc.gameInit(gameMessage);
    int pn = Whitebox.getInternalState(fc, "playerNumber");
    Assert.assertEquals(2,pn);
  }

}
