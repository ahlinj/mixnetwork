package EntryPoint;

import Common.Protocol;

import java.io.*;
import java.net.Socket;

public class EntryPointHandlerEP extends Thread{
    private Socket clientSocket;

    public EntryPointHandlerEP(Socket clientSocket) {
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

            PKIEP.PKusermap.put(clientMessageUsername, PKIEP.stringToPublicKey(clientMessagePublicKey));
            PKIEP.portUserMap.put(clientMessageUsername, Integer.parseInt(clientMessagePort));
            PKIEP.ipUserMap.put(clientMessageUsername,clientMessageIp);

            //System.out.println("Username: " + clientMessageUsername);
            //System.out.println("Users.EntryPointListener port: " + clientMessagePort);
            //System.out.println("Public key: " + clientMessagePublicKey);
            //System.out.println("Ip: " + clientMessageIp);

            //send hashmaps
            sendMaps();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void updateMaps(){
        try {
            sendMaps();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void sendMaps() throws IOException {
        ObjectOutputStream outObject = new ObjectOutputStream(clientSocket.getOutputStream());
        outObject.writeObject(PKIEP.portUserMap);
        outObject.writeObject(PKIEP.PKusermap);
        outObject.writeObject(PKIEP.ipUserMap);
        outObject.flush();
        clientSocket.close();
    }

    private void removeFromMaps(ObjectInputStream in) {
        try {
            String removeUsername = in.readUTF();
            PKIEP.portUserMap.remove(removeUsername);
            PKIEP.PKusermap.remove(removeUsername);
            PKIEP.ipUserMap.remove(removeUsername);
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
