import lombok.Getter;
import lombok.Setter;

import java.text.ParseException;
import java.time.LocalTime;

@Getter
@Setter
public class PvMessage extends Message{
    private int sentToID;
    public PvMessage(String content, int sentByIDm, int sentToID) throws ParseException {
        super(content, sentByIDm);
        this.sentToID = sentToID;
    }

    public PvMessage(String content, int sentByID, int sentToID, String time) throws ParseException {
        super(content, sentByID, time);
        this.sentToID = sentToID;
    }
}
