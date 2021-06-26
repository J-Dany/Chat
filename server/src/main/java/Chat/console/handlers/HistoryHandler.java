package Chat.console.handlers;

import java.util.ArrayList;
import java.util.Stack;
import Chat.console.CloseConsoleException;
import Chat.console.Handler;
import Chat.console.UnexpectedClosedConsole;

/**
 * @author Daniele Castiglia
 * @version 1.0.0
 */
public class HistoryHandler implements Handler
{
    /**
     * Reference to the console history
     */
    private Stack<String> history;

    public HistoryHandler(Stack<String> history)
    {
        this.history = history;
    }

    @Override
    public void handle(ArrayList<String> args) throws CloseConsoleException, UnexpectedClosedConsole 
    {
        if (args.isEmpty())
        {
            for (String command : history)
            {
                System.out.println(command);
            }
        }
        else
        {
            int to = Integer.parseInt(args.get(0));

            for (int i = 0; i < to; ++i)
            {
                System.out.println(history.get(i));
            }
        }
    }
}