import java.io.*;
import java.net.Socket;
import java.security.PublicKey;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Client extends Thread{

    int entryPointPort = 55234;

    private final String userID;
    private final int serverSocketPort;


    public Client(String username, int serverSocketPort) {
        this.userID = username;
        this.serverSocketPort = serverSocketPort;
    }

    @Override
    public void run(){
        handleConnection();
    }

    private void handleConnection() {
        try {
            Socket socketEP = new Socket("localhost", entryPointPort);
            System.out.println("Connected from port: " + socketEP.getLocalPort() + " to port: " + entryPointPort);

            PrintWriter out = new PrintWriter(socketEP.getOutputStream(), true);
            ObjectInputStream inObject = new ObjectInputStream(socketEP.getInputStream());

            //send your information to entry point
            out.println(userID+":"+serverSocketPort+":"+PKI.setPrKeyGetPubKey());

            //receive hashmaps from entry point when first connected
            Map<String, Integer> receivedPortMap = (ConcurrentHashMap<String, Integer>) inObject.readObject();
            System.out.println("Received hashmap from server: " + receivedPortMap);
            PKI.portUserMap.putAll(receivedPortMap);

            Map<String, Integer> receivedLayerMap = (ConcurrentHashMap<String, Integer>) inObject.readObject();
            System.out.println("Received hashmap from server: " + receivedLayerMap);
            PKI.layerUserMap.putAll(receivedLayerMap);

            Map<String, PublicKey> receivedPKMap = (ConcurrentHashMap<String, PublicKey>) inObject.readObject();
            System.out.println("Received hashmap from server: " + receivedPKMap);
            PKI.PKusermap.putAll(receivedPKMap);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    public void sendMessage(Message message){
        try {
            int peerPort = message.portReceiver;
            if(peerPort != serverSocketPort && peerPort != entryPointPort){
                Socket socket = new Socket("localhost", peerPort);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                out.writeObject(message);
                out.flush();
                socket.close();
                System.out.println("Message sent to port " + peerPort);
            }else{
                System.out.println("Peer not found.");
            }
        }catch(IOException e){
            System.err.println(e.getMessage());
        }
    }
}
