package Chat.server.response;

import java.sql.ResultSet;
import Chat.LogMessage;
import Chat.Logger;
import Chat.server.Client;
import Chat.server.Message;
import Chat.server.database.DatabaseTable;

/**
 * @author Daniele Castiglia
 * @version 1.0.1
 */
public class LoginRequest implements Request
{
    @Override
    public RequestReturnValues handle(Message msg, Client client, Logger logger) throws Exception 
    {
        DatabaseTable userTable = new DatabaseTable("users", "username");
        
        ResultSet result = userTable.findOneBy(msg.getSender());

        logger.addMsg(LogMessage.ok("Database interrogated for " + client.getAddress()));

        int num_rows = 0;

        if (result.last())
        {
            num_rows = result.getRow();
            result.beforeFirst();
        }

        if (result.next() && num_rows == 1
            &&
            result.getString("username").equals(msg.getSender()) && result.getString("password").equals(msg.getPassword())
        )
        {
            client.setUsername(msg.getSender());
            client.sendMessage(Message.login(true));
            return RequestReturnValues.LOGIN_OK;
        }
        
        client.sendMessage(Message.login(false));
        return RequestReturnValues.LOGIN_FAILED;
    }
    
}