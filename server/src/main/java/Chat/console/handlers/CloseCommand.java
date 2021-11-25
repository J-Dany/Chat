package Chat.console.handlers;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import Chat.console.exception.CloseConsoleException;

/**
 * @author Daniele Castiglia
 * @version 1.1.0
 */
public class CloseCommand implements Command
{
    /**
     * Options of the command
     */
    final Options options = new Options();

    public CloseCommand()
    {
        options
            .addOption(Option.builder("h")
                .longOpt("help")
                .desc("Print this message")
                .required(false)
                .hasArg(false)
                .build()
            );
    }

    @Override
    public void handle(String[] args) throws CloseConsoleException, ParseException
    {
        CommandLine cmd = parser.parse(options, args);

        if (cmd.hasOption("h"))
        {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("exit", options);

            return;
        }

        throw new CloseConsoleException();
    }
}