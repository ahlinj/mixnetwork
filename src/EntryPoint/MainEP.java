package EntryPoint;

import java.io.IOException;
import java.net.*;
import java.util.*;
public class MainEP {
    public static String myIp;
    public static final ThreadLocal<ServerSocket> serverSocket = new ThreadLocal<>();
    public static final ThreadLocal<Integer> port = new ThreadLocal<>();

    public static void main(String[] args) throws Exception {
        MainEP.findMyIp();
        MainEP.initializeServerSocket(ConstantsEP.SERVICE_PORT);
        EntryPointListenerEP entryPointListener = new EntryPointListenerEP(MainEP.serverSocket.get());
        entryPointListener.start();
/*
        while (true) {
            try {
                Thread.sleep(4000);
                System.out.println("PortUserMap:");
                for (Map.Entry<String, Integer> entry : Users.PKI.portUserMap.entrySet()) {
                    System.out.println(entry.getKey() + ": " + entry.getValue());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(4000);
                System.out.println("LayerUserMap:");
                for (Map.Entry<String, Integer> entry : Users.PKI.layerUserMap.entrySet()) {
                    System.out.println(entry.getKey() + ": " + entry.getValue());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(4000);
                System.out.println("PublicKeyUserMap:");
                for (Map.Entry<String, PublicKey> entry : Users.PKI.PKusermap.entrySet()) {
                    System.out.println(entry.getKey() + ": " + entry.getValue());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
*/
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

    public static void initializeServerSocket(int portNum) {
        try {
            ServerSocket ss = new ServerSocket(portNum);
            serverSocket.set(ss);
            port.set(ss.getLocalPort());
            //System.out.println("Your port: " + ss.getLocalPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
