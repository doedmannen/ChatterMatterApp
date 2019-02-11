package Common;

import java.io.Serializable;

public class Message implements Serializable {
    public final char type;
    public final User sender;
    public final User reciever;
    public final String MSG;
    public Message(char type, User sender, User reciever, String MSG){
        this.type = type;
        this.sender = sender;
        this.reciever = reciever;
        this.MSG = MSG;
    }
}
