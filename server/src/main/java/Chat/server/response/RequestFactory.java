package Chat.server.response;

import Chat.server.Message.TypeOfMessage;

/**
 * @author Daniele Castiglia
 * @version 1.0.0
 */
public class RequestFactory 
{
    public static Request getResponse(TypeOfMessage type)
    {
        switch (type)
        {
            case FOR_LOGIN:
                return new LoginRequest();
            case FOR_CLOSE_CONNECTION:
                return new CloseConnectionRequest();
            case FOR_PRIVATE:
                return new PrivateMessageRequest();
            default:
                return null;
        }
    }
}