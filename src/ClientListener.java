import java.io.*;
import java.net.*;
import java.security.PrivateKey;

public class ClientListener extends Thread{

    private ServerSocket serverSocket;
    private String name;
    private PrivateKey privateKey;

    public ClientListener(ServerSocket serverSocket, String name, PrivateKey privateKey) {
        this.serverSocket = serverSocket;
        this.name = name;
        this.privateKey = privateKey;
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
                System.out.println("Received message: " + message.body+" --- Route: "+message.route);
                //System.out.println("Encrypted received message: " + Cryptography.decrypt(message,privateKey).body);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
