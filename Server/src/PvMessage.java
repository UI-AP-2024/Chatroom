import lombok.Getter;
import lombok.Setter;

import java.text.ParseException;

@Getter
@Setter
public class PvMessage extends Message{
    private int sentToID;
    public PvMessage(String content, int sentByIDm, int sentToID) throws ParseException {
        super(content, sentByIDm);
        this.sentToID = sentToID;
    }
}
