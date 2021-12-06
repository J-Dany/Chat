package Chat.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import com.google.gson.Gson;
import Chat.server.message.Message;

/**
 * @see https://github.com/pusher/websockets-from-scratch-tutorial/blob/master/README.md
 * @author Daniele Castiglia
 * @version 1.3.0
 */
public class WebSocketMessage
{
    private byte[] encodedData;

    private Message message;
    private long encodeMessageSize;
    private ByteBuffer encodedMessage;

    /**
     * Constructor used when receiving data from socket
     *  
     * @param data the data coming from the socket
     * @throws Exception
     */
    public WebSocketMessage(byte[] data) throws Exception
    {
        this.encodedData = Arrays.copyOf(data, data.length + 1);
    }

    /**
     * Constructor used when sending data to socket
     * 
     * @param msg the message object
     */
    public WebSocketMessage(Message msg)
    {
        this.message = msg;
        this.encodedMessage = ByteBuffer.allocate(8192);
    }

    /**
     * Returns the message as String (encoded as JSON)
     * @return
     */
    public String getMessage()
    {
        return new Gson().toJson(this.message).toString();
    }

    /**
     * Encode the message
     * 
     * @throws IOException
     */
    private void encodeMessage() throws IOException
    {
        byte firstByte = (byte) 0x81; // 129

        this.encodedMessage.put(firstByte);

        byte[] messageByte = new Gson().toJson(this.message).toString().getBytes();

        long messageSize = messageByte.length;
        this.encodeMessageSize = messageSize;

        if (messageSize <= 125)
        {
            this.encodedMessage.put((byte)messageSize);
        }
        else if (messageSize <= Math.pow(2, 16)) 
        {
            short mS = (short) messageSize;

            byte[] size = ByteBuffer.allocate(2).putShort(mS).array();
            
            this.encodedMessage.put((byte) 126);
            this.encodedMessage.put(size);
        } 
        else
        {
            byte[] size = ByteBuffer.allocate(8).putLong(messageSize).array();

            this.encodedMessage.put((byte) 127);
            this.encodedMessage.put(size);
        }

        for (int j = 0; j < messageSize; ++j) 
        {
            this.encodedMessage.put(messageByte[j]);
        }
    }

    /**
     * Encodes the message and returns
     * the buffer where the encoded message
     * is stored
     * 
     * @return byte[] the buffer where the encoded message is stored
     * @throws IOException
     */
    public byte[] getEncodedMessage() throws IOException
    {
        this.encodeMessage();

        byte[] tmp = null;

        if (this.encodeMessageSize <= 125)
        {
            tmp = Arrays.copyOf(this.encodedMessage.array(), 2 + (int) this.encodeMessageSize);
        }
        else if (this.encodeMessageSize <= Math.pow(2, 16))
        {
            tmp = Arrays.copyOf(this.encodedMessage.array(), 4 + (int) this.encodeMessageSize);
        }
        else
        {
            tmp = Arrays.copyOf(this.encodedMessage.array(), 10 + (int) this.encodeMessageSize);
        }

        this.encodedMessage.clear();

        return tmp;
    }

    /**
     * Decodes the data and returns it as
     * UTF-8 string
     * 
     * @return String the decoded data
     * @throws Exception
     */
    public String decodeData() throws Exception 
    {
        ByteBuffer wrapped = ByteBuffer.wrap(this.encodedData);

        // Ignore the first byte
        wrapped.get();

        byte length = (byte) (wrapped.get() & 0x7f);

        int indexFirstMask = 2;
        int[] keys = new int[4];

        byte[] decoded;

        switch (length) 
        {
            case 127:
                decoded = new byte[(int) wrapped.getLong()];
                indexFirstMask = 10;
                break;
            case 126:
                decoded = new byte[wrapped.getShort()];
                indexFirstMask = 4;
                break;
            default:
                decoded = new byte[length];
        }

        for (int i = indexFirstMask, j = 0; i <= indexFirstMask + 3; ++i, ++j) 
        {
            keys[j] = wrapped.get();
        }

        for (int i = 0; i < decoded.length; ++i)
        {
            decoded[i] = (byte) (wrapped.get() ^ keys[i % 4]);
        }

        return new String(decoded, 0, decoded.length, "UTF-8");
    }

    /**
     * Returns the raw data got from
     * the socket (data that are encoded)
     * @return byte[] the encoded data
     */
    public byte[] getRawData()
    {
        return this.encodedData;
    }
}