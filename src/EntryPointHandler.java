import java.io.*;
import java.net.Socket;

public class EntryPointHandler extends Thread{
    private Socket clientSocket;

    public EntryPointHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
            Protocol protocol = (Protocol) in.readObject();
            switch (protocol) {
                case CONNECT -> handleConnection(in);
                case UPDATE -> updateMaps();
                case REMOVE -> removeFromMaps(in);
                default -> System.out.println("Unknown protocol: " + protocol);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    private void handleConnection(ObjectInputStream in) {
        //System.out.println("Connected to port: " + clientSocket.getPort());
        String clientMessageUsername;
        String clientMessagePort;
        String clientMessagePublicKey;
        String clientMessageIp;
        try {
            //receive peer information
            String clientMessage = in.readUTF();
            clientMessageUsername = clientMessage.split(":")[0];
            clientMessagePort = clientMessage.split(":")[1];
            clientMessagePublicKey = clientMessage.split(":")[2];
            clientMessageIp = clientMessage.split(":")[3];

            PKI.PKusermap.put(clientMessageUsername, PKI.stringToPublicKey(clientMessagePublicKey));
            PKI.portUserMap.put(clientMessageUsername, Integer.parseInt(clientMessagePort));
            PKI.addUserLayer(clientMessageUsername);
            PKI.ipUserMap.put(clientMessageUsername,clientMessageIp);

            //System.out.println("Username: " + clientMessageUsername);
            //System.out.println("EntryPointListener port: " + clientMessagePort);
            //System.out.println("Public key: " + clientMessagePublicKey);
            //System.out.println("Ip: " + clientMessageIp);

            //send hashmaps
            ObjectOutputStream outObject = new ObjectOutputStream(clientSocket.getOutputStream());
            outObject.writeObject(PKI.portUserMap);
            outObject.writeObject(PKI.layerUserMap);
            outObject.writeObject(PKI.PKusermap);
            outObject.writeObject(PKI.ipUserMap);
            outObject.flush();
            clientSocket.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void updateMaps(){
        try {
            ObjectOutputStream outObject = new ObjectOutputStream(clientSocket.getOutputStream());
            outObject.writeObject(PKI.portUserMap);
            outObject.writeObject(PKI.layerUserMap);
            outObject.writeObject(PKI.PKusermap);
            outObject.writeObject(PKI.ipUserMap);
            outObject.flush();
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void removeFromMaps(ObjectInputStream in) {
        try {
            String removeUsername = in.readUTF();
            PKI.portUserMap.remove(removeUsername);
            PKI.PKusermap.remove(removeUsername);
            PKI.layerUserMap.remove(removeUsername);
            PKI.ipUserMap.remove(removeUsername);
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
