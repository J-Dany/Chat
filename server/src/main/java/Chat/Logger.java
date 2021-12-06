package Chat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import Chat.server.Server;
import io.github.cdimascio.dotenv.Dotenv;

/**
 * Class for logging messages
 * 
 * @author Daniele Castiglia
 * @version 1.3.0
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

    public static Logger createLogger()
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

    public static void closeLogger()
    {
        logger.interrupt();
        logger.close();
    }

    /**
     * Log a OK message
     * @param msg
     */
    public static void ok(String msg)
    {
        logger.addMsg(LogMessage.ok(msg));
    }

    /**
     * Log a WARNING message
     * @param msg
     */
    public static void warning(String msg)
    {
        logger.addMsg(LogMessage.warning(msg));
    }

    /**
     * Log a ERROR message
     * @param msg
     */
    public static void error(String msg)
    {
        logger.addMsg(LogMessage.error(msg));
    }

    /**
     * Log a ERROR message
     * @param exception
     */
    public static void error(Exception exception)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);

        logger.addMsg(LogMessage.error(sw.toString()));
    }

    /**
     * Log a INFO message
     * @param msg
     */
    public static void info(String msg)
    {
        logger.addMsg(LogMessage.info(msg));
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
    private void addMsg(LogMessage msg)
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