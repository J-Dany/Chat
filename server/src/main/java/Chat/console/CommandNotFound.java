package Chat.console;

public class CommandNotFound extends Exception
{
    public CommandNotFound(String command)
    {
        super(command + " not found!");
    }
}