package Chat.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * @see https://github.com/pusher/websockets-from-scratch-tutorial/blob/master/README.md
 * @author Daniele Castiglia
 * @version 1.1.1
 */
public class WebSocketMessage 
{
    private byte[] encodedData;
    private String decoded;

    private String message;
    private ByteBuffer encodedMessage;

    public WebSocketMessage(byte[] data) throws Exception 
    {
        this.encodedData = Arrays.copyOf(data, data.length);
    }

    public WebSocketMessage(String msg) throws UnsupportedEncodingException
    {
        byte[] bytes = msg.getBytes(Charset.forName("UTF-8"));
        this.message = new String(bytes, 0, bytes.length, "UTF-8");
        this.encodedMessage = ByteBuffer.allocate(4096);
    }

    private void encodeMessage() throws IOException
    {
        byte[] messageByte = message.getBytes();

        byte firstByte = (byte) 0x81; // 129

        this.encodedMessage.put(firstByte);

        long messageSize = message.length();

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

        for (int j = 0; j < messageByte.length; ++j) 
        {
            this.encodedMessage.put(messageByte[j]);
        }
    }

    public byte[] getEncodedMessage() throws IOException
    {
        this.encodeMessage();

        return this.encodedMessage.array();
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
                realLength |= this.encodedData[2] < 0 ? 128 - (-this.encodedData[2]) : this.encodedData[2];
                realLength <<= 8;
                realLength |= this.encodedData[3] < 0 ? 128 - (-this.encodedData[3]) : this.encodedData[3];
                realLength <<= 8;
                realLength |= this.encodedData[4] < 0 ? 128 - (-this.encodedData[4]) : this.encodedData[4];
                realLength <<= 8;
                realLength |= this.encodedData[5] < 0 ? 128 - (-this.encodedData[5]) : this.encodedData[5];
                realLength <<= 8;
                realLength |= this.encodedData[6] < 0 ? 128 - (-this.encodedData[6]) : this.encodedData[6];
                realLength <<= 8;
                realLength |= this.encodedData[7] < 0 ? 128 - (-this.encodedData[7]) : this.encodedData[7];
                realLength <<= 8;
                realLength |= this.encodedData[8] < 0 ? 128 - (-this.encodedData[8]) : this.encodedData[8];
                realLength <<= 8;
                realLength |= this.encodedData[9] < 0 ? 128 - (-this.encodedData[9]) : this.encodedData[9];
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