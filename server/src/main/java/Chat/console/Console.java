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
        // Inizializzo l'oggetto per      //
        // leggere da tastiera            //
        ////////////////////////////////////
        Scanner input = new Scanner(System.in);

        while (true)
        {
            ////////////////////////////////////
            // Leggo il comando dalla console //
            ////////////////////////////////////
            String line = input.next();

            try
            {
                ////////////////////////////////////
                // Splitto tutti gli argomenti    //
                // e li metto dentro l'array      //
                ////////////////////////////////////
                String arguments[] = line.split(" ");

                ////////////////////////////////////
                // Dagli argomenti prendo il      //
                // comando richiesto              //
                ////////////////////////////////////
                String command = arguments[0];

                ////////////////////////////////////
                // Inizializzo l'arraylist degli  //
                // argomenti passati al comando   //
                ////////////////////////////////////
                ArrayList<String> args = new ArrayList<>();

                ////////////////////////////////////
                // Riempio l'arraylist con i      //
                // parametri                      //
                ////////////////////////////////////
                for (int i = 1; i < arguments.length; ++i)
                {
                    args.add(arguments[i]);
                }

                ////////////////////////////////////
                // Chiama il comando richiesto e  //
                // gli passa i parametri          //
                ////////////////////////////////////
                Handler handler = this.handlers.get(command);
                
                if (handler == null)
                {
                    throw new CommandNotFound(command);
                }

                handler.handle(args);
            }
            ////////////////////////////////////
            // Questa eccezione indica che    //
            // un comando vuole chiudere      //
            // normalmente, senza errori, la  //
            // console                        //
            ////////////////////////////////////
            catch (CloseConsoleException e)
            {

                break;
            }
            ////////////////////////////////////
            // Questa invece rappresenta      //
            // l'eccezione nella quale un     //
            // comando dato comporta la       //
            // chiusura della console         //
            ////////////////////////////////////
            catch (UnexpectedClosedConsole e)
            {
                
                break;
            }
            ////////////////////////////////////
            // Questa eccezione rappresenta   //
            // il caso in cui il comando      //
            // digitato non esiste            //
            ////////////////////////////////////
            catch (CommandNotFound e)
            {

            }
            ////////////////////////////////////
            // Rappresenta una qualsiasi      //
            // altra eccezione                //
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