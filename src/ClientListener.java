import java.io.*;
import java.net.*;

public class ClientListener extends Thread{

    private ServerSocket serverSocket;
    private String name;

    public ClientListener(ServerSocket serverSocket, String name) {
        this.serverSocket = serverSocket;
        this.name = name;
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("Listening on: " + serverSocket.getLocalPort());
                Socket clientSocket = serverSocket.accept();

                handleConnection(clientSocket);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void handleConnection(Socket clientSocket) throws IOException {
        System.out.println("Connected from port: " + serverSocket.getLocalPort() + " to port: " + clientSocket.getPort());
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        System.out.println(name);
        out.println(name);

    }
}
