package Chat.server.pojo;

/**
 * This class represent every friend object sent
 * to the client when the server deliver a
 * FOR_FRIEND_LIST message
 * 
 * @author Daniele Castiglia
 * @version 1.0.0
 */
public class Friend
{
    private String Username;
    private String Name;
    private String Surname;
    private String Photo;
    private String LastMessage;
    private String Content;
    private String Language;
    private int Id;
    private boolean Online;

    public Friend() { }

    public String getUsername()
    {
        return this.Username;
    }

    public void setUsername(String Username)
    {
        this.Username = Username;
    }

    public String getName() 
    {
        return this.Name;
    }

    public void setName(String Name) 
    {
        this.Name = Name;
    }

    public String getSurname()
    {
        return this.Surname;
    }

    public void setSurname(String Surname)
    {
        this.Surname = Surname;
    }

    public String getPhoto() 
    {
        return this.Photo;
    }

    public void setPhoto(String Photo) 
    {
        this.Photo = Photo;
    }

    public String getLastMessage() 
    {
        return this.LastMessage;
    }

    public void setLastMessage(String LastMessage) 
    {
        this.LastMessage = LastMessage;
    }

    public String getContent() {
        return this.Content;
    }

    public void setContent(String Content) 
    {
        this.Content = Content;
    }

    public String getLanguage() {
        return this.Language;
    }

    public void setLanguage(String Language) 
    {
        this.Language = Language;
    }

    public int getId() 
    {
        return this.Id;
    }

    public void setIdFriend(int Id) 
    {
        this.Id = Id;
    }

    public boolean isOnline() 
    {
        return this.Online;
    }

    public void setOnline(boolean Online) 
    {
        this.Online = Online;
    }   
}