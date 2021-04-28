package Chat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import Chat.Logger.LoggerType;

/**
 * Class that represent a log message
 * 
 * @author Daniele Castiglia
 * @version 0.0.0
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
    public LogMessage(LoggerType type, String msg)
    {
        this.type = type;
        this.msg = msg;
        this.threadName = Thread.currentThread().getName();
    }

    @Override
    public String toString()
    {
        return 
        "[ " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("uuu-L-d H:m:s")) + " ]" + 
        " [ " + this.type + " ] - " + 
        this.threadName + " - " + msg;
    }
}