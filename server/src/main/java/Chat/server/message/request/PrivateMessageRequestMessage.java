package Chat.server.message.request;

import Chat.server.message.Message;

/**
 * @author Daniele Castiglia
 * @version 1.1.0
 */
public class PrivateMessageRequestMessage extends Message
{
    private String Content;
    private String Language;
    private String Message;
    private String Addresse;
    private String HM;
    private String Data;

    public PrivateMessageRequestMessage() { }

    public String getContent() 
    {
        return this.Content;
    }

    public void setContent(String Content) 
    {
        this.Content = Content;
    }

    public String getLanguage() 
    {
        return this.Language;
    }

    public void setLanguage(String Language) 
    {
        this.Language = Language;
    }

    public String getMessage() 
    {
        return this.Message;
    }

    public void setMessage(String Message) 
    {
        this.Message = Message;
    }

    public String getAddresse() 
    {
        return this.Addresse;
    }

    public void setAddresse(String Addresse) 
    {
        this.Addresse = Addresse;
    }

    public String getHM() {
        return this.HM;
    }

    public void setHM(String HM) 
    {
        this.HM = HM;
    }

    public String getData() {
        return this.Data;
    }

    public void setData(String Data) 
    {
        this.Data = Data;
    }
}