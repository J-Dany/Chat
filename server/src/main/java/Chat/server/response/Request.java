package Chat.server.response;

import Chat.Logger;
import Chat.server.Client;
import Chat.server.Message;

/**
 * @author Daniele Castiglia
 * @version 1.0.0
 */
public interface Request
{
    enum RequestReturnValues {
        LOGIN_FAILED,
        LOGIN_OK
    }

    /**
     * Method that handle the request
     * 
     * @param msg the message received
     * @param client the client that sended this request
     * @param logger the server logger
     * @throws Exception
     */
    public RequestReturnValues handle(Message msg, Client client, Logger logger) throws Exception;
}