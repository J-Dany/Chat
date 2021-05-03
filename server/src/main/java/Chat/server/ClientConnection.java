package Chat.server;

import java.io.BufferedReader;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

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
        this.manageConnection();
        
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

    private void manageConnection()
    {
        try
        {
            String msg = this.client.listenForIncomingMessage();

            this.logger.addMsg(LogMessage.info("Creating the response connection"));

            for (String line : msg.split("\n"))
            {
                String l[] = line.split(":");

                try
                {
                    System.out.println("> " + l[0] + ": " + l[1]);
                }
                catch (Exception e) { }
            }

            Map<String, String> headers = Arrays.stream(msg.split("\n"))
                .map(s -> s.split(":"))
                .collect(Collectors.toMap(s -> s[0], new Function<String[],String>(){
                    @Override
                    public String apply(String[] arg0) {
                        try
                        {
                            return arg0[1].trim();
                        }
                        catch (IndexOutOfBoundsException e)
                        {
                            return "";
                        }
                    }
                }));

            String key = Base64.encodeBase64String(DigestUtils.sha1Hex(headers.get("Sec-WebSocket-Key") + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes());

            String response =
                "HTTP/1.1 101 Switching Protocols\r\n" +
                "Upgrade: websocket\r\n" +
                "Connection: Upgrade\r\n" +
                "Sec-WebSocket-Accept: " + key + "\r\n\r\n";

            this.client.sendMessage(response);

            this.logger.addMsg(LogMessage.ok("Response created and sent to " + this.client.getAddress()));
        }
        catch (Exception e)
        {
            this.logger.addMsg(LogMessage.error(e.getMessage()));
        }
    }
}