package Users;

import java.io.*;
import java.net.Socket;
import java.security.PrivateKey;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class PeerHandler extends Thread{
    private Socket clientSocket;
    private PrivateKey privateKey;

    public PeerHandler(Socket clientSocket, PrivateKey privateKey) {
        this.clientSocket = clientSocket;
        this.privateKey = privateKey;
    }

    @Override
    public void run() {
        try {
            handleMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleMessage() throws IOException {
        //System.out.println("Connected from port: " + serverSocket.getLocalPort() + " to port: " + clientSocket.getPort());
        ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
        try {
            Message message = null;
            while(message == null){
                message = (Message) in.readObject();
                //System.out.println("Received message: " + message.body);
                //System.out.println("Route: "+message.route);

                message = Message.deleteRouteInfo(message);
                //System.out.println("Received message: " + message.body);
                //System.out.println("Route: "+message.route);

                message = Cryptography.decrypt(message, privateKey);
                //System.out.println("Received message: " + message.body);
                //System.out.println("Route: "+message.route);
                if(!message.route.equals("-1")) {
                    String sendTo = message.route.split("::")[0];
                    Socket socket = new Socket(sendTo, Constants.SERVICE_PORT);
                    ObjectOutputStream outObject = new ObjectOutputStream(socket.getOutputStream());
                    outObject.writeObject(message);
                    outObject.flush();
                    socket.close();
                }else{
                    //System.out.println("Ending time: "+System.currentTimeMillis());
                    System.out.println("--------------------------------");
                    System.out.println("YOU HAVE RECEIVED A MESSAGE:");
                    System.out.println("Received message: "+Message.removePadding(message.body));
                    System.out.println("Sender: "+message.sender);
                    System.out.println("Timestamp: "+ LocalDateTime.ofInstant(Instant.ofEpochMilli(message.timestamp), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    System.out.println("--------------------------------");
                    System.out.println("What do you want to do?");
                    System.out.println("1: Send message");
                    System.out.println("2: Show users in the network");
                    System.out.println("3: Leave the network and close the program");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        clientSocket.close();
    }

}
