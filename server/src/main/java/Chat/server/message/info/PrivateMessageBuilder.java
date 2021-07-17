package Chat.server.message.info;

/**
 * @author Daniele Castiglia
 * @version 1.0.1
 */
public class PrivateMessageBuilder
{
    private String Sender;
    private String Message;
    private String Data;
    private String HM;
    private String Content;
    private String Language;

    public PrivateMessageBuilder() { }

    public PrivateMessageBuilder setSender(String Sender) 
    {
        this.Sender = Sender;

        return this;
    }

    public PrivateMessageBuilder setMessage(String Message) 
    {
        this.Message = Message;

        return this;
    }

    public PrivateMessageBuilder setData(String Data) 
    {
        this.Data = Data;

        return this;
    }

    public PrivateMessageBuilder setHM(String HM) 
    {
        this.HM = HM;

        return this;
    }

    public PrivateMessageBuilder setContent(String Content) 
    {
        this.Content = Content;

        return this;
    }

    public PrivateMessageBuilder setLanguage(String Language)
    {
        this.Language = Language;

        return this;
    }

    public PrivateMessage build()
    {
        return new PrivateMessage(Content, Language, Sender, Message, Data, HM);
    }
}