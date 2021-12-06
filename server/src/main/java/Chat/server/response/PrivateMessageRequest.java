package Chat.server.response;

import Chat.Logger;
import Chat.server.Client;
import Chat.database.DatabaseQueries;
import Chat.server.message.info.PrivateMessageBuilder;
import Chat.server.message.request.PrivateMessageRequestMessage;
import Chat.server.pojo.Friend;

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
        Friend friend = client.getFriend(req.getAddresse());

        Logger.info("Sending message => " + client.getUsername() + " -> " + req.getAddresse());

        client.sendToFriend(
            req.getAddresse(),
            new PrivateMessageBuilder()
                .setSender(client.getUsername())
                .setData(req.getData())
                .setMessage(req.getMessage())
                .setHM(req.getHM())
                .setContent(req.getContent())
                .setLanguage(req.getLanguage())
                .build()
        );

        Logger.ok("Message sent");

        DatabaseQueries.insertNewMessage(req.getMessage(), req.getData(), client.getId(), friend.getId(), req.getContent(), req.getLanguage());
    }
}