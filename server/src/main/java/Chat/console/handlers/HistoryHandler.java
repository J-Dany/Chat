package Chat.console.handlers;

import java.util.Stack;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;

/**
 * @author Daniele Castiglia
 * @version 1.1.0
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

        options
            .addOption(
                Option.builder("n")
                    .longOpt("index")
                    .desc("Returns the command at specified index")
                    .required(false)
                    .hasArg(true)
                    .type(Integer.class)
                    .build()
            )
            .addOption(
                Option.builder("h")
                    .longOpt("help")
                    .desc("Print this message")
                    .required(false)
                    .hasArg(false)
                    .build()
            );
    }

    @Override
    public void handle(String[] args) throws ParseException
    {
        if (args == null)
        {
            return;
        }

        CommandLine cmd = parser.parse(options, args);

        if (cmd.hasOption("h"))
        {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("history", options);
        }
        else if (cmd.hasOption("n"))
        {
            int index = Integer.parseInt(cmd.getOptionValue("n"));

            System.out.println("> history[" + index + "]: " + history.get(index));
        }
        else
        {
            for (String command : history)
            {
                System.out.println("> " + command);
            }
        }
    }
}