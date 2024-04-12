import java.io.*;
import java.net.Socket;
import java.security.PublicKey;
import java.util.HashMap;
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
        int entryPointPort = 53526;
            try {
                Socket socket = new Socket("localhost", entryPointPort);
                System.out.println("Connected from port: " + socket.getLocalPort() + " to port: " + entryPointPort);

                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                ObjectInputStream inObject = new ObjectInputStream(socket.getInputStream());

                out.println(userID+":"+serverSocketPort+":"+PKI.setPrKeyGetPubKey(userID));

                String response = in.readLine();
                System.out.println("Server says: " + response);
                Map<String, Integer> receivedPortMap = (ConcurrentHashMap<String, Integer>) inObject.readObject();
                System.out.println("Received hashtable from server: " + receivedPortMap);
                PKI.portUserMap.putAll(receivedPortMap);
                Map<String, Integer> receivedLayerMap = (ConcurrentHashMap<String, Integer>) inObject.readObject();
                System.out.println("Received hashtable from server: " + receivedLayerMap);
                PKI.layerUserMap.putAll(receivedLayerMap);
                Map<String, PublicKey> receivedPKMap = (ConcurrentHashMap<String, PublicKey>) inObject.readObject();
                System.out.println("Received hashtable from server: " + receivedPKMap);
                PKI.PKusermap.putAll(receivedPKMap);

                for (Map.Entry<String, Integer> entry : PKI.portUserMap.entrySet()) {
                    String key = entry.getKey();
                    Integer value = entry.getValue();
                    System.out.println("Key: " + key + ", Value: " + value);
                }



                UserInerface userInerface = new UserInerface();
                String receiver = userInerface.enterReceiver();
                String message = userInerface.enterMessage();

            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
    }

}