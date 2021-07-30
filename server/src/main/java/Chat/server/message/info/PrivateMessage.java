package Chat.server.message.info;

import Chat.server.ClientMessage.TypeOfMessage;
import Chat.server.message.Message;

/**
 * @author Daniele Castiglia
 * @version 1.0.1
 */
public class PrivateMessage extends Message
{
    private String UniqueId;
    private String Sender;
    private String Message;
    private String Data;
    private String HM;
    private String Content;
    private String Language;

    /**
     * Default ctor
     * 
     * @param content content of the message, not the message itself but the type (PLAIN or CODE)
     * @param language the language of the message (C, C++ for code, null for plain)
     * @param sender the name of the sender
     * @param message the message
     * @param data data of the message
     * @param HM hour and minute of the message
     */
    public PrivateMessage(String UniqueId, String content, String language, String sender, String message, String data, String HM)
    {
        this.UniqueId = UniqueId;
        this.Type = TypeOfMessage.FOR_PRIVATE;
        this.Content = content;
        this.Sender = sender;
        this.Message = message;
        this.Data = data;
        this.HM = HM;
    }
}