import java.io.*;
import java.net.Socket;

public class Client extends Thread{


    @Override
    public void run(){
        int entryPointPort = 54480;
            try {
                Socket socket = new Socket("localhost", entryPointPort);
                System.out.println("Connected from port: " + socket.getLocalPort() + " to port: " + entryPointPort);
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
    }

}
