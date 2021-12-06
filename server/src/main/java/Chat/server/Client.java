package Chat.server;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import Chat.server.exception.FriendNotFoundException;
import Chat.server.message.Message;
import Chat.server.message.info.DisconnectionMessage;
import Chat.server.message.info.ListOfFriendMessage;
import Chat.server.message.info.NewConnectionMessage;
import Chat.server.pojo.Friend;

/**
 * Class that represent a Client
 * 
 * @author Daniele Castiglia
 * @version 1.5.0
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
     * The socket assigned for this
     * client
     */
    private ThatClientSocket socket;

    /**
     * Flag indicating if this client
     * is a web client or not
     */
    private boolean isWebClient = false;

    /**
     * Friends
     */
    private HashMap<String, Friend> friends;

    /**
     * Buffer that contains the last
     * received data (max size: 2048)
     */
    private byte[] lastReceivedData;

    /**
     * Constructor
     * 
     * @param s socket of the connection
     * @throws IOException
     */
    public Client(ThatClientSocket s) throws IOException
    {
        this.socket = s;
        this.address = s.getInetAddress();
    }

    /**
     * Set the client friends
     * @param friends
     */
    public void setFriends(HashMap<String, Friend> friends)
    {
        this.friends = friends;
    }

    /**
     * Set the username of this client
     */
    public void setUsername(String username)
    {
        this.username = username;
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
     * Set the type of the socket
     * as a web client socket
     */
    public void webClient()
    {
        this.isWebClient = true;
        this.socket.setIsWebSocket(true);
    }
    
    /**
     * 
     * @return
     */
    public boolean isWebClient()
    {
        return this.isWebClient;
    }

    /**
     * Gets the last received data
     * @return
     */
    public byte[] getLastReceivedData()
    {
        return this.lastReceivedData;
    }

    /**
     * This method sends the message to
     * this client
     * 
     * @param msg the message to sent
     * @throws IOException
     */
    public void sendMessage(Message msg) throws IOException
    {
        this.socket.send(msg);
    }

    /**
     * This method sends the message to
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
        this.socket.connectionMessage(buffer);
    }

    /**
     * As {@code notifyOnlineToFriend} but for
     * disconnection
     */
    public void notifyDisconnectionToFriend()
    {
        this.friends.values().forEach(friend -> {
            if (Server.server.isOnline(friend.getUsername()))
            {
                Client f = Server.server.getClient(friend.getUsername());

                try
                {
                    f.sendMessage(new DisconnectionMessage(this.username));
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Notify to friends of this client
     * that has connected to the chat
     */
    public void notifyOnlineToFriend()
    {
        this.friends.values().forEach(friend -> {
            if (Server.server.isOnline(friend.getUsername()))
            {
                Client f = Server.server.getClient(friend.getUsername());

                try
                {
                    f.sendMessage(new NewConnectionMessage(this.username));
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Returns one friend of this client
     * @return
     */
    public Friend getFriend(String friendUsername) throws FriendNotFoundException
    {
        if (!this.friends.containsKey(friendUsername))
        {
            throw new FriendNotFoundException(this.username, friendUsername);
        }

        return this.friends.get(friendUsername);
    }

    /**
     * Send a message to a friend
     * *
     * @param friendUsername friend of this client
     * @param msg the message
     * @throws SQLException
     * @throws FriendNotFoundException
     * @throws IOException
     */
    boolean found = false;
    public void sendToFriend(String friendUsername, Message msg) throws FriendNotFoundException, IOException
    {
        if (!Server.server.isOnline(friendUsername))
        {
            return;
        }

        if (!this.friends.containsKey(friendUsername))
        {
            throw new FriendNotFoundException(this.username, friendUsername);
        }

        if (Server.server.isOnline(friendUsername))
        {
            Client f = Server.server.getClient(friendUsername);

            f.sendMessage(msg);
        }
    }

    /**
     * Send the list of friend
     */
    public void sendListOfFriend() throws IOException
    {
        ArrayList<Friend> friends = new ArrayList<Friend>(this.friends.values());
        this.sendMessage(new ListOfFriendMessage(friends));
    }

    /**
     * Blocking method, returning the red message
     * 
     * @return red message
     * @throws Exception
     */
    public byte[] listenForIncomingMessage() throws Exception
    {
        byte[] buffer = new byte[2048];
        int l = this.socket.getInputStream().read(buffer);
        
        this.lastReceivedData = Arrays.copyOfRange(buffer, 0, l);
        return this.lastReceivedData;
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

    @Override
    public String toString()
    {
        return this.username + " [" + this.address + "]";
    }
}