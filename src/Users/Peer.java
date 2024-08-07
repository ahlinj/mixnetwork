package Users;

import Common.Protocol;

import java.io.*;
import java.net.Socket;
import java.security.PublicKey;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Peer extends Thread{
    String myIp;
    String entryPointIp;
    int entryPointPort;

    private final String userID;
    private final int serverSocketPort;
    private final PublicKey publicKey;
    private final int numberOfEncryptions;


    public Peer(String username, int serverSocketPort, PublicKey publicKey, int epPort, String entryPointIp, String myIp, int numberOfEncryptions) {
        this.userID = username;
        this.serverSocketPort = serverSocketPort;
        this.publicKey = publicKey;
        this.entryPointPort = epPort;
        this.entryPointIp = entryPointIp;
        this.myIp = myIp;
        this.numberOfEncryptions = numberOfEncryptions;
    }

    @Override
    public void run(){
        initialConnection();
    }

    private void initialConnection() {
        try {
            Socket socketEP = new Socket(entryPointIp, entryPointPort);
            //System.out.println("Connected from port: " + socketEP.getLocalPort() + " to port: " + entryPointPort);

            ObjectOutputStream outObject = new ObjectOutputStream(socketEP.getOutputStream());
            Protocol protocol = Protocol.CONNECT;
            outObject.writeObject(protocol);
            outObject.flush();
            //send your information to entry point
            outObject.writeUTF(userID+":"+serverSocketPort+":"+PKI.publicKeyToString(publicKey)+":"+myIp);
            outObject.flush();

            //receive hashmaps from entry point when first connected
            ObjectInputStream inObject = new ObjectInputStream(socketEP.getInputStream());
            Map<String, Integer> receivedPortMap = (ConcurrentHashMap<String, Integer>) inObject.readObject();
            //System.out.println("Received hashmap from server: " + receivedPortMap);
            PKI.portUserMap.putAll(receivedPortMap);

            Map<String, PublicKey> receivedPKMap = (ConcurrentHashMap<String, PublicKey>) inObject.readObject();
            //System.out.println("Received hashmap from server: " + receivedPKMap);
            PKI.PKusermap.putAll(receivedPKMap);

            Map<String, String> receivedIpMap = (ConcurrentHashMap<String, String>) inObject.readObject();
            //System.out.println("Received hashmap from server: " + receivedIpMap);
            PKI.ipUserMap.putAll(receivedIpMap);

            socketEP.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    public void sendMessage(Message message){
        try {
            String lastKey = null;
            //System.out.println("Starting time: "+System.currentTimeMillis());
            for (int i = 0; i < numberOfEncryptions; i++) {
                String key = PKI.getRandomEntryFromMap(PKI.PKusermap).getKey();
                //System.out.println("Random peer in the network: "+key);
                message = Cryptography.encrypt(message, PKI.PKusermap.get(key));
                //System.out.println("Encryption num.: "+i+2);
                //System.out.println("Body: "+message.body);
                //System.out.println("Sender: "+message.sender);
                //System.out.println("Route: "+message.route);
                message = Message.addRouteInfo(message, PKI.ipUserMap.get(key));
                //System.out.println("Added route info: "+message.route);
                lastKey = key;
            }
            Socket socket = new Socket(PKI.ipUserMap.get(lastKey), Constants.SERVICE_PORT);
            ObjectOutputStream outObject = new ObjectOutputStream(socket.getOutputStream());
            outObject.writeObject(message);
            outObject.flush();
            socket.close();
            System.out.println("--------------------------------");
            System.out.println("Message sent!");
            System.out.println("--------------------------------");
        }catch(Exception e){
            System.err.println(e.getMessage());
        }
    }
    public void updateUsermaps(){
        try {
            Socket socketEP = new Socket(entryPointIp, entryPointPort);
            ObjectOutputStream outObject = new ObjectOutputStream(socketEP.getOutputStream());
            Protocol protocol = Protocol.UPDATE;
            outObject.writeObject(protocol);
            outObject.flush();

            ObjectInputStream inObject = new ObjectInputStream(socketEP.getInputStream());
            Map<String, Integer> receivedPortMap = (ConcurrentHashMap<String, Integer>) inObject.readObject();
            //System.out.println("Received hashmap from server: " + receivedPortMap);
            PKI.portUserMap.clear();
            PKI.portUserMap.putAll(receivedPortMap);

            Map<String, PublicKey> receivedPKMap = (ConcurrentHashMap<String, PublicKey>) inObject.readObject();
            //System.out.println("Received hashmap from server: " + receivedPKMap);
            PKI.PKusermap.clear();
            PKI.PKusermap.putAll(receivedPKMap);

            Map<String, String> receivedIpMap = (ConcurrentHashMap<String, String>) inObject.readObject();
            //System.out.println("Received hashmap from server: " + receivedIpMap);
            PKI.ipUserMap.clear();
            PKI.ipUserMap.putAll(receivedIpMap);

            System.out.println("--------------------------------");
            System.out.println("Users in the network:");
            for (String key : PKI.ipUserMap.keySet()) {
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
            Socket socketEP = new Socket(entryPointIp, entryPointPort);
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
