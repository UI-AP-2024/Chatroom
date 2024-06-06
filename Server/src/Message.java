import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.text.ParseException;
import java.time.LocalTime;

@Getter
@Setter
public class Message implements Serializable {
    private String content;
    private LocalTime time;
    private int sentByID;

    public Message(String content, int sentByID) throws ParseException {
        this.content = content;
        this.sentByID = sentByID;
        this.time = LocalTime.parse(LocalTime.now().getHour() + ":" + LocalTime.now().getMinute() + ":" + LocalTime.now().getSecond());

    }
}
