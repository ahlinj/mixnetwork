package Users;

import java.io.*;
import java.net.*;
import java.security.PrivateKey;

public class PeerListener extends Thread{

    private ServerSocket serverSocket;
    private PrivateKey privateKey;

    public PeerListener(ServerSocket serverSocket, PrivateKey privateKey) {
        this.serverSocket = serverSocket;
        this.privateKey = privateKey;
    }

    @Override
    public void run() {
        while (true) {
            try {
                //System.out.println("Listening on: " + serverSocket.getLocalPort());
                Socket clientSocket = serverSocket.accept();
                new PeerHandler(clientSocket,privateKey).start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
