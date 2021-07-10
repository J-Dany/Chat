package Chat.server.message.info;

import Chat.server.ClientMessage.TypeOfMessage;
import Chat.server.message.Message;

/**
 * @author Daniele Castiglia
 * @version 1.0.0
 */
public class ClosingServerMessage extends Message
{
    public ClosingServerMessage()
    {
        this.Type = TypeOfMessage.FOR_SERVER_CLOSING;
    }
}