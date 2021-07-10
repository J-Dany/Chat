package Chat.server.message.info;

import java.util.ArrayList;
import Chat.server.ClientMessage.TypeOfMessage;
import Chat.server.message.Message;
import Chat.server.pojo.Friend;

/**
 * @author Daniele Castiglia
 * @version 1.0.0
 */
public class ListOfFriendMessage extends Message
{
    private ArrayList<Friend> Friends;

    public ListOfFriendMessage(ArrayList<Friend> friends)
    {
        this.Type = TypeOfMessage.FOR_FRIEND_LIST;
        this.Friends = friends;
    }
}