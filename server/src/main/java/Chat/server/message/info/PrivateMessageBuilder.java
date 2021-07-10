package Chat.server.message.info;

import Chat.server.ClientMessage.TypeOfContent;

/**
 * @author Daniele Castiglia
 * @version 1.0.0
 */
public class PrivateMessageBuilder
{
    private String Sender, Message, Data, HM;
    private TypeOfContent Content;

    public PrivateMessageBuilder() { }

    public PrivateMessageBuilder setSender(String sender)
    {
        this.Sender = sender;

        return this;
    }

    public PrivateMessageBuilder setMessage(String message)
    {
        this.Message = message;

        return this;
    }

    public PrivateMessageBuilder setData(String data)
    {
        this.Data = data;

        return this;
    }

    public PrivateMessageBuilder setContent(TypeOfContent content)
    {
        this.Content = content;

        return this;
    }

    public PrivateMessageBuilder setHM(String HM)
    {
        this.HM = HM;

        return this;
    }

    public PrivateMessage build()
    {
        return new PrivateMessage(Content, Sender, Message, Data, HM);
    }
}