package Chat.server.response;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Base64;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import Chat.LogMessage;
import Chat.server.Client;
import Chat.server.Server;
import Chat.server.database.DatabaseConnection;
import Chat.server.message.info.LoginMessage;
import Chat.server.message.request.LoginRequestMessage;

/**
 * @author Daniele Castiglia
 * @version 1.0.1
 */
public class LoginRequest implements Request
{
    LoginRequestMessage req;

    public LoginRequest(LoginRequestMessage req)
    {
        this.req = req;
    }

    @Override
    public void handle(Client client) throws Exception 
    {        
        Connection connection = DatabaseConnection.getConnection();

        Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

        String query = "SELECT * FROM users WHERE username = '" + req.getUsername() + "';";

        ResultSet result = stmt.executeQuery(query);

        logger.addMsg(LogMessage.ok("Database interrogated for " + client.getAddress()));

        int num_rows = 0;

        if (result.last())
        {
            num_rows = result.getRow();
            result.beforeFirst();
        }

        String password = Base64.getEncoder().encodeToString(
            Hashing.sha256().hashString(req.getPassword(), Charsets.UTF_8).asBytes()
        );

        if (result.next() && num_rows == 1
            &&
            result.getString("username").equals(req.getUsername()) && result.getString("password").equals(password)
        )
        {
            client.setId(result.getInt("id_user"));
            client.setUsername(req.getUsername());
            client.sendMessage(new LoginMessage(true, result.getInt("id_user")));

            Server.server.addNewConnectedClient(client.getUsername(), client);
            client.sendListOfFriend();
            client.notifyOnlineToFriend();
            logger.addMsg(LogMessage.info("Login ok for " + client.getAddress() + " (" + client.getUsername() + ")"));

            return;
        }
        
        logger.addMsg(LogMessage.info("Login failed for " + client.getAddress()));

        client.sendMessage(new LoginMessage(false, -1));
    }
}