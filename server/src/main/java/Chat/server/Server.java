package Chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import Chat.LogMessage;
import Chat.Logger;
import Chat.console.Console;
import Chat.gui.ServerGUI;
import Chat.server.exception.ClientAlreadyConnectedException;
import Chat.server.message.info.ClosingServerMessage;
import io.github.cdimascio.dotenv.*;

/**
 * The message server. Here were all the message
 * and connection will be processed
 * 
 * @author Daniele Castiglia
 * @version 1.3.0
 */
public class Server extends Thread
{
    /**
     * If a port is not provided, by default
     * the server will listen in this port
     */
    private final int PORT = 60000;

    /**
     * The size of the thread pool
     */
    private final int THREAD_POOL_SIZE = Integer.parseInt(Dotenv.load().get("MAX_USERS"));

    /**
     * A constant indicating the max number
     * of message that this client can send
     * within a minute before being muted
     * (used for preventing spam)
     */
    public static final int MAX_MESSAGE_BEFORE_MUTE = Integer.parseInt(Dotenv.load().get("MAX_MESSAGE_BEFORE_MUTE"));
    
    /**
     * Same as MAX_MESSAGE_BEFORE_MUTE, but this
     * time it refers to BAN
     */
    public static final int MAX_MESSAGE_BEFORE_BAN = Integer.parseInt(Dotenv.load().get("MAX_MESSAGE_BEFORE_BAN"));

    /**
     * Reference to the instance of the server
     */
    public static Server server = null;

    /**
     * An HashMap containing all banned
     * client. The string param refers to
     * username, while the client param refers
     * to the object
     */
    private HashMap<String, Client> banned;

    /**
     * The graphical interface for the
     * server
     */
    private ServerGUI gui;

    /**
     * The list of logs for this instance
     */
    private ArrayList<LogMessage> logs;

    /**
     * An HashMap containing all client
     * authenticated and connected
     */
    private HashMap<String, Client> connected;

    /**
     * Socket used by the server to
     * write and read messages
     */
    private ServerSocket socket;

    /**
     * Server console
     */
    private Console console;

    /**
     * The reference to the Main
     * logger
     */
    private Logger logger = null;

    /**
     * This thread pool will handle
     * all connection
     */
    private ExecutorService threadPool;
    
    /**
     * Constructor
     * 
     * @param port the port where the server will listen on
     * @throws IOException
     */
    public Server(int port) throws IOException
    {
        this.setName("Server");
        this.socket = new ServerSocket(port);
        this.banned = new HashMap<>();
        this.threadPool = Executors.newFixedThreadPool(this.THREAD_POOL_SIZE);
        this.connected = new HashMap<>();
        this.gui = new ServerGUI();
        this.logs = new ArrayList<>();
        server = this;
    }

    /**
     * The default constructor. If you don't
     * provide a port, the program will use
     * the port 60000
     * 
     * @throws IOException
     */
    public Server() throws IOException
    {
        this.setName("Server");
        this.socket = new ServerSocket(this.PORT);
        this.banned = new HashMap<>();
        this.threadPool = Executors.newFixedThreadPool(this.THREAD_POOL_SIZE);
        this.connected = new HashMap<>();
        this.gui = new ServerGUI();
        this.logs = new ArrayList<>();
        server = this;
    }

    @Override
    public void run()
    {
        this.console = new Console(this, this.logger);

        ////////////////////////////////////
        // Starts the console             //
        ////////////////////////////////////
        this.console.start();

        ////////////////////////////////////
        // While this thread is not       //
        // interrupted...                 //
        ////////////////////////////////////
        while(!Thread.interrupted())
        {
            try
            {
                this.logger.addMsg(LogMessage.info("Listening for incoming connection"));
                
                ////////////////////////////////////
                // Accepts connections            //
                ////////////////////////////////////
                Socket s = this.socket.accept();
                
                this.logger.addMsg(LogMessage.ok("Connection accepted for " + s.getInetAddress()));

                this.threadPool.submit(new Session(new Client(s, s.getInetAddress()), this.logger));
            }
            catch (Exception e)
            {
                ////////////////////////////////////
                // If logger is not set, print    //
                // in standard output             //
                ////////////////////////////////////
                if (this.logger != null)
                {
                    this.logger.addMsg(LogMessage.error(e.toString()));
                }
                else
                {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Check if a client is online
     * @param username
     * @return true if the client is online, false otherwise
     */
    public boolean isOnline(String username)
    {
        return this.connected.containsKey(username);
    }

    /**
     * Get client by username
     * 
     * @param username the username of the client
     * @return Client
     */
    public Client getClient(String username)
    {
        return this.connected.get(username);
    }

    /**
     * Upgrade logs for this instance
     * @param msg
     */
    public void upgradeLogs(LogMessage msg)
    {
        this.logs.add(msg);

        if (this.gui.isOn())
        {
            this.gui.upgradeLogs(msg);
        }
    }

    /**
     * Starts the graphical interface
     */
    public void startGui()
    {
        try
        {
            String[] args = {};

            gui.setOn(true);
            gui.main(args);

            gui = new ServerGUI();
        }
        catch (Exception e)
        {
            this.logger.addMsg(LogMessage.error(e.toString()));
        }
    }

    /**
     * When a client connects and authenticated, the thread
     * that handle that connection will call this method
     * 
     * @param username the username of authenticated client
     * @param client the reference to the client created when he connected
     * @throws ClientAlreadyConnectedException
     */
    public void addNewConnectedClient(String username, Client client) throws ClientAlreadyConnectedException
    {
        if (this.connected.containsKey(username))
        {
            throw new ClientAlreadyConnectedException(username, this.connected.get(username).getAddress().toString());
        }
        
        this.connected.put(username, client);
        
        if (this.gui.isOn())
        {
            this.gui.upgradeListUsers(client);
        }
    }

    /**
     * Removes a connected client
     * @param username the username of the client disconnected
     */
    public void removeConnectedClient(String username)
    {
        if (this.gui.isOn())
        {
            this.gui.removeUser(this.connected.get(username));
        }

        this.connected.remove(username);
    }

    /**
     * Returns the list of connected clients.
     * Method used by Console
     * 
     * @return the list of connected clients
     */
    public Client[] getConnectedClients()
    {
        return this.connected.values().toArray(new Client[0]);
    }

    /**
     * The logger where the server will be
     * write messages
     * 
     * @param logger
     */
    public void setLogger(Logger logger)
    {
        this.logger = logger;
    }

    /**
     * Release the resource
     * 
     * @throws IOException
     * @throws InterruptedException
     */
    public void close() throws IOException, InterruptedException
    {
        this.logger.addMsg(LogMessage.info("Closing the server"));

        for (Client c : this.connected.values())
        {
            c.sendMessage(new ClosingServerMessage());
            c.closeConnection();
        }

        this.threadPool.shutdown();

        this.socket.close();
        
        this.interrupt();
        
        this.console.interrupt();
        this.console.join(500);
    }
}