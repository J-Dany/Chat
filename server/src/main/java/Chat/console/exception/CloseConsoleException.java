package Chat.console.exception;

/**
 * Represent the exception of closed
 * console.
 * 
 * THIS IS NOT AN ERROR
 * 
 * @author Daniele Castiglia
 * @version 1.0.0
 */
public class CloseConsoleException extends Exception
{
    public CloseConsoleException()
    {
        super("Console closed");
    }
}