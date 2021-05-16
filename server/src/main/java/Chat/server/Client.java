package Chat.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import Chat.server.database.*;

/**
 * Class that represent a Client
 * 
 * @author Daniele Castiglia
 * @version 1.2.3
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
    private OutputStream writer;

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
        this.writer = s.getOutputStream();
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
     * Get the username
     * 
     * @return String
     */
    public String getUsername()
    {
        return this.username;
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
        WebSocketMessage wbm = new WebSocketMessage(msg);
        wbm.encodeMessage();

        this.writer.write(wbm.getEncodedMessage());
        this.writer.flush();
    }

    /**
     * This metod sends the message to
     * this client.
     * 
     * USE ONLY FOR CONNECTION
     * 
     * @param buffer the buffer containing the message
     * @throws IOException
     * @see sendMessage(String msg)
     */
    public void sendMessage(byte[] buffer) throws IOException
    {
        this.writer.write(buffer);
        this.writer.flush();
    }

    /**
     * Notify to friends of this client
     * that has connected to the chat
     */
    public void notifyOnlineToFriend() throws SQLException, IOException
    {
        Connection connection = DatabaseConnection.getConnection();

        Statement stmt = connection.createStatement();

        String query = "SELECT `user2` as `friend` FROM friends WHERE `user1` = '" + this.username + "';";

        ResultSet result = stmt.executeQuery(query);

        while (result.next())
        {
            Client friend = Server.server.getClient(result.getString("friend"));

            friend.sendMessage(Message.newConnection(this.username));
        }
    }

    /**
     * Blocking method, returing the red message
     * 
     * @return red message
     * @throws Exception
     */
    public byte[] listenForIncomingMessage() throws Exception
    {
        byte[] buffer = new byte[1024];
        int l = this.socket.getInputStream().read(buffer);
        
        return Arrays.copyOfRange(buffer, 0, l);
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
     * Close the connection for this client
     * 
     * @throws IOException
     */
    public void closeConnection() throws IOException
    {
        this.socket.close();
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