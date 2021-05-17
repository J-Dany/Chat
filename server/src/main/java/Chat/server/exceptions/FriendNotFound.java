package Chat.server.exceptions;

/**
 * @author Daniele Castiglia
 * @version 1.0.0
 */
public class FriendNotFound extends Exception
{
    public FriendNotFound(String client, String friend)
    {
        super(client + " has no friend with this username: " + friend);
    }
}
