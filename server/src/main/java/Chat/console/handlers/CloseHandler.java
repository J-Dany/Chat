package Chat.console.handlers;

import java.util.ArrayList;

import Chat.console.CloseConsoleException;
import Chat.console.Handler;
import Chat.console.UnexpectedClosedConsole;

/**
 * @author Daniele Castiglia
 * @version 1.0.0
 */
public class CloseHandler implements Handler
{
    @Override
    public void handle(ArrayList<String> args) throws CloseConsoleException, UnexpectedClosedConsole 
    {
        throw new CloseConsoleException();
    }
}