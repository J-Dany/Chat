package Chat.server.response;

import Chat.Logger;
import Chat.server.Client;

/**
 * @author Daniele Castiglia
 * @version 1.0.0
 */
public interface Request
{
    /**
     * Method that handle the request
     * 
     * @param client the client that sended this request
     * @param logger the server logger
     * @throws Exception
     */
    public void handle(Client client, Logger logger) throws Exception;
}