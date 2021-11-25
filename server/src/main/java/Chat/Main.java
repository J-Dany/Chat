package Chat;

import java.sql.SQLException;
import Chat.server.Server;
import Chat.server.database.DatabaseConnection;

/**
 * Here is where the program starts
 * 
 * @author Daniele Castiglia
 * @version 1.2.0
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

        Logger logger = null;

        try
        {
            ////////////////////////////////////
            // Create an instance of Logger   //
            // and starts the thread          //
            ////////////////////////////////////
            logger = Logger.getLogger();

            logger.addMsg(LogMessage.info("Establishing a connection to the database..."));

            ////////////////////////////////////
            // Try to establish a connection  //
            // to the database                //
            ////////////////////////////////////
            try
            {
                DatabaseConnection.getConnection();

                logger.addMsg(LogMessage.ok("Connection to the database established"));
            }
            catch (SQLException e)
            {
                System.out.println("Can't establish a connection to the database!");
            }

            logger.addMsg(LogMessage.info("Starting the server..."));

            ////////////////////////////////////
            // instantiation of server,       //
            // setting the logger for the     //
            // server and starts it           //
            ////////////////////////////////////
            Server server = new Server(3678);
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