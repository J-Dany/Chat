package Chat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import Chat.server.Server;
import io.github.cdimascio.dotenv.Dotenv;

/**
 * Class for logging messages
 * 
 * @author Daniele Castiglia
 * @version 1.2.0
 */
public class Logger extends Thread
{
    private static Logger logger;

    /**
     * Represents the queue where the LogMessages
     * are stored
     */
    private BlockingQueue<LogMessage> queue;

    /**
     * Represents the resource of the
     * log file
     */
    private File logFile;

    /**
     * Object used to write
     * on the {@code logFile}
     */
    private PrintWriter writer;

    /**
     * Type of log message.
     * 
     * At this time, it can be:
     * - OK => an operation returns successfully 
     * - ER => stands for "error", represents a message error
     * - IN => stands for "info", represents a info message
     * - WA => stands for "warning", represents a warning message
     */
    public enum LoggerType
    {
        OK,
        ER, // ERROR
        IN, // INFO
        WA  // WARNING
    };

    private Logger(String path) throws FileNotFoundException, UnsupportedEncodingException, IOException
    {
        super("Logger");
        this.queue = new LinkedBlockingQueue<>();
        this.logFile = new File(path);
        if (!this.logFile.exists())
        {
            this.logFile.createNewFile();
        }

        FileWriter w = new FileWriter(this.logFile, true);
        this.writer = new PrintWriter(w);

        this.start();
    }

    public static Logger getLogger()
    {
        if (logger == null)
        {
            try
            {
                logger = new Logger(Dotenv.load().get("LOG_FILE_PATH"));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return logger;
    }

    @Override
    public void run()
    {
        while(!Thread.interrupted())
        {
            try
            {
                this.insertIntoFile(this.queue.take());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Add msg to the queue
     * 
     * @param msg the object LogMessage
     * @see LogMessage
     */
    public void addMsg(LogMessage msg)
    {
        try
        {
            synchronized (this.queue)
            {
                this.queue.add(msg);
            }

            if (Server.server != null)
            {
                Server.server.upgradeLogs(msg);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Close the resource allocated for this
     * object
     */
    public void close()
    {
        if (!this.queue.isEmpty())
        {
            while (!this.queue.isEmpty())
            {
                this.insertIntoFile(this.queue.poll());
            }
        }
    }

    /**
     * Write into the file the passed message
     * 
     * @param msg the object LogMessage
     */
    private void insertIntoFile(LogMessage msg)
    {
        try
        {
            this.writer.println(msg);
            this.writer.flush();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}