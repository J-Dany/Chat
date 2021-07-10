package Chat.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import com.google.gson.Gson;

import Chat.server.message.Message;

/**
 * @see https://github.com/pusher/websockets-from-scratch-tutorial/blob/master/README.md
 * @author Daniele Castiglia
 * @version 1.2.0
 */
public class WebSocketMessage 
{
    private byte[] encodedData;
    private String decoded;

    private Message message;
    private long encodeMessageSize;
    private ByteBuffer encodedMessage;

    public WebSocketMessage(byte[] data) throws Exception
    {
        this.encodedData = Arrays.copyOf(data, data.length);
    }

    public WebSocketMessage(Message msg) throws UnsupportedEncodingException
    {
        this.message = msg;
        this.encodedMessage = ByteBuffer.allocate(4096);
    }

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

    public void decodeData() throws Exception 
    {
        byte length = (byte) (this.encodedData[1] & 0x7f);
        long realLength = 0;

        int indexFirstMask = 2;
        int[] keys = new int[4];

        switch (length) 
        {
            case 127:

                for (int i = 2; i != 9; ++i)
                {
                    realLength |= this.encodedData[i] < 0 ? 128 - (-this.encodedData[i]) : this.encodedData[i];
                    realLength <<= 8;
                }

                indexFirstMask = 10;
                realLength += length + 8;
                break;
            case 126:
                realLength |= this.encodedData[2] < 0 ? 128 - (-this.encodedData[2]) : this.encodedData[2];
                realLength <<= 8;
                realLength |= this.encodedData[3] < 0 ? 128 - (-this.encodedData[3]) : this.encodedData[3];
                indexFirstMask = 4;
                realLength += length + 2;
                break;
            default:
                realLength = length;
        }

        for (int i = indexFirstMask, j = 0; i <= indexFirstMask + 3; ++i, ++j) 
        {
            keys[j] = this.encodedData[i];
        }

        int indexFirstData = indexFirstMask + 4;

        byte[] decoded = new byte[(int) realLength];

        for (int i = indexFirstData, j = 0; j < realLength; ++i, ++j) 
        {
            decoded[j] = (byte) (this.encodedData[i] ^ keys[j % 4]);
        }

        this.decoded = new String(decoded, 0, (int) realLength, "UTF-8");
    }

    public String getDecodedData() 
    {
        return this.decoded;
    }

    public byte[] getRawData()
    {
        return this.encodedData;
    }
}