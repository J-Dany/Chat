package Chat.server.exceptions;

public class ClientAlreadyConnected extends Exception
{
    public ClientAlreadyConnected(String username, String ip)
    {
        super("The client with the following username, '" + username + "', is already connected (" + ip + ")");
    }
}