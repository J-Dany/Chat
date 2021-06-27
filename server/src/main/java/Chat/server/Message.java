package Chat.server;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import Chat.server.exceptions.FieldNotFound;

/**
 * @author Daniele Castiglia
 * @version 1.4.0
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
     * Get types of content
     * 
     * @return TypeOfMessage
     * @throws FieldNotFound
     */
    public TypeOfContent getTypeOfContent() throws FieldNotFound
    {
        if (!json.has("Content"))
        {
            throw new FieldNotFound("Content");
        }

        return TypeOfContent.valueOf(json.getString("Content"));
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
     * (ONLY FOR PRIVATE OR GROUP MESSAGE)
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
     * Returns the addresse of the message
     * 
     * (ONLY FOR PRIVATE MESSAGE)
     * 
     * @return String
     * @throws FieldNotFound
     */
    public String getAddresse() throws FieldNotFound
    {
        if (!json.has("Addresse"))
        {
            throw new FieldNotFound("Addresse");
        }

        return json.getString("Addresse");
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
     * Returns the data of the message
     * 
     * (ONLY FOR PRIVATE OR GROUP MESSAGE)
     * 
     * @return
     * @throws FieldNotFound
     */
    public String getData() throws FieldNotFound
    {
        if (!json.has("Data"))
        {
            throw new FieldNotFound("Data");
        }

        return json.getString("Data");
    }

    /**
     * Returns the HM field (hour and minute)
     * 
     * @return String
     * @throws FieldNotFound
     */
    public String getHM() throws FieldNotFound
    {
        if (!json.has("HM"))
        {
            throw new FieldNotFound("HM");
        }

        return json.getString("HM");
    }

    /**
     * Returns the entire json received
     * @return the json received
     */
    public String getRawString()
    {
        return this.json.toString();
    }

    /////////////////////////////////////////////////////////////////////////////////
    // Response message                                                            //
    //                                                                             //
    /////////////////////////////////////////////////////////////////////////////////

    /**
     * Returns a login response message
     * 
     * @param status true (login ok) or false (login failed)
     * @return String
     */
    public static String login(boolean status, int id)
    {
        JSONObject json = new JSONObject();

        json.put("Type", TypeOfMessage.FOR_LOGIN);
        json.put("Status", status);
        json.put("Id", id);

        return json.toString();
    }

    /**
     * Returns a login response message
     * 
     * (ONLY FOR FAILED LOGIN)
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

    /**
     * Returns a new connection response message
     * 
     * @param username the connected client
     * @return String
     */
    public static String newConnection(String username)
    {
        JSONObject json = new JSONObject();

        json.put("Type", TypeOfMessage.FOR_NEW_CONNECTION);
        json.put("Username", username);

        return json.toString();
    }

    /**
     * 
     */
    public static String disconnection(String username)
    {
        JSONObject json = new JSONObject();

        json.put("Type", TypeOfMessage.FOR_DISCONNECTION);
        json.put("Username", username);

        return json.toString();
    }

    /**
     * Returns a list of friend response message
     * 
     * @param friends the list of friends
     * @return String
     */
    public static String listOfFriend(ArrayList<JSONObject> friends)
    {
        JSONObject json = new JSONObject();

        json.put("Type", TypeOfMessage.FOR_FRIEND_LIST);
        json.put("Friends", new JSONArray(friends));

        return json.toString();
    }

    /**
     * Build a private message response
     * 
     * @param sender the sender
     * @param message the message
     * @param hM the hour and the minute when the client sent the message
     * @return String
     */
    public static String privateMessage(String sender, String message, String hM, TypeOfContent contentType)
    {
        JSONObject json = new JSONObject();

        json.put("Type", TypeOfMessage.FOR_PRIVATE);
        json.put("Content", contentType);
        json.put("Sender", sender);
        json.put("Message", message);
        json.put("Data", hM);

        return json.toString();
    }

    /**
     * Build a closing server message response
     * 
     * @return String
     */
    public static String closingServer()
    {
        JSONObject json = new JSONObject();

        json.put("Type", TypeOfMessage.FOR_SERVER_CLOSING);

        return json.toString();
    }
}