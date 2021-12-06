package Chat.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;

import com.google.gson.Gson;

import Chat.server.callback.OnSocketWritingCallback;
import Chat.server.message.Message;

/**
 * @author Daniele Castiglia
 * @version 1.0.0
 */
public class ThatClientSocket
{
    /**
     * Function that is called each time something
     * is sent over the socket
     */
    private OnSocketWritingCallback onSendingMessage;

    /**
     * Flag for sending data encoded as
     * web socket message
     */
    private boolean isWebSocket = true;

    /**
     * The actual socket
     */
    private Socket socket;

    /**
     * Default ctor
     * @param socket
     */
    public ThatClientSocket(Socket socket)
    {
        this.socket = socket;
    }

    /**
     * Get the IP of the socket
     */
    public InetAddress getInetAddress()
    {
        return this.socket.getInetAddress();
    }

    /**
     * Set the flag {@code isWebSocket}
     * @param isWebSocket
     */
    public void setIsWebSocket(boolean isWebSocket)
    {
        this.isWebSocket = isWebSocket;
    }

    /**
     * Get the socket's input stream
     */
    public InputStream getInputStream() throws IOException
    {
        return this.socket.getInputStream();
    }

    /**
     * Close socket
     * @throws IOException
     */
    public void close() throws IOException
    {
        this.socket.close();
    }

    /**
     * Send the message over the socket
     * @param msg
     * @throws IOException
     */
    public void send(Message msg) throws IOException
    {
        byte[] buffer;
        String rawMessage = new Gson().toJson(msg);

        if (this.isWebSocket)
        {
            WebSocketMessage wbm = new WebSocketMessage(msg);

            buffer = wbm.getEncodedMessage();
        }
        else
        {
            buffer = rawMessage.getBytes();
        }

        if (onSendingMessage != null)
        {
            this.onSendingMessage.doJob(Thread.currentThread().getName(), rawMessage);
        }

        this.socket.getOutputStream().write(buffer);
        this.socket.getOutputStream().flush();
    }

    /**
     * Used for establishing a connection
     * @param buffer
     * @throws IOException
     */
    public void connectionMessage(byte[] buffer) throws IOException
    {
        this.socket.getOutputStream().write(buffer);
        this.socket.getOutputStream().flush();
    }

    /**
     * Set the callback function for the {@code send} method
     * @param callback
     */
    public void onSending(OnSocketWritingCallback callback)
    {
        this.onSendingMessage = callback;
    }
}