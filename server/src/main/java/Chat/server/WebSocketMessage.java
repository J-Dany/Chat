import java.util.Arrays;

/**
 * @see https://github.com/pusher/websockets-from-scratch-tutorial/blob/master/README.md
 */
public class WebSocketMessage {
    private byte[] data;
    private String decoded;

    public WebSocketMessage(byte[] data) throws Exception {
        this.data = Arrays.copyOf(data, data.length);
        this.decodeData();
    }

    public void decodeData() throws Exception {
        byte length = (byte) (this.data[1] & 0x7f);
        long realLength = 0;

        int indexFirstMask = 2;
        int[] keys = new int[4];

        switch (length) {
            case 127:
                realLength |= this.data[2] < 0 ? 128 - (-this.data[2]) : this.data[2];
                realLength <<= 8;
                realLength |= this.data[3] < 0 ? 128 - (-this.data[3]) : this.data[3];
                realLength <<= 8;
                realLength |= this.data[4] < 0 ? 128 - (-this.data[4]) : this.data[4];
                realLength <<= 8;
                realLength |= this.data[5] < 0 ? 128 - (-this.data[5]) : this.data[5];
                realLength <<= 8;
                realLength |= this.data[6] < 0 ? 128 - (-this.data[6]) : this.data[6];
                realLength <<= 8;
                realLength |= this.data[7] < 0 ? 128 - (-this.data[7]) : this.data[7];
                realLength <<= 8;
                realLength |= this.data[8] < 0 ? 128 - (-this.data[8]) : this.data[8];
                realLength <<= 8;
                realLength |= this.data[9] < 0 ? 128 - (-this.data[9]) : this.data[9];
                indexFirstMask = 10;
                realLength += length + 8;
            break;
            case 126:
                realLength |= this.data[2] < 0 ? 128 - (-this.data[2]) : this.data[2];
                realLength <<= 8;
                realLength |= this.data[3] < 0 ? 128 - (-this.data[3]) : this.data[3];
                indexFirstMask = 4;
                realLength += length + 2;
            break;
            default:
                realLength = length;
        }

        for (int i = indexFirstMask, j = 0; i <= indexFirstMask + 3; ++i, ++j) {
            keys[j] = this.data[i];
        }

        int indexFirstData = indexFirstMask + 4;

        byte[] decoded = new byte[(int)realLength];

        for (int i = indexFirstData, j = 0; j < realLength; ++i, ++j) {
            decoded[j] = (byte)(this.data[i] ^ keys[j % 4]);
        }

        this.decoded = new String(decoded, 0, (int)realLength, "UTF-8");
    }

    public String getDecodedData()
    {
        return this.decoded;
    }

    public byte[] getRawData()
    {
        return this.data;
    }
}