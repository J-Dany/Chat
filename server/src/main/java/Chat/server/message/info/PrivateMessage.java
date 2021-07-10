package Chat.server.message.info;

import Chat.server.ClientMessage.TypeOfContent;
import Chat.server.ClientMessage.TypeOfMessage;
import Chat.server.message.Message;

/**
 * @author Daniele Castiglia
 * @version 1.0.0
 */
public class PrivateMessage extends Message
{
    private String Sender, Message, Data, HM;
    private TypeOfContent Content;

    public PrivateMessage(TypeOfContent content, String sender, String message, String data, String HM)
    {
        this.Type = TypeOfMessage.FOR_PRIVATE;
        this.Content = content;
        this.Sender = sender;
        this.Message = message;
        this.Data = data;
        this.HM = HM;
    }
}