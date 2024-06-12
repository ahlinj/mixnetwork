import java.security.PublicKey;
import java.util.Scanner;

public class UserInerface {
    private final Scanner sc;

    public UserInerface() {
        this.sc = new Scanner(System.in);
    }

    public void messageExchange(Client client){
        String yesNo = sendMessage();

        if (yesNo.equals("1")) {
            String rec = enterReceiver();
            String mes = enterMessage();
            PublicKey recPK = PKI.PKusermap.get(rec);
            Message message = new Message(mes,Main.username.get(),"-1");

            if(recPK != null) {
                try {
                    message = Cryptography.encrypt(message,recPK);
                    message = Message.addRouteInfo(message,PKI.portUserMap.get(rec).toString());
                    client.sendMessage(message,PKI.portUserMap.get("EP"));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }else{
                System.out.println("Receiver not in the network");
                messageExchange(client);
            }

        } else if (yesNo.equals("2")) {
            client.updateUsermaps();
        } else {
            System.out.println("Please answer with one of possible numbers.");
            messageExchange(client);
        }
    }

    public String enterUsername(){
        System.out.println("Enter your username: ");
        return sc.nextLine();
    }

    public String sendMessage(){
        System.out.println("What do you want to do?");
        System.out.println("1: Send message");
        System.out.println("2: Show users in the network");
        return sc.nextLine();
    }

    public String enterMessage(){
        System.out.println("Enter message: ");
        return sc.nextLine();
    }

    public String enterReceiver(){
        System.out.println("Enter receiver: ");
        return sc.nextLine();
    }


}
