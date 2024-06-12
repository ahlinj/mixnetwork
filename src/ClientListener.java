import java.io.*;
import java.net.*;
import java.security.PrivateKey;

public class ClientListener extends Thread{

    private ServerSocket serverSocket;
    private String name;
    private PrivateKey privateKey;

    public ClientListener(ServerSocket serverSocket, String name, PrivateKey privateKey) {
        this.serverSocket = serverSocket;
        this.name = name;
        this.privateKey = privateKey;
    }

    @Override
    public void run() {
        while (true) {
            try {
                //System.out.println("Listening on: " + serverSocket.getLocalPort());
                Socket clientSocket = serverSocket.accept();
                handleConnection(clientSocket);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void handleConnection(Socket clientSocket) throws IOException {
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
                    int sendTo = Integer.parseInt(message.route.split("::")[0]);
                    Socket socket = new Socket("localhost", sendTo);
                    ObjectOutputStream outObject = new ObjectOutputStream(socket.getOutputStream());
                    outObject.writeObject(message);
                    outObject.flush();
                }else{
                    System.out.println("--------------------------------");
                    System.out.println("YOU HAVE RECEIVED A MESSAGE:");
                    System.out.println("Received message: "+message.body);
                    System.out.println("Sender: "+message.sender);
                    System.out.println("Timestamp: "+message.timestamp);
                    System.out.println("--------------------------------");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
