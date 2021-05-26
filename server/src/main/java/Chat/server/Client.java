package Chat.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import org.json.JSONObject;
import Chat.server.database.*;
import Chat.server.exceptions.FriendNotFound;

/**
 * Class that represent a Client
 * 
 * @author Daniele Castiglia
 * @version 1.3.4
 */
public class Client 
{
    /**
     * The IP of the client
     */
    private InetAddress address;

    /**
     * The id of the user in the database
     */
    private int id;

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
     * Sets the id of this client
     * 
     * @param id
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * Returns the id of this client
     * 
     * @return int
     */
    public int getId()
    {
        return this.id;
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
     * As {@code notifyOnlineToFriend} but for
     * disconnection
     */
    public void notifyDisconnectionToFriend() throws SQLException, IOException
    {
        Connection connection = DatabaseConnection.getConnection();

        Statement stmt = connection.createStatement();

        String query = "SELECT `users`.`username` as `friend` " +
            " FROM friends " +
            " INNER JOIN `users` " +
            " ON `users`.`id_user` = `friends`.`user2`" +
            " WHERE `friends`.`user1` = " + this.id + ";";

        ResultSet result = stmt.executeQuery(query);

        while (result.next())
        {
            if (Server.server.isOnline(result.getString("friend")))
            {
                Client friend = Server.server.getClient(result.getString("friend"));

                friend.sendMessage(Message.disconnection(this.username));
            }
        }
    }

    /**
     * Notify to friends of this client
     * that has connected to the chat
     */
    public void notifyOnlineToFriend() throws SQLException, IOException
    {
        Connection connection = DatabaseConnection.getConnection();

        Statement stmt = connection.createStatement();

        String query = "SELECT `users`.`username` as `friend` " +
            " FROM friends " +
            " INNER JOIN `users` " +
            " ON `users`.`id_user` = `friends`.`user2`" +
            " WHERE `friends`.`user1` = " + this.id + ";";

        ResultSet result = stmt.executeQuery(query);

        while (result.next())
        {
            if (Server.server.isOnline(result.getString("friend")))
            {
                Client friend = Server.server.getClient(result.getString("friend"));

                friend.sendMessage(Message.newConnection(this.username));
            }
        }
    }

    /**
     * Send a message to a friend
     * *
     * @param friend friend of this client
     * @param msg the message
     * @throws SQLException
     * @throws FriendNotFound
     * @throws IOException
     */
    public void sendToFriend(String friend, String msg) throws SQLException, FriendNotFound, IOException
    {
        if (!Server.server.isOnline(friend))
        {
            return;
        }

        Connection connection = DatabaseConnection.getConnection();

        Statement stmt = connection.createStatement();

        String query = "SELECT COUNT(*) as num_rows FROM friends WHERE `user1` = " + this.id + " AND `user2` = " + Server.server.getClient(friend).id + ";";

        ResultSet result = stmt.executeQuery(query);

        if (result.next() && result.getInt("num_rows") == 1)
        {
            if (Server.server.isOnline(friend))
            {
                Client f = Server.server.getClient(friend);

                f.sendMessage(msg);
            }

            return;
        }

        throw new FriendNotFound(this.username, friend);
    }

    /**
     * Send the list of friend
     */
    public void sendListOfFriend() throws SQLException, IOException
    {
        ArrayList<JSONObject> friends = new ArrayList<>();

        Connection connection = DatabaseConnection.getConnection();

        Statement stmt = connection.createStatement();
        Statement stmt1 = connection.createStatement();

        String query = "SELECT users.id_user as friend_id, users.username as friend, users.photo as photo FROM friends" +
        " INNER JOIN users ON friends.user2 = users.id_user " +
        " WHERE `user1` = " + this.id + ";";

        ResultSet result = stmt.executeQuery(query);

        while (result.next())
        {
            JSONObject json = new JSONObject();

            String query1 = "SELECT * FROM messages WHERE sender = " + this.id + " AND addresse = " + result.getInt("friend_id") + 
            " OR sender = " + result.getInt("friend_id") + " AND addresse = " + this.id + 
            " ORDER BY data LIMIT 1;";

            ResultSet result1 = stmt1.executeQuery(query1);

            json.put("Name", result.getString("friend"));
            json.put("IdFriend", result.getInt("friend_id"));
            json.put("Online", Server.server.isOnline(result.getString("friend")));
            json.put("Photo", result.getString("photo"));

            if (result1.next())
            {
                json.put("LastMessage", result1.getString("message"));
            }

            friends.add(json);
        }

        this.sendMessage(Message.listOfFriend(friends));
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