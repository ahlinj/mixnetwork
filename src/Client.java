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
        int entryPointPort = 64816;
        try {
            Socket socket = new Socket("localhost", entryPointPort);
            System.out.println("Connected from port: " + socket.getLocalPort() + " to port: " + entryPointPort);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            ObjectInputStream inObject = new ObjectInputStream(socket.getInputStream());

            out.println(userID+":"+serverSocketPort+":"+PKI.setPrKeyGetPubKey(userID));

            Map<String, Integer> receivedPortMap = (ConcurrentHashMap<String, Integer>) inObject.readObject();
            System.out.println("Received hashtable from server: " + receivedPortMap);
            PKI.portUserMap.putAll(receivedPortMap);

            Map<String, Integer> receivedLayerMap = (ConcurrentHashMap<String, Integer>) inObject.readObject();
            System.out.println("Received hashtable from server: " + receivedLayerMap);
            PKI.layerUserMap.putAll(receivedLayerMap);

            Map<String, PublicKey> receivedPKMap = (ConcurrentHashMap<String, PublicKey>) inObject.readObject();
            System.out.println("Received hashtable from server: " + receivedPKMap);
            PKI.PKusermap.putAll(receivedPKMap);

            socket.close();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
