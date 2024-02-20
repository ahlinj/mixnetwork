import java.security.PublicKey;

public class Message {
    long timestamp;
    PublicKey sender;
    String body;


    public Message(String body, PublicKey pk) {
        this.body = body;
        this.sender = pk;
        this.timestamp = System.currentTimeMillis();
    }
}
