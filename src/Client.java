import java.io.*;
import java.net.Socket;

public class Client extends Thread{
    private String userID;

    public Client(String username) {
        this.userID = username;
    }

    @Override
    public void run(){
        int entryPointPort = 56145;
            try {
                Socket socket = new Socket("localhost", entryPointPort);
                System.out.println("Connected from port: " + socket.getLocalPort() + " to port: " + entryPointPort);

                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println(userID);
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
    }

}
