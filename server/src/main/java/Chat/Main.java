package Chat;

import Chat.server.Server;
import io.github.cdimascio.dotenv.*;

/**
 * Here is where the program starts
 * 
 * @author Daniele Castiglia
 * @version 1.0.0
 */
public class Main
{
    public static void main(String[] args) 
    {
        ////////////////////////////////////
        // Setto il nome al thread        //
        // corrente per identificarlo     //
        ////////////////////////////////////
        Thread.currentThread().setName("Main");
        
        ////////////////////////////////////
        // Carico il file .env con le     //
        // variabili d'ambiente           //
        ////////////////////////////////////
        Dotenv dotenv = Dotenv.load();

        Logger logger = null;

        try
        {
            ////////////////////////////////////
            // Istanzio il logger e lo faccio //
            // partire                        //
            ////////////////////////////////////
            logger = new Logger(dotenv.get("LOG_FILE_PATH"));
            logger.start();

            ////////////////////////////////////
            // Istanzio il server, lo faccio  //
            // partire e gli passo il logger  //
            ////////////////////////////////////
            Server server = new Server(60000);
            server.setLogger(logger);
            server.start();

            ////////////////////////////////////
            // Aspetto che il server termini  //
            // la sua esecuzione              //
            ////////////////////////////////////
            server.join();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        if (logger != null)
        {
            try
            {
                logger.interrupt();
                logger.join();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}