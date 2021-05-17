package Chat.server.exceptions;

/**
 * @author Daniele Castiglia
 * @version 1.0.0
 */
public class FieldNotFound extends Exception
{
    public FieldNotFound(String field)
    {
        super("This message does not contain '" + field + "' field");
    }
}
