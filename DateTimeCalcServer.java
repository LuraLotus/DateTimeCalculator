import java.net.*;
import java.io.*;
import java.util.*;
import java.time.*;

class Server
{
  public static void main(String[] args) throws Exception
  {
    ServerSocket socket = new ServerSocket(21513);
    while(true)
        {
            System.out.println("Waiting For Connection ...");
            Socket soc = socket.accept();
            ObjectOutputStream out = new ObjectOutputStream(soc.getOutputStream());
            out.writeObject(LocalDateTime.now());
            out.close();
            soc.close();
        }
  }
}
