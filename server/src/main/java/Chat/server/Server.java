package Chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import Chat.LogMessage;
import Chat.Logger;
import Chat.console.Console;
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
     * whitin a minute before being muted
     * (used for preventing spam)
     */
    public static final int MAX_MESSAGE_BEFORE_MUTE = Integer.parseInt(Dotenv.load().get("MAX_MESSAGE_BEFORE_MUTE"));
    
    /**
     * Same as MAX_MESSAGE_BEFORE_MUTE, but this
     * time it refers to BAN
     */
    public static final int MAX_MESSAGE_BEFORE_BAN = Integer.parseInt(Dotenv.load().get("MAX_MESSAGE_BEFORE_BAN"));

    /**
     * An HashMap containing all banned
     * client. The string param refers to
     * username, while the client param refers
     * to the object
     */
    private HashMap<String, Client> banned;

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
            }
            catch (Exception e)
            {
                ////////////////////////////////////
                // If logger is not set, print    //
                // in standard output             //
                ////////////////////////////////////
                if (this.logger != null)
                {
                    this.logger.addMsg(LogMessage.error(e.getMessage()));
                }
                else
                {
                    e.printStackTrace();
                }
            }
        }
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
     */
    public void close() throws IOException, InterruptedException
    {
        this.logger.addMsg(LogMessage.info("Closing the server"));
        this.threadPool.shutdown();
        this.socket.close();
        this.interrupt();
        this.console.interrupt();
        this.console.join();
    }
}