package Chat.console.handlers;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import Chat.server.Client;
import Chat.server.Server;

/**
 * @author Daniele Castiglia
 * @version 1.1.0
 */
public class ConnectedHandler implements Handler 
{
    public ConnectedHandler()
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
            formatter.printHelp("connected", options);

            return;
        }

        Client[] clients = Server.server.getConnectedClients();

        if (clients.length == 1) 
        {
            System.out.println("There is only one client connected: ");
            System.out.println("> " + clients[0].getUsername() + " (" + clients[0].getAddress() + ")");
        } 
        else if (clients.length == 0) 
        {
            System.out.println("There are 0 clients connected");
        } 
        else 
        {
            System.out.println("There are " + clients.length + " clients connected: ");
            for (Client c : clients) {
                System.out.println("> " + c.getUsername() + " (" + c.getAddress() + ")");
            }
        }
    }
}