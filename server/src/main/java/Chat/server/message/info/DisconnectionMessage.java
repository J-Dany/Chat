package Chat.server.message.info;

import Chat.server.ClientMessage.TypeOfMessage;
import Chat.server.message.Message;

/**
 * @author Daniele Castiglia
 * @version 1.0.0
 */
public class DisconnectionMessage extends Message
{
    private String Username;

    public DisconnectionMessage(String username)
    {
        this.Type = TypeOfMessage.FOR_DISCONNECTION;
        this.Username = username;
    }
}