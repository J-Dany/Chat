package Chat.server.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Daniele Castiglia
 * @version 1.0.0
 */
public class DatabaseTable
{
    /**
     * Connection to the database 
     */
    Connection connection;
    
    /**
     * Represents the table name
     */
    private String tableName;

    /**
     * Represents the primary key field
     */
    private String pk;

    /**
     * Default constructor
     * 
     * @param tableName the name of the table that will handle
     * @param pk the field name of the primary key
     * @throws SQLException
     */
    public DatabaseTable(String tableName, String pk) throws SQLException
    {
        this.tableName = tableName;
        this.pk = pk;
        this.connection = DatabaseConnection.getConnection();
    }

    /**
     * Returns all the values on the table
     * 
     * @return ResultSet
     * @throws SQLException
     */
    public ResultSet findAll() throws SQLException
    {
        Statement stmt = connection.createStatement();

        return stmt.executeQuery("SELECT * FROM `" + this.tableName + "`");
    }

    /**
     * Find one value, which can be retrieved with
     * the primary key
     * 
     * @param pk the value of the pk
     * @return ResultSet
     * @throws SQLException
     */
    public ResultSet findOneBy(Object pk) throws SQLException
    {
        Statement stmt = this.connection.createStatement();

        String query = "";

        if (pk instanceof Integer)
        {
            query = "SELECT * FROM `" + this.tableName + "`" +
                "WHERE `" + this.pk + "` = " + pk + ";"; 
        }
        else if (pk instanceof String)
        {
            query = "SELECT * FROM `" + this.tableName + "`" +
                "WHERE `" + this.pk + "` = '" + pk + "';";
        }

        return stmt.executeQuery(query);
    }
}