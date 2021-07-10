package Chat.server.message.info;

import Chat.server.ClientMessage.TypeOfMessage;
import Chat.server.message.Message;

/**
 * @author Daniele Castiglia
 * @version 1.0.0
 */
public class LoginMessage extends Message
{
    private boolean Status;
    private int Id;

    public LoginMessage(boolean status, int id)
    {
        this.Type = TypeOfMessage.FOR_LOGIN;

        this.Status = status;

        if (status)
        {
            this.Id = id;
        }
    }
}