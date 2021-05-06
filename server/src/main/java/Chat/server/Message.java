package Chat.server;

import org.json.JSONObject;
import Chat.server.exceptions.FieldNotFound;

/**
 * @author Daniele Castiglia
 * @version 1.0.0
 */
public class Message
{
    private JSONObject json;

    public enum TypeOfMessage {
        FOR_GROUP,
        FOR_PRIVATE
    }

    public Message(WebSocketMessage webSocketMessage)
    {
        this.json = new JSONObject(webSocketMessage.getDecodedData());
    }

    public TypeOfMessage getTypeOfMessage() throws FieldNotFound
    {
        if (!json.has("Type"))
        {
            throw new FieldNotFound("Type");
        }

        return TypeOfMessage.valueOf(json.getString("Type"));
    }

    public String getMessage() throws FieldNotFound
    {
        if (!json.has("Message"))
        {
            throw new FieldNotFound("Message");
        }

        return json.getString("Message");
    }

    public String getRawString()
    {
        return this.json.toString();
    }
}