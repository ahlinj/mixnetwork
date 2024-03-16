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
        String clientMessagePort;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String clientMessage = in.readLine();
            clientMessageUsername = clientMessage.split(":")[0];
            clientMessagePort = clientMessage.split(":")[1];
            System.out.println("Username: " + clientMessageUsername);
            System.out.println("Server port: " + clientMessagePort);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        PKI.addUserPort(clientMessageUsername,Integer.parseInt(clientMessagePort));
        PKI.addUserLayer(clientMessageUsername);
    }

}
