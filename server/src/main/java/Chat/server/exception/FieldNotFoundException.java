package Chat.server.exception;

/**
 * @author Daniele Castiglia
 * @version 1.0.0
 */
public class FieldNotFoundException extends Exception
{
    public FieldNotFoundException(String field)
    {
        super("This message does not contain '" + field + "' field");
    }
}
