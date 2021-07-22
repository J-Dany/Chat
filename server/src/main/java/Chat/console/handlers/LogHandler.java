package Chat.console.handlers;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import Chat.server.Server;

/**
 * @author Daniele Castiglia
 * @version 1.1.0
 */
public class LogHandler implements Handler
{
    public LogHandler()
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
    public void handle(String[] args) throws ParseException
    {
        CommandLine cmd = parser.parse(options, args);

        if (cmd.hasOption("h"))
        {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("log", options);
            return;
        }

        if (args == null)
        {
            System.out.println("> Logs: " + Server.server.logsSize());
            System.out.println("> Last log: " + Server.server.getLastLog());
        }
        else
        {

        }
    }
}