package Chat.server;

import org.json.JSONObject;
import Chat.server.exceptions.FieldNotFound;

/**
 * @author Daniele Castiglia
 * @version 1.1.0
 */
public class Message
{
    /**
     * JSON sended
     */
    private JSONObject json;

    /**
     * Types of message
     */
    public enum TypeOfMessage {
        FOR_GROUP,
        FOR_PRIVATE,
        FOR_LOGIN
    }

    /**
     * Default constructor
     * @param webSocketMessage the message received from the socket
     * @throws Exception
     */
    public Message(WebSocketMessage webSocketMessage) throws Exception
    {
        webSocketMessage.decodeData();
        this.json = new JSONObject(webSocketMessage.getDecodedData());
    }

    /**
     * Returns the type of message sended
     * 
     * @return TypeOfMessage
     * @throws FieldNotFound
     */
    public TypeOfMessage getTypeOfMessage() throws FieldNotFound
    {
        if (!json.has("Type"))
        {
            throw new FieldNotFound("Type");
        }

        return TypeOfMessage.valueOf(json.getString("Type"));
    }

    /**
     * Returns the password
     * 
     * @return String
     * @throws FieldNotFound
     */
    public String getPassword() throws FieldNotFound
    {
        if (!json.has("Password"))
        {
            throw new FieldNotFound("Password");
        }

        return json.getString("Password");
    }

    /**
     * Returns the content of the message
     * 
     * @return the message
     * @throws FieldNotFound
     */
    public String getMessage() throws FieldNotFound
    {
        if (!json.has("Message"))
        {
            throw new FieldNotFound("Message");
        }

        return json.getString("Message");
    }

    /**
     * Returns the sender of the message
     * 
     * @return the sender
     * @throws FieldNotFound
     */
    public String getSender() throws FieldNotFound
    {
        if (!json.has("Sender"))
        {
            throw new FieldNotFound("Sender");
        }

        return json.getString("Sender");
    }

    /**
     * Returns the entire json received
     * @return the json received
     */
    public String getRawString()
    {
        return this.json.toString();
    }

    /**
     * Returns a login response message
     * 
     * @param status true (login ok) or false (login failed)
     * @return String
     */
    public static String login(boolean status)
    {
        JSONObject json = new JSONObject();

        json.put("Type", TypeOfMessage.FOR_LOGIN);
        json.put("Status", status);

        return json.toString();
    }
}