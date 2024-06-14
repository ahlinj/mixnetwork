import java.io.*;
import java.net.Socket;
import java.security.PublicKey;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Peer extends Thread{

    int entryPointPort;

    private final String userID;
    private final int serverSocketPort;
    private final PublicKey publicKey;


    public Peer(String username, int serverSocketPort, PublicKey publicKey, int epPort) {
        this.userID = username;
        this.serverSocketPort = serverSocketPort;
        this.publicKey = publicKey;
        this.entryPointPort = epPort;
    }

    @Override
    public void run(){
        initialConnection();
    }

    private void initialConnection() {
        try {
            Socket socketEP = new Socket("localhost", entryPointPort);
            //System.out.println("Connected from port: " + socketEP.getLocalPort() + " to port: " + entryPointPort);

            ObjectOutputStream outObject = new ObjectOutputStream(socketEP.getOutputStream());
            Protocol protocol = Protocol.CONNECT;
            outObject.writeObject(protocol);
            outObject.flush();
            //send your information to entry point
            outObject.writeUTF(userID+":"+serverSocketPort+":"+PKI.publicKeyToString(publicKey));
            outObject.flush();

            //receive hashmaps from entry point when first connected
            ObjectInputStream inObject = new ObjectInputStream(socketEP.getInputStream());
            Map<String, Integer> receivedPortMap = (ConcurrentHashMap<String, Integer>) inObject.readObject();
            //System.out.println("Received hashmap from server: " + receivedPortMap);
            PKI.portUserMap.putAll(receivedPortMap);

            Map<String, Integer> receivedLayerMap = (ConcurrentHashMap<String, Integer>) inObject.readObject();
            //System.out.println("Received hashmap from server: " + receivedLayerMap);
            PKI.layerUserMap.putAll(receivedLayerMap);

            Map<String, PublicKey> receivedPKMap = (ConcurrentHashMap<String, PublicKey>) inObject.readObject();
            //System.out.println("Received hashmap from server: " + receivedPKMap);
            PKI.PKusermap.putAll(receivedPKMap);
            socketEP.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    public void sendMessage(Message message, int sendTo){
        try {
            Socket socket = new Socket("localhost", sendTo);
            ObjectOutputStream outObject = new ObjectOutputStream(socket.getOutputStream());
            outObject.writeObject(Protocol.MESSAGE);
            outObject.flush();
            outObject.writeObject(message);
            outObject.flush();
            socket.close();
            System.out.println("--------------------------------");
            System.out.println("Message sent!");
            System.out.println("--------------------------------");
            socket.close();
        }catch(IOException e){
            System.err.println(e.getMessage());
        }
    }
    public void updateUsermaps(){
        try {
            Socket socketEP = new Socket("localhost", entryPointPort);
            ObjectOutputStream outObject = new ObjectOutputStream(socketEP.getOutputStream());
            Protocol protocol = Protocol.UPDATE;
            outObject.writeObject(protocol);
            outObject.flush();

            ObjectInputStream inObject = new ObjectInputStream(socketEP.getInputStream());
            Map<String, Integer> receivedPortMap = (ConcurrentHashMap<String, Integer>) inObject.readObject();
            //System.out.println("Received hashmap from server: " + receivedPortMap);
            PKI.portUserMap.putAll(receivedPortMap);

            Map<String, Integer> receivedLayerMap = (ConcurrentHashMap<String, Integer>) inObject.readObject();
            //System.out.println("Received hashmap from server: " + receivedLayerMap);
            PKI.layerUserMap.putAll(receivedLayerMap);

            Map<String, PublicKey> receivedPKMap = (ConcurrentHashMap<String, PublicKey>) inObject.readObject();
            //System.out.println("Received hashmap from server: " + receivedPKMap);
            PKI.PKusermap.putAll(receivedPKMap);

            System.out.println("--------------------------------");
            System.out.println("Users in the network:");
            for (String key : PKI.portUserMap.keySet()) {
                if (!key.equals("EP") && !key.equals(Main.username.get())) {
                    System.out.print(key+", ");
                }
            }
            System.out.println("\n--------------------------------");
            socketEP.close();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public void removeUser(){

        try {
            Socket socketEP = new Socket("localhost", entryPointPort);
            ObjectOutputStream outObject = new ObjectOutputStream(socketEP.getOutputStream());
            Protocol protocol = Protocol.REMOVE;
            outObject.writeObject(protocol);
            outObject.flush();
            outObject.writeUTF(Main.username.get());
            outObject.flush();
            socketEP.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
