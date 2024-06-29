package EntryPoint;

import java.io.*;
import java.net.*;

public class EntryPointListenerEP extends Thread {
    private ServerSocket serverSocket;
    public EntryPointListenerEP(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        while (true) {
            try {
                //System.out.println("Listening on: " + serverSocket.getLocalPort());
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected from " + clientSocket.getInetAddress().getHostAddress());
                new EntryPointHandlerEP(clientSocket).start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
