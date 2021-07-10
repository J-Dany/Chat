package Chat.server.message.info;

import Chat.server.ClientMessage.TypeOfMessage;
import Chat.server.message.Message;

/**
 * @author Daniele Castiglia
 * @version 1.0.0
 */
public class NewConnectionMessage extends Message
{
    private String Username;

    public NewConnectionMessage(String username)
    {
        this.Type = TypeOfMessage.FOR_NEW_CONNECTION;
        this.Username = username;
    }
}