import java.io.*;
import java.net.Socket;

public class Client extends Thread{

    private final String userID;
    private final int serverSocketPort;

    public Client(String username, int serverSocketPort) {
        this.userID = username;
        this.serverSocketPort = serverSocketPort;
    }

    @Override
    public void run(){
        int entryPointPort = 61385;
            try {
                Socket socket = new Socket("localhost", entryPointPort);
                System.out.println("Connected from port: " + socket.getLocalPort() + " to port: " + entryPointPort);

                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println(userID+":"+serverSocketPort);
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
    }

}
