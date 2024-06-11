import java.io.*;
import java.net.Socket;

public class ServerHandler extends Thread{
    private Socket clientSocket;

    public ServerHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
            Protocol protocol = (Protocol) in.readObject();
            switch (protocol) {
                case CONNECT -> handleConnection(in);
                case MESSAGE -> receiveMessages(in);
                default -> System.out.println("Unknown protocol: " + protocol);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    private void handleConnection(ObjectInputStream in) {
        System.out.println("Connected to port: " + clientSocket.getPort());

        String clientMessageUsername;
        String clientMessagePort;
        String clientMessagePublicKey;
        try {


            //receive peer information
            String clientMessage = in.readUTF();
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
            ObjectOutputStream outObject = new ObjectOutputStream(clientSocket.getOutputStream());
            outObject.writeObject(PKI.portUserMap);
            outObject.writeObject(PKI.layerUserMap);
            outObject.writeObject(PKI.PKusermap);
            outObject.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void receiveMessages(ObjectInputStream in) {
        try {
            Message message = (Message) in.readObject();
            System.out.println("Received message: " + message.body+" --- Route: "+message.route);
            //System.out.println("Encrypted received message: " + Cryptography.decrypt(message,privateKey).body);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
