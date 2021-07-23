package Chat.console.handlers;

import Chat.console.exception.CloseConsoleException;
import Chat.console.exception.UnexpectedClosedConsole;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;

/**
 * Handle a console command
 * 
 * @author Daniele Castiglia
 * @version 1.1.0
 */
public interface Handler
{
    /**
     * The parser of the command's args
     */
    final CommandLineParser parser = new DefaultParser();

    /**
     * Method that handle the command. The implementation
     * depends on the handler
     * 
     * @param args the arguments passed to the command
     */
    void handle(String[] args) throws CloseConsoleException, UnexpectedClosedConsole, ParseException;
}