package Chat.console;

/**
 * Represent the exception in the console that
 * causes the unexpected close
 * 
 * @author Daniele Castiglia
 * @version 1.0.0
 */
public class UnexpectedClosedConsole extends Exception
{
    public UnexpectedClosedConsole(String er)
    {
        super(er);
    }
}