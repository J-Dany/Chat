package Chat.server.callback;

/**
 * @author Daniele Castiglia
 * @version 1.0.0
 */
@FunctionalInterface
public interface OnSocketWritingCallback 
{
    void doJob(String senderUsername, String message);
}
