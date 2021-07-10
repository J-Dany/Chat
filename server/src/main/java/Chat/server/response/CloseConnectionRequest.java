package Chat.server.response;

import Chat.Logger;
import Chat.server.Client;
import Chat.LogMessage;

/**
 * @author Daniele Castiglia
 * @version 1.0.0
 */
public class CloseConnectionRequest implements Request
{
    @Override
    public RequestReturnValues handle(Client client, Logger logger) throws Exception 
    {
        logger.addMsg(LogMessage.info("Disconnection request from " + client.getAddress() + " (" + client.getUsername() + ")"));
        
        return RequestReturnValues.CLOSE_CONNECTION;
    }
}