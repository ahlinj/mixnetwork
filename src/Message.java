import java.security.PublicKey;

public class Message {
    long timestamp;
    PublicKey sender;
    String body;
    Protocol header;



    public Message(String body, PublicKey pk, Protocol header) {
        this.header = header;
        this.body = body;
        this.sender = pk;
        this.timestamp = System.currentTimeMillis();
    }
}
