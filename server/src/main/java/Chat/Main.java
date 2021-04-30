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
            ////////////////////////////////////
            // Create an instance of Logger   //
            // and starts the thread          //
            ////////////////////////////////////
            logger = new Logger(dotenv.get("LOG_FILE_PATH"));
            logger.start();

            ////////////////////////////////////
            // instantiation of server,       //
            // setting the logger for the     //
            // server and starts it           //
            ////////////////////////////////////
            Server server = new Server(60000);
            server.setLogger(logger);
            server.start();

            ////////////////////////////////////
            // Wait for the server finishing  //
            // his work                       //
            ////////////////////////////////////
            server.join();
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