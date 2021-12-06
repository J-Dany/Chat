package Chat.server.response;

import Chat.Logger;
import Chat.database.DatabaseQueries;
import Chat.server.Client;
import Chat.server.Server;
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
        if (!DatabaseQueries.userLogin(client, req.getUsername(), req.getPassword()))
        {
            Logger.info("Login failed for " + client.getAddress());

            //////////////////////////
            // Send a failed login  //
            // message              //
            //////////////////////////
            client.sendMessage(new LoginMessage(false, -1));

            return;
        }

        Thread.currentThread().setName(client.getUsername());

        client.sendMessage(new LoginMessage(true, client.getId()));
        client.setFriends(DatabaseQueries.getUserFriends(client));
        client.sendListOfFriend();
        client.notifyOnlineToFriend();

        Server.server.addNewConnectedClient(client);

        Logger.info("Login ok for " + client.getAddress() + " (" + client.getUsername() + ")");
    }
}