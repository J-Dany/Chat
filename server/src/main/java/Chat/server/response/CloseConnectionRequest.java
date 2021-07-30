package Chat.server.response;

import Chat.Logger;
import Chat.server.Client;
import Chat.server.exception.CloseConnectionException;
import Chat.LogMessage;

/**
 * @author Daniele Castiglia
 * @version 1.0.0
 */
public class CloseConnectionRequest implements Request
{
    @Override
    public void handle(Client client, Logger logger) throws Exception 
    {
        logger.addMsg(LogMessage.info("Disconnection request from " + client.getAddress() + " (" + client.getUsername() + ")"));
        
        throw new CloseConnectionException(client.getUsername());
    }
}