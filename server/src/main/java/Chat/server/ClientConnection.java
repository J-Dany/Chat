package Chat.server;

import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Base64;
import Chat.LogMessage;
import Chat.Logger;

/***
 * @author Daniele Castiglia
 * @version 1.0.1
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
        ////////////////////////////////////
        // Handle connection              //
        ////////////////////////////////////
        this.manageConnection();
        
        while (true)
        {
            try
            {
                ////////////////////////////////////
                // Listen for incoming message    //
                // from the client that this      //
                // task handle                    //
                ////////////////////////////////////
                byte[] buffer = this.client.listenForIncomingMessage();

                Message msg = new Message(new WebSocketMessage(buffer));
                
                logger.addMsg(LogMessage.ok("Message received from " + this.client.getUsername()));

                //System.out.println(msg.getMessage());
            }
            catch (Exception e)
            {
                logger.addMsg(LogMessage.error(e.getMessage()));
            }
        }
    }

    /**
     * This method handle WebSocket connection (it is
     * a special handshake using HTTP protocol)
     * 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/WebSockets_API/Writing_a_WebSocket_server_in_Java
     */
    private void manageConnection()
    {
        try
        {
            byte[] buffer = this.client.listenForIncomingMessage();
            String msg = new String(buffer, 0, buffer.length, "UTF-8");

            this.logger.addMsg(LogMessage.info("Creating the response connection"));

            Matcher get = Pattern.compile("^GET").matcher(msg);

            if (get.find())
            {
                Matcher match = Pattern.compile("Sec-WebSocket-Key: (.*)").matcher(msg);

                match.find();

                byte[] response = ("HTTP/1.1 101 Switching Protocols\r\n"
                    + "Connection: Upgrade\r\n"
                    + "Upgrade: websocket\r\n"
                    + "Sec-WebSocket-Accept: "
                    + Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-1").digest((match.group(1) + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes("UTF-8")))
                    + "\r\n\r\n").getBytes("UTF-8");

                this.client.sendMessage(response);

                this.logger.addMsg(LogMessage.ok("Response created and sent to " + this.client.getAddress()));
            }
        }
        catch (Exception e)
        {
            this.logger.addMsg(LogMessage.error(e.getMessage()));
        }
    }
}