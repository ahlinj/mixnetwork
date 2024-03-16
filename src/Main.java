import java.io.IOException;
import java.net.*;
import java.security.*;
import java.util.Enumeration;
import java.util.Map;

public class Main {
    public static String myIp;
    public static final ThreadLocal<String> username = new ThreadLocal<>();
    public static final ThreadLocal<ServerSocket> serverSocket = new ThreadLocal<>();
    public static final ThreadLocal<Integer> port = new ThreadLocal<>();
    public static final ThreadLocal<PrivateKey> prKey = new ThreadLocal<>();

    public static void main(String[] args) throws Exception {
        //FOR ENTRY POINT
/*
        Main.findMyIp();

        Main.username.set("EntryPoint");
        Main.initializeServerSocket();

        Server server = new Server(Main.serverSocket.get());
        server.start();

         while (true){
            try {
                Thread.sleep(4000);
                System.out.println("PortUserMap:");
                for (Map.Entry<String, Integer> entry : PKI.PortUserMap.entrySet()) {
                    System.out.println(entry.getKey() + ": " + entry.getValue());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }



 */

        //FOR USERS


        Main.findMyIp();
        Main.username.set("User3");
        Main.initializeServerSocket();

        Server server = new Server(Main.serverSocket.get());
        server.start();

        Client client = new Client(Main.username.get());
        client.start();



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
            PKI.addUserPort(username.get(), ss.getLocalPort());
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
                System.out.println("Server socket closed for current thread.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
