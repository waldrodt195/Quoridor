package client;

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
@PrepareForTest(CMT.class)
public class ClientTest {

  private CMT client;

  @Before // Runs when class is initialized
  public void setup() {
    client = PowerMockito.spy(new CMT());
  }

  @Test
  public void testIfClientConstructed() {
    assertNotNull(client);

  }

  @Test //Tests if Socket is returned from error check when passed valid port
  public void testSocketCheck() {
    int portNumber = 55555;
    String portString = "55555";
    String hostname = "localhost";
    String [] ec = {"localhost","55555"};
    Socket temp = new Socket();
    Socket test = CMT.errorCheck(ec);
    try{
      Assert.assertEquals((temp = new Socket(hostname, portNumber)), test); 
    }catch(UnknownHostException e){
      System.out.println(e);
    }catch(IOException e){
      System.out.println(e);
    }
  }

}
