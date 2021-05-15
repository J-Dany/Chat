package Chat.console;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;

import Chat.LogMessage;
import Chat.Logger;
import Chat.server.Client;
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
        this.setHanderls();
        this.setIgnoredCommands();
        this.server = server;
        this.logger = logger;
    }

    @Override
    public void run()
    {
        ////////////////////////////////////
        // Instatiating the object for    //
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
                // Instatiating the arguments     //
                // list                           //
                ////////////////////////////////////
                ArrayList<String> args = new ArrayList<>();

                ////////////////////////////////////
                // Filling the arraylist with     //
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
    private void setHanderls()
    {
        Handler closeHandler = new Handler()
        {
            @Override
            public void handle(ArrayList<String> args) throws CloseConsoleException, UnexpectedClosedConsole 
            {
                throw new CloseConsoleException();
            }
        };

        this.handlers.put("exit", closeHandler);
        this.handlers.put("close", closeHandler);
        this.handlers.put("stop", closeHandler);

        this.handlers.put("history", new Handler()
        {
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
        });

        this.handlers.put("connected", new Handler()
        {
            @Override
            public void handle(ArrayList<String> args) throws CloseConsoleException, UnexpectedClosedConsole 
            {
                Client[] clients = server.getConnectedClients();

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
                    for (Client c : clients)
                    {
                        System.out.println("> " + c.getUsername() + " (" + c.getAddress() + ")");
                    }
                }
            }
        });
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