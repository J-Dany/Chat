package Chat.server.response;

import java.sql.ResultSet;
import Chat.server.Client;
import Chat.server.Message;
import Chat.server.database.DatabaseTable;

public class LoginRequest implements Request
{
    @Override
    public RequestReturnValues handle(Message msg, Client client) throws Exception 
    {
        DatabaseTable userTable = new DatabaseTable("users", "username");
        
        ResultSet result = userTable.findOneBy(msg.getSender());

        int num_rows = 0;

        if (result.last())
        {
            num_rows = result.getRow();
            result.beforeFirst();
        }

        if (num_rows == 1
            &&
            result.getString("username") == msg.getSender() && result.getString("password") == msg.getPassword()
        )
        {

            return RequestReturnValues.LOGIN_FAILED;
        }
        
        return RequestReturnValues.LOGIN_FAILED;
    }
    
}