package Chat.server.message.request;

import Chat.server.ClientMessage.TypeOfMessage;
import Chat.server.message.Message;

/**
 * @author Daniele Castiglia
 * @version 1.0.0
 */
public class LoginRequestMessage extends Message
{
    private String Username;
    private String Password;

    public LoginRequestMessage() 
    { 
        this.Type = TypeOfMessage.FOR_LOGIN;
    }

    public String getUsername() 
    {
        return this.Username;
    }

    public void setUsername(String Username) 
    {
        this.Username = Username;
    }

    public String getPassword() 
    {
        return this.Password;
    }

    public void setPassword(String Password) 
    {
        this.Password = Password;
    }
}