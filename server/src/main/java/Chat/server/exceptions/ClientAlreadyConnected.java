package Chat.server.exceptions;

/**
 * @author Daniele Castiglia
 * @version 1.0.0
 */
public class ClientAlreadyConnected extends Exception
{
    public ClientAlreadyConnected(String username, String ip)
    {
        super("The client with the following username, '" + username + "', is already connected (" + ip + ")");
    }
}