import java.io.*;
import java.net.*;

public class EntryPointListener extends Thread {
    private ServerSocket serverSocket;
    private int numEncyptions;

    public EntryPointListener(ServerSocket serverSocket, int numEncyptions) {
        this.serverSocket = serverSocket;
        this.numEncyptions = numEncyptions;
    }

    @Override
    public void run() {
        while (true) {
            try {
                //System.out.println("Listening on: " + serverSocket.getLocalPort());
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected from " + clientSocket.getInetAddress().getHostAddress());
                new EntryPointHandler(clientSocket, numEncyptions).start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
