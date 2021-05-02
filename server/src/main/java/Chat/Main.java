package Chat;

import Chat.server.Server;
import io.github.cdimascio.dotenv.*;

/**
 * Here is where the program starts
 * 
 * @author Daniele Castiglia
 * @version 1.0.0
 */
public class Main
{
    public static void main(String[] args) 
    {
        ////////////////////////////////////
        // Sets the name of the current   //
        // thread to identify it          //
        ////////////////////////////////////
        Thread.currentThread().setName("Main");
        
        ////////////////////////////////////
        // Load .env file                 //
        ////////////////////////////////////
        Dotenv dotenv = Dotenv.load();

        Logger logger = null;

        try
        {
            System.out.println("Starting the logger...");
            ////////////////////////////////////
            // Create an instance of Logger   //
            // and starts the thread          //
            ////////////////////////////////////
            logger = new Logger(dotenv.get("LOG_FILE_PATH"));
            logger.start();

            System.out.println("Starting the server...");
            logger.addMsg(LogMessage.info("Starting the server..."));
            ////////////////////////////////////
            // instantiation of server,       //
            // setting the logger for the     //
            // server and starts it           //
            ////////////////////////////////////
            Server server = new Server(60000);
            server.setLogger(logger);
            server.start();

            logger.addMsg(LogMessage.ok("Server started"));

            ////////////////////////////////////
            // Wait for the server finishing  //
            // his work                       //
            ////////////////////////////////////
            server.join();

            logger.addMsg(LogMessage.ok("Server closed"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        if (logger != null)
        {
            try
            {
                logger.interrupt();
                logger.join();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}