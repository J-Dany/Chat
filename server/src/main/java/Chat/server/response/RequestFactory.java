package Chat.server.response;

import com.google.gson.Gson;
import Chat.server.ClientMessage.TypeOfMessage;
import Chat.server.message.request.LoginRequestMessage;
import Chat.server.message.request.PrivateMessageRequestMessage;

/**
 * @author Daniele Castiglia
 * @version 1.0.0
 */
public class RequestFactory 
{
    public static Request getResponse(TypeOfMessage type, String rawData)
    {
        switch (type)
        {
            case FOR_LOGIN:
                return new LoginRequest(new Gson().fromJson(rawData, LoginRequestMessage.class));
            case FOR_CLOSE_CONNECTION:
                return new CloseConnectionRequest();
            case FOR_PRIVATE:
                return new PrivateMessageRequest(new Gson().fromJson(rawData, PrivateMessageRequestMessage.class));
            default:
                return null;
        }
    }
}