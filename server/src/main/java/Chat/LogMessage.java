package Chat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import Chat.Logger.LoggerType;

/**
 * Class that represent a log message
 * 
 * @author Daniele Castiglia
 * @version 1.1.0
 */
public class LogMessage 
{
    /**
     * Log type
     * @see Logger.LoggerType
     */
    private LoggerType type;

    /**
     * The messagge
     */
    private String msg;
    
    /**
     * The name of the thread that
     * instantiated this object
     */
    private String threadName;

    /**
     * Constructor
     * 
     * @param type log type
     * @param msg the messagge
    */
    private LogMessage(LoggerType type, String msg)
    {
        this.type = type;
        this.msg = msg;
        this.threadName = Thread.currentThread().getName();
    }

    /**
     * Returns an info LogMessage instance, representing
     * a message that will be written into the log
     * 
     * @param msg the message to write
     * @return an instance of LogMessage
     */
    public static LogMessage info(String msg)
    {
        return new LogMessage(LoggerType.IN, msg);
    }

    /**
     * Returns an error LogMessage instance, representing
     * a message that will be written into the log
     * 
     * @param msg the message to write
     * @return an instance of LogMessage
     */
    public static LogMessage error(String msg)
    {
        return new LogMessage(LoggerType.ER, msg);
    }

    /**
     * Returns an ok LogMessage instance, representing
     * a message that will be written into the log
     * 
     * @param msg the message to write
     * @return an instance of LogMessage
     */
    public static LogMessage ok(String msg)
    {
        return new LogMessage(LoggerType.OK, msg);
    }

    @Override
    public String toString()
    {
        return 
        "[ " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("u-L-d H:m:s")) + " ]" + 
        " [ " + this.type + " ] - " + 
        this.threadName + " - " + msg;
    }
}