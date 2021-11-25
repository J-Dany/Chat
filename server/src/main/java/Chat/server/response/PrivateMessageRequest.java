package Chat.server.response;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import Chat.Logger;
import Chat.server.Client;
import Chat.server.Server;
import Chat.server.database.DatabaseConnection;
import Chat.server.message.info.PrivateMessageBuilder;
import Chat.server.message.request.PrivateMessageRequestMessage;

/**
 * @author Daniele Castiglia
 * @version 1.0.2
 */
public class PrivateMessageRequest implements Request
{
    private PrivateMessageRequestMessage req;

    public PrivateMessageRequest(PrivateMessageRequestMessage req)
    {
        this.req = req;
    }

    @Override
    public void handle(Client client) throws Exception 
    {
        Connection connection = DatabaseConnection.getConnection();

        PreparedStatement statement = connection.prepareStatement("INSERT INTO messages(data, unique_id, message, id_group, sender, addresse, content_type, language)"
        + " VALUES (?, ?, ?, ?, ?, ?, ?, ?);");

        int addresseId = 0;
        if (Server.server.getClient(req.getAddresse()) == null)
        {
            String query = "SELECT id_user FROM users WHERE username = '" + req.getAddresse() + "';";

            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery(query);

            if (result.next())
            {
                addresseId = result.getInt("id_user");
            }
        }
        else
        {
            addresseId = Server.server.getClient(req.getAddresse()).getId();
        }

        String uniqueId = req.getUniqueId();

        statement.setString(1, req.getData());
        statement.setString(2, uniqueId);
        statement.setString(3, req.getMessage());
        statement.setNull(4, Types.NULL);
        statement.setInt(5, client.getId());
        statement.setInt(6, addresseId);
        statement.setString(7, req.getContent());
        statement.setString(8, req.getLanguage());

        statement.executeUpdate();

        Logger.info("Sending message => " + client.getUsername() + " -> " + req.getAddresse());

        client.sendToFriend(
            req.getAddresse(),
            new PrivateMessageBuilder()
                .setSender(client.getUsername())
                .setUniqueId(uniqueId)
                .setData(req.getData())
                .setMessage(req.getMessage())
                .setHM(req.getHM())
                .setContent(req.getContent())
                .setLanguage(req.getLanguage())
                .build()
        );

        Logger.ok("Message sent");
    }
}