package Chat.console.exception;

/**
 * Represents the exception "command not found"
 * 
 * @author Daniele Castiglia
 * @version 1.0.0
 */
public class CommandNotFound extends Exception
{
    public CommandNotFound(String command)
    {
        super(command + " not found!");
    }
}