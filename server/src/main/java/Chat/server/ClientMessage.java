package Chat.server;

import org.json.JSONObject;
import Chat.server.exception.FieldNotFoundException;

/**
 * @author Daniele Castiglia
 * @version 1.5.0
 */
public class ClientMessage
{
    /**
     * JSON sent
     */
    private JSONObject json;

    /**
     * Types of message
     */
    public enum TypeOfMessage {
        ////////////////////////////////////
        // Types of request               //
        // client -> server               //
        ////////////////////////////////////
        FOR_GROUP,
        FOR_PRIVATE,
        FOR_LOGIN,
        FOR_CLOSE_CONNECTION,
        ////////////////////////////////////
        // Types of info message          //
        // server -> client               //
        ////////////////////////////////////
        FOR_SERVER_CLOSING,
        FOR_FRIEND_LIST,
        FOR_DISCONNECTION,
        FOR_NEW_CONNECTION
    }

    /**
     * Types of message content
     */
    public enum TypeOfContent {
        PLAIN,
        CODE
    }

    /**
     * Default constructor
     * @param webSocketMessage the message received from the socket
     * @throws Exception
     */
    public ClientMessage(WebSocketMessage webSocketMessage) throws Exception
    {
        this.json = new JSONObject(webSocketMessage.decodeData());
    }

    /**
     * Constructor for normal JSON message 
     * (normal => data not encoded as web socket message)
     * @param socketMessage
     */
    public ClientMessage(String socketMessage)
    {
        this.json = new JSONObject(socketMessage);
    }

    /**
     * Returns the type of message sended
     * 
     * @return TypeOfMessage
     * @throws FieldNotFoundException
     */
    public TypeOfMessage getTypeOfMessage() throws FieldNotFoundException
    {
        if (!json.has("Type"))
        {
            throw new FieldNotFoundException("Type");
        }

        return TypeOfMessage.valueOf(json.getString("Type"));
    }

    /**
     * Returns the entire json received
     * @return the json received
     */
    public String getRawString()
    {
        return this.json.toString();
    }
}