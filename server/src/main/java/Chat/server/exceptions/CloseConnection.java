package Chat.server.exceptions;

public class CloseConnection extends Exception
{
    public CloseConnection(String username)
    {
        super("Close connection request for " + username);
    }
}
