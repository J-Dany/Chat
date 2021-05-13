package Chat.server;

import java.util.Arrays;

/**
 * @see https://github.com/pusher/websockets-from-scratch-tutorial/blob/master/README.md
 * @author Daniele Castiglia
 * @version 1.1.0
 */
public class WebSocketMessage 
{
    private byte[] encodedData;
    private String decoded;

    private String message;
    private byte[] encodedMessage;

    public WebSocketMessage(byte[] data) throws Exception 
    {
        this.encodedData = Arrays.copyOf(data, data.length);
    }

    public WebSocketMessage(String msg) 
    {
        this.message = String.copyValueOf(msg.toCharArray());
        this.encodedMessage = new byte[1024];
    }

    public void encodeMessage() 
    {
        byte[] messageByte = message.getBytes();

        byte firstByte = (byte) 0x81; // 129

        this.encodedMessage[0] = firstByte;

        long messageSize = message.length();
        int indexFirstByteOfMessage = 2;

        if (messageSize <= 125) 
        {
            this.encodedMessage[1] = (byte) messageSize;
        } 
        else if (messageSize <= Math.pow(2, 16)) 
        {
            this.encodedMessage[1] = (byte) 126;
            this.encodedMessage[2] = (byte) (messageSize & 0x000000000000FF00L);
            this.encodedMessage[3] = (byte) (messageSize & 0x00000000000000FFL);
            indexFirstByteOfMessage = 4;
        } 
        else 
        {
            this.encodedMessage[1] = (byte) 127;
            this.encodedMessage[2] = (byte) (messageSize & 0xFF00000000000000L);
            this.encodedMessage[3] = (byte) (messageSize & 0x00FF000000000000L);
            this.encodedMessage[4] = (byte) (messageSize & 0x0000FF0000000000L);
            this.encodedMessage[5] = (byte) (messageSize & 0x000000FF00000000L);
            this.encodedMessage[6] = (byte) (messageSize & 0x00000000FF000000L);
            this.encodedMessage[7] = (byte) (messageSize & 0x0000000000FF0000L);
            this.encodedMessage[8] = (byte) (messageSize & 0x000000000000FF00L);
            this.encodedMessage[9] = (byte) (messageSize & 0x00000000000000FFL);
            indexFirstByteOfMessage = 10;
        }

        for (int i = indexFirstByteOfMessage, j = 0; j < messageByte.length; ++i, ++j) 
        {
            this.encodedMessage[i] = messageByte[j];
        }
    }

    public byte[] getEncodedMessage()
    {
        return this.encodedMessage;
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