package Chat.server.exceptions;

public class FieldNotFound extends Exception
{
    public FieldNotFound(String field)
    {
        super("This message does not contain '" + field + "' field");
    }
}
