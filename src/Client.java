import java.io.*;
import java.net.Socket;

public class Client extends Thread{


    @Override
    public void run(){
        int entryPointPort = 55642;
            try {
                Socket socket = new Socket("localhost", entryPointPort);
                System.out.println("Connected from port: " + socket.getLocalPort() + " to port: " + entryPointPort);

                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println(Main.username);
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
    }

}
