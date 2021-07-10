package Chat.server.exception;

/**
 * @author Daniele Castiglia
 * @version 1.0.0
 */
public class ClientAlreadyConnectedException extends Exception
{
    public ClientAlreadyConnectedException(String username, String ip)
    {
        super("The client with the following username, '" + username + "', is already connected (" + ip + ")");
    }
}