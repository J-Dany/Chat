package Chat.server.response;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import Chat.LogMessage;
import Chat.Logger;
import Chat.server.Client;
import Chat.server.Message;
import Chat.server.Server;
import Chat.server.database.DatabaseConnection;

/**
 * @author Daniele Castiglia
 * @version 1.0.2
 */
public class PrivateMessageRequest implements Request
{
    @Override
    public RequestReturnValues handle(Message msg, Client client, Logger logger) throws Exception 
    {
        Connection connection = DatabaseConnection.getConnection();

        PreparedStatement statement = connection.prepareStatement("INSERT INTO messages(data, message, id_group, sender, addresse)"
        + " VALUES (?, ?, ?, ?, ?);");

        int addresseId = 0;
        if (Server.server.getClient(msg.getAddresse()) == null)
        {
            String query = "SELECT id_user FROM users WHERE username = '" + msg.getAddresse() + "';";

            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery(query);

            if (result.next())
            {
                addresseId = result.getInt("id_user");
            }
        }
        else
        {
            addresseId = Server.server.getClient(msg.getAddresse()).getId();
        }

        statement.setString(1, msg.getData());
        statement.setString(2, msg.getMessage());
        statement.setNull(3, Types.NULL);
        statement.setInt(4, client.getId());
        statement.setInt(5, addresseId);

        statement.executeUpdate();

        logger.addMsg(LogMessage.info("Sending message => " + client.getUsername() + " -> " + msg.getAddresse()));

        client.sendToFriend(msg.getAddresse(), Message.privateMessage(client.getUsername(), msg.getMessage(), msg.getHM()));

        return RequestReturnValues.PRIVATE_MESSAGE;
    }
}