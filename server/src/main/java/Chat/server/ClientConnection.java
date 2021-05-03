package Chat.server;

import Chat.LogMessage;
import Chat.Logger;

/***
 * @author Daniele Castiglia
 * @version 1.0.0
 */
public class ClientConnection implements Runnable
{
    /**
     * Reference to the client
     */
    private Client client;

    /**
     * Reference to the loggger
     */
    private Logger logger;

    /**
     * Constrructor
     * @param c the client that this connection will handle
     */
    public ClientConnection(Client c, Logger log)
    {
        this.client = c;
        this.logger = log;
    }
    
    @Override
    public void run()
    {
        while (true)
        {
            try
            {
                String msg = this.client.listenForIncomingMessage();
                
                logger.addMsg(LogMessage.ok("Message received from " + this.client.getUsername()));

                System.out.println(msg);
            }
            catch (Exception e)
            {
                logger.addMsg(LogMessage.error(e.getMessage()));
            }
        }
    }
}