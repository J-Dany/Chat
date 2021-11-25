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
import javafx.application.Application;

/**
 * The message server. Here were all the message
 * and connection will be processed
 * 
 * @author Daniele Castiglia
 * @version 1.4.0
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
     * Reference to the instance of the server
     */
    public static Server server = null;

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
        this.threadPool = Executors.newFixedThreadPool(this.THREAD_POOL_SIZE);
        this.connected = new HashMap<>();
        this.gui = new ServerGUI();
        this.logs = new ArrayList<>();
        server = this;
    }

    @Override
    public void run()
    {
        this.console = new Console();

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
                Logger.info("Listening for incoming connection");

                ////////////////////////////////////
                // Accepts connections            //
                ////////////////////////////////////
                Socket s = this.socket.accept();

                if (this.connected.size() >= THREAD_POOL_SIZE)
                {
                    Logger.ok("Connection refused for " + s.getInetAddress() + "(reached the maximum connection)");
                    s.close();

                    continue;
                }

                Logger.ok("Connection accepted for " + s.getInetAddress());

                this.threadPool.submit(new Session(new Client(s)));
            }
            catch (Exception e)
            {
                Logger.error(e.getMessage());
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
     * Upgrade logs for this run
     * 
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
     * Retrieve the last log message
     * for this run
     * 
     * @return LogMessage
     */
    public LogMessage getLastLog()
    {
        if (this.logs.isEmpty())
        {
            return null;
        }

        return this.logs.get(this.logs.size() - 1);
    }

    /**
     * Returns the number of logs for this run
     * 
     * @return int
     */
    public int logsSize()
    {
        return this.logs.size();
    }

    /**
     * Returns a log message at specified index
     * 
     * @param index index of the log message
     * @return LogMessage
     */
    public LogMessage getLogMessageAt(int index)
    {
        try
        {
            return this.logs.get(index);
        }
        catch (IndexOutOfBoundsException e)
        {
            Logger.error(e.toString());
        }

        return null;
    }

    /**
     * Update the GUI with the new
     * exiting data
     * 
     * @param data the data
     */
    public void updateExitingData(String data)
    {
        if (gui.isOn())
        {
            gui.updateExitingData(data);
        }
    }

    /**
     * Starts the graphical interface
     */
    public void startGui()
    {
        try
        {
            String[] args = new String[0];

            Application.launch(ServerGUI.class, args);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Logger.error(e.toString());
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
     * Release the resource
     * 
     * @throws IOException
     * @throws InterruptedException
     */
    public void close() throws IOException, InterruptedException
    {
        Logger.info("Closing the server");

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