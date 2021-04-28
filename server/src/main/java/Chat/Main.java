package Chat;

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
        Thread.currentThread().setName("Main");
        
        Dotenv dotenv = Dotenv.load();

        Logger logger = null;

        try
        {
            logger = new Logger(dotenv.get("LOG_FILE_PATH"));
            logger.start();
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