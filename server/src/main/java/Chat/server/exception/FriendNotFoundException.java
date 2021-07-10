package Chat.server.exception;

/**
 * @author Daniele Castiglia
 * @version 1.0.0
 */
public class FriendNotFoundException extends Exception
{
    public FriendNotFoundException(String client, String friend)
    {
        super(client + " has no friend with this username: " + friend);
    }
}
