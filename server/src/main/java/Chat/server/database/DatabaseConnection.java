package Chat.server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
    public static final String DB_URL = Dotenv.load().get("DB_URL");

    /**
     * The connection to the database
     */
    public static Connection connection = null;

    /**
     * Get the connection using the
     * design pattern "Singleton"
     * 
     * @return Connection
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException
    {
        if (connection != null)
        {
            connection = DriverManager.getConnection(DB_URL);
        }

        return connection;
    }
}