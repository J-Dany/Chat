package Chat.server.exceptions;

/**
 * @author Daniele Castiglia
 * @version 1.0.0
 */
public class CloseConnection extends Exception
{
    public CloseConnection(String username)
    {
        super("Close connection request for " + username);
    }
}
