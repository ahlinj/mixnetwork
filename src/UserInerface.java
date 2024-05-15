import java.util.Scanner;

public class UserInerface {
    private final Scanner sc;

    public UserInerface() {
        this.sc = new Scanner(System.in);
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
