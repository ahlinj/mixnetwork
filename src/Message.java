import java.io.Serializable;
import java.security.PublicKey;

public class Message implements Serializable {
    long timestamp;
    PublicKey sender;
    String body;
    String route;



    public Message(String body, PublicKey pk, String route) {
        this.body = body;
        this.sender = pk;
        this.timestamp = System.currentTimeMillis();
        this.route = route;
    }
}
