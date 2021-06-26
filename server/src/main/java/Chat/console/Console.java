package Chat.console;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;
import Chat.LogMessage;
import Chat.Logger;
import Chat.console.handlers.CloseHandler;
import Chat.console.handlers.ConnectedHandler;
import Chat.console.handlers.GuiHandler;
import Chat.console.handlers.HistoryHandler;
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
    private HashMap<String, Handler> handlers;

    /**
     * A reference to the server
     */
    private Server server;

    /**
     * A reference to the server logger
     */
    private Logger logger;

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
    public Console(Server server, Logger logger)
    {
        this.setName("Console");
        this.ignoredCommand = new ArrayList<>();
        this.history = new Stack<>();
        this.handlers = new HashMap<>();
        this.setHandlers();
        this.setIgnoredCommands();
        this.server = server;
        this.logger = logger;
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
            // Read the written command       //
            ////////////////////////////////////
            System.out.print("? ");
            String line = input.nextLine();

            this.logger.addMsg(LogMessage.info("Command: " + line));

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
                // Take command name              //
                ////////////////////////////////////
                String command = arguments[0];

                ////////////////////////////////////
                // There is a set of commands     //
                // to be ignored when saving the  //
                // command to history             //
                ////////////////////////////////////
                if (!this.ignoredCommand.contains(command))
                {
                    this.history.push(line);
                }

                ////////////////////////////////////
                // Instantiating the arguments     //
                // list                           //
                ////////////////////////////////////
                ArrayList<String> args = new ArrayList<>();

                ////////////////////////////////////
                // Filling the array list with    //
                // the written params             //
                ////////////////////////////////////
                for (int i = 1; i < arguments.length; ++i)
                {
                    args.add(arguments[i]);
                }

                ////////////////////////////////////
                // Calls the command handler      //
                // and pass to it its args        //
                ////////////////////////////////////
                Handler handler = this.handlers.get(command);
                
                if (handler == null)
                {
                    throw new CommandNotFound(command);
                }

                handler.handle(args);
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
                    this.logger.addMsg(LogMessage.error(ex.toString()));
                }

                break;
            }
            ////////////////////////////////////
            // Represents a console crash     //
            ////////////////////////////////////
            catch (UnexpectedClosedConsole e)
            {
                this.logger.addMsg(LogMessage.error(e.toString()));
                break;
            }
            ////////////////////////////////////
            // This exception is thrown when  //
            // a written command does not     //
            // exists                         //
            ////////////////////////////////////
            catch (CommandNotFound e)
            {
                System.err.println(e.getMessage());
                this.logger.addMsg(LogMessage.error(e.toString()));
            }
            ////////////////////////////////////
            // Any other exception            //
            ////////////////////////////////////
            catch (Exception e)
            {
                this.logger.addMsg(LogMessage.error(e.toString()));
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
        Handler closeHandler = new CloseHandler();
        this.handlers.put("exit", closeHandler);
        this.handlers.put("close", closeHandler);
        this.handlers.put("stop", closeHandler);

        ////////////////////////////////////
        // Setting the "history" handler  //
        ////////////////////////////////////
        this.handlers.put("history", new HistoryHandler(this.history));

        ////////////////////////////////////
        // Setting the "gui" handler      //
        ////////////////////////////////////
        this.handlers.put("gui", new GuiHandler());

        ////////////////////////////////////
        // Setting the "connected"        //
        // handler                        //
        ////////////////////////////////////
        this.handlers.put("connected", new ConnectedHandler());
    }

    /**
     * Sets the command to be ignored
     * by console when saving command
     * in history
     */
    private void setIgnoredCommands()
    {
        this.ignoredCommand.add("useradd");
    }
}