

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
        this.time = LocalTime.parse((String.valueOf(LocalTime.now().getHour()).length() != 1 ? LocalTime.now().getHour() : "0" + LocalTime.now().getHour()) + ":" + (String.valueOf(LocalTime.now().getMinute()).length() != 1 ? LocalTime.now().getMinute() : "0" + LocalTime.now().getMinute()) + ":" + (String.valueOf(LocalTime.now().getMinute()).length() != 1 ? LocalTime.now().getMinute() : "0" + LocalTime.now().getMinute()) );
    }
}
