package Chat.database;

import java.sql.Connection;
import java.sql.DriverManager;
import Chat.Logger;
import io.github.cdimascio.dotenv.*;

/**
 * @author Daniele Castiglia
 * @version 1.0.0
 */
public class DatabaseConnection 
{
    /**
     * Database URL, loaded from .env file
     */
    public static final String DB_URL = 
        "jdbc:mysql://" + Dotenv.load().get("DB_HOST") + "/" 
        + Dotenv.load().get("DB_NAME") + "?autoReconnect=false&connectTimeout=10000&socketTimeout=10000";

    /**
     * The connection to the database
     */
    public static Connection connection = null;

    /**
     * Get the connection using the
     * design pattern "Singleton"
     * 
     * @return Connection
     */
    public static Connection getConnection()
    {
        if (connection == null)
        {
            try
            {
                DriverManager.setLoginTimeout(2);
                connection = DriverManager.getConnection(DB_URL, Dotenv.load().get("DB_USER"), Dotenv.load().get("DB_PASSWD"));
            }
            catch (Exception e)
            {
                Logger.error(e);
                System.err.println("Can't establish connection to database: " + e.getMessage());
            }
        }

        return connection;
    }
}