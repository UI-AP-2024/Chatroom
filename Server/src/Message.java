import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class Message implements Serializable {
    private String content;
    private boolean isPublic;
    private LocalTime time;
    private Client sentBy;
    private boolean owner;

    public Message(String content, Client sentBy, boolean owner, boolean isPublic) throws ParseException {
        this.content = content;
        this.isPublic = isPublic;
        this.sentBy = sentBy;
        this.time = LocalTime.parse(LocalTime.now().getHour() + ":" + LocalTime.now().getMinute() + ":" + LocalTime.now().getSecond());
        this.owner = owner;
    }
}
