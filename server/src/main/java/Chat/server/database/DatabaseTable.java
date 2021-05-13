package Chat.server.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Daniele Castiglia
 * @version 1.2.0
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
        else if (pk instanceof LocalDate)
        {
            String date = ((LocalDate)pk).format(DateTimeFormatter.ofPattern("uuuu-LL-dd"));

            query = "SELECT * FROM `" + this.tableName + "` " +
                "WHERE `" + this.pk + "` = '" + date + "';";
        }
        else if (pk instanceof LocalTime)
        {
            String time = ((LocalTime)pk).format(DateTimeFormatter.ofPattern("uuuu-LL-dd"));

            query = "SELECT * FROM `" + this.tableName + "` " +
                "WHERE `" + this.pk + "` = '" + time + "';";
        }

        return stmt.executeQuery(query);
    }

    /**
     * Returns the number of rows in this table. If
     * the value can't be retreived from the database,
     * this metod will return -1
     * 
     * @return int
     */
    public int rows() throws SQLException
    {
        String query = "SELECT COUNT(*) as num_rows FROM `" + this.tableName + "`;";

        Statement stmt = this.connection.createStatement();

        ResultSet result = stmt.executeQuery(query);

        if (result.next())
        {
            return result.getInt("num_rows");
        }

        return -1;
    }
}