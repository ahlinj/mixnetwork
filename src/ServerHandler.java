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
                case UPDATE -> updateMaps();
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
            System.out.println("Received message: " + message.body);
            System.out.println("Route: "+message.route);

            String k1 = PKI.getRandomEntryFromMap(PKI.PKusermap).getKey();
            System.out.println("Second encryption user: "+k1);
            message = Cryptography.encrypt(message,PKI.PKusermap.get(k1));
            message = Message.addRouteInfo(message,PKI.portUserMap.get(k1).toString());

            String k2 = PKI.getRandomEntryFromMap(PKI.PKusermap).getKey();
            System.out.println("Third encryption user: "+k2);
            message = Cryptography.encrypt(message,PKI.PKusermap.get(k2));
            message = Message.addRouteInfo(message,PKI.portUserMap.get(k2).toString());
            Socket socket = new Socket("localhost", PKI.portUserMap.get(k2));
            ObjectOutputStream outObject = new ObjectOutputStream(socket.getOutputStream());
            outObject.writeObject(message);
            outObject.flush();

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
            outObject.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
