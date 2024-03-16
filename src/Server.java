import java.io.*;
import java.net.*;

public class Server extends Thread{

    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }


    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("Listening on: "+serverSocket.getLocalPort());
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleConnection(clientSocket)).start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void handleConnection(Socket clientSocket) {
        System.out.println("Connected from port: " + serverSocket.getLocalPort() + " to port: " + clientSocket.getPort());

        String clientMessageUsername;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            clientMessageUsername = in.readLine();
            System.out.println("Username: " + clientMessageUsername);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        PKI.addUserPort(clientMessageUsername, clientSocket.getPort());

    }

}
