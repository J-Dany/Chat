package Chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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
 * @version 1.2.0
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
                ////////////////////////////////////
                // Accepts connections            //
                ////////////////////////////////////
                Socket s = this.socket.accept();
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