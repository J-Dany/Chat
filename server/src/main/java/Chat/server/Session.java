package Chat.server;

import java.net.SocketException;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import Chat.Logger;
import Chat.server.exception.CloseConnectionException;
import Chat.server.response.LoginRequest;
import Chat.server.response.Request;
import Chat.server.response.RequestFactory;

/***
 * @author Daniele Castiglia
 * @version 1.0.2
 */
public class Session implements Runnable
{
    /**
     * Reference to the client
     */
    private Client client;

    /**
     * Constructor
     * @param c the client that this connection will handle
     */
    public Session(Client c)
    {
        this.client = c;
    }
    
    @Override
    public void run()
    {
        this.manageConnection();
        this.manageLogin();

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
                
                Logger.ok("Message received from " + this.client.getAddress());

                ClientMessage msg = null;

                ////////////////////////////////////
                // Try to read the message, if    //
                // you can't just ignore it and   //
                // listen again for incoming      //
                // message                        //
                ////////////////////////////////////
                try
                {
                    if (client.isWebClient())
                    {
                        msg = new ClientMessage(new WebSocketMessage(buffer));
                    }
                    else
                    {
                        msg = new ClientMessage(new String(buffer, 0, buffer.length, "UTF-8"));
                    }
                }
                catch (Exception e)
                {
                    Logger.error(e);
                    continue;
                }

                ////////////////////////////////////
                // Instantiating the response for //
                // the request                    //
                ////////////////////////////////////
                Request request = RequestFactory.getResponse(msg.getTypeOfMessage(), msg.getRawString());

                if (request instanceof LoginRequest)
                {
                    throw new Exception(this.client.getUsername() + " is trying to login while is already logged in");
                }

                Logger.info("Handler instantiated for " + this.client.getAddress() + ": " + request.getClass().getName());

                ////////////////////////////////////
                // Handle the request             //
                ////////////////////////////////////
                request.handle(this.client);

                Logger.ok("Request handled for " + this.client.getUsername());
            }
            ////////////////////////////////////
            // Exception for closing the      //
            // connection                     //
            // (THIS IS NOT AN ERROR)         //
            ////////////////////////////////////
            catch (CloseConnectionException e)
            {
                Logger.info(e.toString());
                break;
            }
            ////////////////////////////////////
            // In case of bad close           //
            // connection request             //
            ////////////////////////////////////
            catch (IllegalArgumentException | SocketException e)
            {
                Logger.error(e);
                break;
            }
            catch (Exception e)
            {
                Logger.error(e);
            }
        }

        ////////////////////////////////////
        // Notify to friend the           //
        // disconnection                  //
        ////////////////////////////////////
        try
        {
            client.notifyDisconnectionToFriend();
        }
        catch (Exception e)
        {
            Logger.error(e);
        }

        ////////////////////////////////////
        // Remove the client from the     //
        // server                         //
        ////////////////////////////////////
        Logger.ok("Connection closed for " + this.client.getAddress() + " (" + this.client.getUsername() + ")");
        Server.server.removeConnectedClient(client.getUsername());
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
            byte[] buffer = client.listenForIncomingMessage();
            String msg = new String(buffer, 0, buffer.length, "UTF-8");

            Matcher get = Pattern.compile("^GET").matcher(msg);

            if (get.find())
            {
                Logger.info("Detect web socket. Creating the response connection");

                Matcher match = Pattern.compile("Sec-WebSocket-Key: (.*)").matcher(msg);

                match.find();

                byte[] response = ("HTTP/1.1 101 Switching Protocols\r\n"
                    + "Connection: Upgrade\r\n"
                    + "Upgrade: websocket\r\n"
                    + "Sec-WebSocket-Accept: "
                    + Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-1").digest((match.group(1) + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes("UTF-8")))
                    + "\r\n\r\n").getBytes("UTF-8");

                client.sendMessage(response);

                Logger.ok("Response created and sent to " + client.getAddress());

                client.webClient();
            }
            else
            {
                Logger.info("Detect normal socket.");
            }
        }
        catch (Exception e)
        {
            Logger.error(e);
        }
    }

    /**
     * Manage the login
     */
    private void manageLogin()
    {
        ClientMessage msg = null;

        try
        {
            if (this.client.isWebClient())
            {
                byte[] buffer = this.client.listenForIncomingMessage();
                msg = new ClientMessage(new WebSocketMessage(buffer));
            }
            else
            {
                byte[] buffer = client.getLastReceivedData();
                msg = new ClientMessage(new String(buffer, 0, buffer.length, "UTF-8"));
            }

            Request request = RequestFactory.getResponse(msg.getTypeOfMessage(), msg.getRawString());

            request.handle(this.client);
        }
        catch (Exception e)
        {
            Logger.error(e);
        }
    }
}