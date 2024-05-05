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
                System.out.println("Listening on: " + serverSocket.getLocalPort());
                Socket clientSocket = serverSocket.accept();
                handleConnection(clientSocket);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void handleConnection(Socket clientSocket) {
        System.out.println("Connected from port: " + serverSocket.getLocalPort() + " to port: " + clientSocket.getPort());

        String clientMessageUsername = null;
        String clientMessagePort = null;
        String clientMessagePublicKey;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            ObjectOutputStream outObject = new ObjectOutputStream(clientSocket.getOutputStream());
            String clientMessage = in.readLine();

            //receive peer information
            clientMessageUsername = clientMessage.split(":")[0];
            clientMessagePort = clientMessage.split(":")[1];
            clientMessagePublicKey = clientMessage.split(":")[2];

            PKI.PKusermap.put(clientMessageUsername, PKI.stringToPublicKey(clientMessagePublicKey));
            PKI.portUserMap.put(clientMessageUsername, Integer.parseInt(clientMessagePort));
            PKI.addUserLayer(clientMessageUsername);

            System.out.println("Username: " + clientMessageUsername);
            System.out.println("Server port: " + clientMessagePort);
            System.out.println("Public key: " + clientMessagePublicKey);

            //send hashmaps
            outObject.writeObject(PKI.portUserMap);
            outObject.writeObject(PKI.layerUserMap);
            outObject.writeObject(PKI.PKusermap);
            outObject.flush();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
