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

        Logger.createLogger();

        try
        {
            Logger.info("Establishing a connection to the database...");

            ////////////////////////////////////
            // Try to establish a connection  //
            // to the database                //
            ////////////////////////////////////
            try
            {
                DatabaseConnection.getConnection();

                Logger.ok("Connection to the database established");
            }
            catch (SQLException e)
            {
                System.out.println("Can't establish a connection to the database!");
            }

            Logger.info("Starting the server...");

            ////////////////////////////////////
            // instantiation of server,       //
            // setting the logger for the     //
            // server and starts it           //
            ////////////////////////////////////
            Server server = new Server(3678);
            server.start();

            Logger.ok("Server started");

            ////////////////////////////////////
            // Wait for the server finishing  //
            // his work                       //
            ////////////////////////////////////
            server.join();

            Logger.ok("Server closed");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        Logger.closeLogger();
    }
}