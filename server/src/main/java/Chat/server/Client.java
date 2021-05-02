package Chat.server;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import io.github.cdimascio.dotenv.Dotenv;

/**
 * Class that represent a Client
 * 
 * @author Daniele Castiglia
 * @version 1.1.0
 */
public class Client 
{
    /**
     * The IP of the client
     */
    private InetAddress address;

    /**
     * The username of the client. This
     * will be set after an authentication
     * process
     */
    private String username;

    /**
     * A counter for ban or mute
     * this client
     * @see MAX_MESSAGE_BEFORE_MUTE
     */
    private int messageCounter;

    /**
     * The socket assigned for this
     * client
     */
    private Socket socket;

    /**
     * The object used to write into
     * the socket
     */
    private OutputStreamWriter writer;

    /**
     * Constructor
     * 
     * @param address client's IP
     * @throws IOException
     */
    public Client(Socket s, InetAddress address) throws IOException
    {
        this.address = address;
        this.socket = s;
        this.writer = new OutputStreamWriter(this.socket.getOutputStream());
    }

    /**
     * Set the username of this client
     */
    public void setUsername(String username)
    {
        this.username = username;
        this.messageCounter = 0;
    }

    /**
     * This metod sends the message to
     * this client
     * 
     * @param msg the message to sent
     * @throws IOException
     */
    public void sendMessage(String msg) throws IOException
    {
        this.writer.write(msg);
        this.writer.flush();
    }

    /**
     * Invoked every time this client
     * sent a message
     */
    public void newMessageSent()
    {
        ++this.messageCounter;
    }

    /**
     * Used to retrieve the message counter
     * of this client. The server will use this
     * metod to determine if can send, or mute or 
     * ban this client
     */
    public int getMessageCounter()
    {
        return this.messageCounter;
    }

    /**
     * Return the address (IP) of this client
     * @return InetAddress, the IP of this client
     */
    public InetAddress getAddress()
    {
        return this.address;
    }
}