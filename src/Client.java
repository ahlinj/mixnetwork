import java.io.*;
import java.net.Socket;
import java.security.PublicKey;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Client extends Thread{

    private final String userID;
    private final int serverSocketPort;

    public Client(String username, int serverSocketPort) {
        this.userID = username;
        this.serverSocketPort = serverSocketPort;
    }

    @Override
    public void run(){
        int entryPointPort = 65238;
        try {
            Socket socket = new Socket("localhost", entryPointPort);
            System.out.println("Connected from port: " + socket.getLocalPort() + " to port: " + entryPointPort);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            ObjectInputStream inObject = new ObjectInputStream(socket.getInputStream());

            //send your information to entry point
            out.println(userID+":"+serverSocketPort+":"+PKI.setPrKeyGetPubKey(userID));

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

            //connect to peers
            for (Map.Entry<String, Integer> entry : PKI.portUserMap.entrySet()) {
                String peerUserID = entry.getKey();
                int peerPort = entry.getValue();
                //dont connect to yourself and entrypoint
                if (!peerUserID.equals(userID) && peerPort != serverSocketPort && peerPort != entryPointPort) {
                    try {
                        Socket peerSocket = new Socket("localhost", peerPort);
                        System.out.println("Connected to peer " + peerUserID + " on port " + peerPort);




                    } catch (IOException e) {
                        System.err.println("Failed to connect to peer " + peerUserID + " on port " + peerPort);
                        e.printStackTrace();
                    }
                }
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
