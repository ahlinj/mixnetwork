import java.security.PublicKey;
import java.util.Scanner;

public class UserInerface {
    private final Scanner sc;

    public UserInerface() {
        this.sc = new Scanner(System.in);
    }

    public Message messageExchange(){
        String yesNo = sendMessage();

        if (yesNo.equals("Y")) {
            String rec = enterReceiver();
            String mes = enterMessage();
            PublicKey recPK = PKI.PKusermap.get(rec);
            Message message = new Message(mes,PKI.PKusermap.get(Main.username.get()),"-1");

            if(recPK != null) {
                try {
                    message = Cryptography.encrypt(message,recPK);
                    message = Message.addRouteInfo(message,PKI.portUserMap.get(rec).toString());
                    return message;

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }else{
                System.out.println("Receiver not in the network");
                messageExchange();
            }

        } else if (yesNo.equals("N")) {
            //Main.closeServerSocket();
        } else {
            System.out.println("Please answer with Y or N");
            messageExchange();
        }
        return null;
    }

    public String enterUsername(){
        System.out.println("Enter your username: ");
        return sc.nextLine();
    }

    public String sendMessage(){
        System.out.println("Do you want to send the message? (Y/N)");
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
