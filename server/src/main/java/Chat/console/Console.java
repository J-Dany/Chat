package Chat.console;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Represents the server console
 * 
 * @author Daniele Castiglia
 * @version 1.1.0
 */
public class Console extends Thread
{
    /**
     * The HashMap containing handler
     * for each command available
     */
    HashMap<String, Handler> handlers;

    /**
     * Constructor
     */
    public Console()
    {
        this.setHanderls();
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
            String line = input.next();

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

            }
            ////////////////////////////////////
            // Any other exception            //
            ////////////////////////////////////
            catch (Exception e)
            {

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

    }
}