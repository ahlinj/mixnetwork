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
        int entryPointPort = 53018;
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

            //connect to peers
            for (Map.Entry<String, Integer> entry : PKI.portUserMap.entrySet()) {
                String peerUserID = entry.getKey();
                int peerPort = entry.getValue();
                //dont connect to yourself and entrypoint
                if (peerPort != serverSocketPort && peerPort != entryPointPort) {
                    System.out.println("Connecting to: "+peerPort);
                    Socket socket = new Socket("localhost",peerPort);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    System.out.println("Connected to: "+in.readLine());
                }
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
