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
    private String Name;
    private String Photo;
    private String LastMessage;
    private int IdFriend;
    private boolean Online;

    public Friend() { }

    public String getName() 
    {
        return this.Name;
    }

    public void setName(String Name) 
    {
        this.Name = Name;
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

    public int getIdFriend() 
    {
        return this.IdFriend;
    }

    public void setIdFriend(int IdFriend) 
    {
        this.IdFriend = IdFriend;
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