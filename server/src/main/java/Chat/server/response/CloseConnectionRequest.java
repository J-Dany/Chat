package Chat.server.response;

import Chat.server.Client;
import Chat.server.exception.CloseConnectionException;
import Chat.Logger;

/**
 * @author Daniele Castiglia
 * @version 1.0.0
 */
public class CloseConnectionRequest implements Request
{
    @Override
    public void handle(Client client) throws Exception 
    {
        Logger.info("Disconnection request from " + client.getAddress() + " (" + client.getUsername() + ")");
        
        throw new CloseConnectionException(client.getUsername());
    }
}