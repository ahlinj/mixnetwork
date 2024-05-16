import java.io.IOException;
import java.net.*;
import java.security.*;
import java.util.*;

public class Main {
    public static String myIp;
    public static final ThreadLocal<String> username = new ThreadLocal<>();
    public static final ThreadLocal<ServerSocket> serverSocket = new ThreadLocal<>();
    public static final ThreadLocal<Integer> port = new ThreadLocal<>();
    public static final ThreadLocal<PrivateKey> prKey = new ThreadLocal<>();

    public static void main(String[] args) throws Exception {
        UserInerface userInerface = new UserInerface();
        if (args[0].equals("EP")) {
            Main.findMyIp();
            Main.username.set(args[0]);
            Main.initializeServerSocket();

            Server server = new Server(Main.serverSocket.get());
            server.start();


            while (true) {
                try {
                    Thread.sleep(4000);
                    System.out.println("PortUserMap:");
                    for (Map.Entry<String, Integer> entry : PKI.portUserMap.entrySet()) {
                        System.out.println(entry.getKey() + ": " + entry.getValue());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(4000);
                    System.out.println("LayerUserMap:");
                    for (Map.Entry<String, Integer> entry : PKI.layerUserMap.entrySet()) {
                        System.out.println(entry.getKey() + ": " + entry.getValue());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(4000);
                    System.out.println("PublicKeyUserMap:");
                    for (Map.Entry<String, PublicKey> entry : PKI.PKusermap.entrySet()) {
                        System.out.println(entry.getKey() + ": " + entry.getValue());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } else {
            Main.findMyIp();
            Main.username.set(userInerface.enterUsername());
            Main.initializeServerSocket();
            ClientListener clientListener = new ClientListener(Main.serverSocket.get(), Main.username.get());
            clientListener.start();
            Client client = new Client(Main.username.get(), Main.port.get());
            client.start();


            Thread.sleep(1500);
            userInerface.messageExchange();

        }
    }

    public static void findMyIp(){
        try {
            Enumeration e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                NetworkInterface inter = (NetworkInterface) e.nextElement();
                if (inter.isUp() && !inter.isLoopback()) {
                    Enumeration addr = inter.getInetAddresses();
                    while (addr.hasMoreElements()) {
                        InetAddress address = (InetAddress) addr.nextElement();
                        myIp = address.getHostAddress().replace("/", "");
                        if (myIp.length() <= 15) {
                            System.out.println("LocalIp: " + myIp);
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
    }

    public static void initializeServerSocket() {
        try {
            ServerSocket ss = new ServerSocket(0);
            serverSocket.set(ss);
            port.set(ss.getLocalPort());
            PKI.portUserMap.put(username.get(),ss.getLocalPort());
            System.out.println("Server started on port: " + ss.getLocalPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void closeServerSocket() {
        try {
            ServerSocket ss = serverSocket.get();
            if (ss != null && !ss.isClosed()) {
                ss.close();
                System.out.println("Server socket closed for thread: "+ss.getLocalPort());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
