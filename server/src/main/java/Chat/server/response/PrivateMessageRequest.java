package Chat.server.response;

import java.sql.Connection;
import java.sql.Statement;

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
        if (!Server.server.isOnline(msg.getAddresse()))
        {
            return RequestReturnValues.CANT_SEND_PRIVATE_MESSAGE_BECAUSE_OFFLINE;
        }
        
        Connection connection = DatabaseConnection.getConnection();

        Statement stmt = connection.createStatement();
        
        String query = "INSERT INTO messages(data, message, id_group, sender, addresse) values" +
        " ('" + msg.getData() + "', '" + msg.getMessage() + "', NULL, " + client.getId() + ", " + Server.server.getClient(msg.getAddresse()).getId() + ");";

        stmt.executeUpdate(query);

        logger.addMsg(LogMessage.info("Sending message => " + client.getUsername() + " -> " + msg.getAddresse()));

        client.sendToFriend(msg.getAddresse(), Message.privateMessage(client.getUsername(), msg.getMessage()));

        return RequestReturnValues.PRIVATE_MESSAGE;
    }
}