package Chat;

import Chat.server.Server;
import Chat.console.Console;
import Chat.database.DatabaseConnection;

/**
 * Here is where the program starts
 * 
 * @author Daniele Castiglia
 * @version 1.3.0
 */
public class Main
{
    public static void main(String[] args)
    {
        ////////////////////////////////////
        // Sets the name of the current   //
        // thread to identify it          //
        ////////////////////////////////////
        Thread.currentThread().setName("That-Main");

        Logger.createLogger();

        try
        {
            Logger.info("Establishing a connection to the database...");

            ////////////////////////////////////
            // Try to establish a connection  //
            // to the database                //
            ////////////////////////////////////
            DatabaseConnection.getConnection();

            Logger.info("Starting the server...");

            ////////////////////////////////////
            // instantiation of server,       //
            // setting the logger for the     //
            // server and starts it           //
            ////////////////////////////////////
            Server server = new Server(3678);
            server.start();

            Logger.ok("Server started");

            Console console = new Console();
            console.start();

            server.join();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        Logger.closeLogger();
    }
}