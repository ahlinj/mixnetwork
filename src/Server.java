import java.io.*;
import java.net.*;

public class Server extends Thread {
    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        while (true) {
            try {
                //System.out.println("Listening on: " + serverSocket.getLocalPort());
                Socket clientSocket = serverSocket.accept();
                new ServerHandler(clientSocket).start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
