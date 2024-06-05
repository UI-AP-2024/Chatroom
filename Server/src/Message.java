import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Time;

@Getter
@Setter
public class Message implements Serializable {
    private String content;
    private boolean value;
    private long time;
    private Client sentBy;

    public Message(String content, boolean value, Client sentBy) {
        this.content = content;
        this.value = value;
        this.sentBy = sentBy;
        this.time = System.currentTimeMillis();
    }
}
