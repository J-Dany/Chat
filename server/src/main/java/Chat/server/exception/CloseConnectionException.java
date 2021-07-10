package Chat.server.exception;

/**
 * @author Daniele Castiglia
 * @version 1.0.0
 */
public class CloseConnectionException extends Exception
{
    public CloseConnectionException(String username)
    {
        super("Close connection request for " + username);
    }
}
