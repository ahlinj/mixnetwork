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
}
