package Chat.server.message;

import java.io.Serializable;

import Chat.server.ClientMessage.TypeOfMessage;

/**
 * @author Daniele Castiglia
 * @version 1.0.0
 */
public abstract class Message implements Serializable 
{ 
    protected TypeOfMessage Type;
}