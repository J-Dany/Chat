package Chat.console;

import java.util.ArrayList;

/**
 * Handle a console command
 * 
 * @author Daniele Castiglia
 * @version 1.0.0
 */
public interface Handler 
{
    /**
     * Actually handle the command
     * 
     * @param args the arguments passed to the command
     */
    void handle(ArrayList<String> args) throws CloseConsoleException, UnexpectedClosedConsole;
}