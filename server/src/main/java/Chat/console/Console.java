package Chat.console;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;
import Chat.Logger;
import Chat.console.exception.CloseConsoleException;
import Chat.console.exception.CommandNotFound;
import Chat.console.exception.UnexpectedClosedConsole;
import Chat.console.handlers.CloseCommand;
import Chat.console.handlers.ConnectedCommand;
import Chat.console.handlers.LogCommand;
import Chat.console.handlers.GuiCommand;
import Chat.console.handlers.Command;
import Chat.console.handlers.HistoryCommand;
import Chat.server.Server;

/**
 * Represents the server console
 * 
 * @author Daniele Castiglia
 * @version 1.3.2
 */
public class Console extends Thread
{
    /**
     * The HashMap containing handler
     * for each command available
     */
    private HashMap<String, Command> commands;

    /**
     * A reference to the server
     */
    private Server server;

    /**
     * Console history
     */
    private Stack<String> history;

    /**
     * ArrayList containing commands to be ignored
     * when saving command in the history
     */
    private ArrayList<String> ignoredCommand;

    /**
     * Constructor
     */
    public Console()
    {
        this.setName("Console");
        this.ignoredCommand = new ArrayList<>();
        this.history = new Stack<>();
        this.commands = new HashMap<>();
        this.setHandlers();
        this.setIgnoredCommands();
        this.server = Server.server;
    }

    @Override
    public void run()
    {
        ////////////////////////////////////
        // Instantiating the object for   //
        // reading from standard input    //
        ////////////////////////////////////
        Scanner input = new Scanner(System.in);

        while (true)
        {
            ////////////////////////////////////
            // Read the command               //
            ////////////////////////////////////
            System.out.print("? ");
            String line = input.nextLine();

            try
            {
                ////////////////////////////////////
                // Splits the string into an      //
                // array. From this array I can   //
                // obtain command name and its    //
                // arguments                      //
                ////////////////////////////////////
                String arguments[] = line.split(" ");

                ////////////////////////////////////
                // Get command name               //
                ////////////////////////////////////
                String com = arguments[0];

                ////////////////////////////////////
                // There is a set of commands     //
                // to be ignored when saving the  //
                // command to history             //
                ////////////////////////////////////
                if (!this.ignoredCommand.contains(com))
                {
                    this.history.push(line);
                }

                ////////////////////////////////////
                // Calls the command handler      //
                // and pass to it its args        //
                ////////////////////////////////////
                Command command = this.commands.get(com);

                if (command == null)
                {
                    throw new CommandNotFound(com);
                }

                int to = arguments.length == 1
                    ? 1
                    : arguments.length;

                command.handle(Arrays.copyOfRange(arguments, 1, to));
            }
            ////////////////////////////////////
            // This exception means "Ok, I    //
            // want to close the console, I   //
            // am closing the server"         //
            ////////////////////////////////////
            catch (CloseConsoleException e)
            {
                try
                {
                    this.server.close();
                }
                catch (Exception ex)
                {
                    Logger.error(ex);
                }

                break;
            }
            ////////////////////////////////////
            // Represents a console crash     //
            ////////////////////////////////////
            catch (UnexpectedClosedConsole e)
            {
                Logger.error(e);
                break;
            }
            ////////////////////////////////////
            // This exception is thrown when  //
            // a written command does not     //
            // exists                         //
            ////////////////////////////////////
            catch (CommandNotFound e)
            {
                System.err.println("> " + e.getMessage());
                Logger.error(e);
            }
            ////////////////////////////////////
            // Any other exception            //
            ////////////////////////////////////
            catch (Exception e)
            {
                System.out.println("> Error: " + e.getMessage());
                Logger.error(e);
            }
        }

        input.close();
    }

    /**
     * Set up every commands
     * available
     */
    private void setHandlers()
    {
        ////////////////////////////////////
        // Setting the "close" handler    //
        ////////////////////////////////////
        Command closeHandler = new CloseCommand();
        this.commands.put("exit", closeHandler);
        this.commands.put("close", closeHandler);
        this.commands.put("stop", closeHandler);

        ////////////////////////////////////
        // Setting the "history" handler  //
        ////////////////////////////////////
        this.commands.put("history", new HistoryCommand(this.history));

        ////////////////////////////////////
        // Setting the "gui" handler      //
        ////////////////////////////////////
        this.commands.put("gui", new GuiCommand());

        ////////////////////////////////////
        // Setting the "connected"        //
        // handler                        //
        ////////////////////////////////////
        this.commands.put("connected", new ConnectedCommand());

        ////////////////////////////////////
        // Setting the "log"              //
        // handler                        //
        ////////////////////////////////////
        this.commands.put("log", new LogCommand());
    }

    /**
     * Sets the command to be ignored
     * by console when saving command
     * in history
     */
    private void setIgnoredCommands() { }
}