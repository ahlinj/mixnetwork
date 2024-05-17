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
        ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
        try {
            Message message = null;
            while(message == null){
                message = (Message) in.readObject();
                System.out.println("Received message: " + message.body);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
