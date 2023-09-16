package freed.camera;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class FreeDBuffer {
    public int id;
    public float pitch;
    public float yaw;
    public float roll;
    public float posZ;
    public float posX;
    public float posY;
    public int zoom;
    public int focus;

    public static FreeDBuffer decode(byte[] data) throws Exception {
        //if (checksum(data) == data[28]) {
        FreeDBuffer trackingData = new FreeDBuffer();
        trackingData.id = data[1];
        trackingData.pitch = getRotation(data, 2);
        trackingData.yaw = getRotation(data, 5);
        trackingData.roll = getRotation(data, 8);
        trackingData.posZ = getPosition(data, 11);
        trackingData.posX = getPosition(data, 14);
        trackingData.posY = getPosition(data, 17);
        trackingData.zoom = getEncoder(data, 20);
        trackingData.focus = getEncoder(data, 23);
        return trackingData;
        //}
        //throw new Exception("Calculated checksum does not match provided data. Probably not FreeD");
    }
    public String  toString(){
        StringBuilder builder = new StringBuilder();
        builder
                .append("pitch")
                .append(pitch)
                .append("yaw")
                .append(yaw)
                .append("roll")
                .append(roll)
                .append("posX")
                .append(posX)
                .append("posY")
                .append(posY)
                .append("posZ")
                .append(posZ)
                .append("zoom")
                .append(zoom)
                .append("focus")
                .append(focus)
        ;
        return builder.toString();
    }

    public static byte[] encode(FreeDBuffer data) {
        ByteBuffer buffer = ByteBuffer.allocate(32).order(ByteOrder.BIG_ENDIAN);
        buffer.put((byte) 0xD1); // Identifier
        buffer.put((byte) 0xFF); // ID
        buffer.put(setRotation(data.pitch));
        buffer.put(setRotation(data.yaw));
        buffer.put(setRotation(data.roll));
        buffer.put(setPosition(data.posZ));
        buffer.put(setPosition(data.posX));
        buffer.put(setPosition(data.posY));
        buffer.put(setEncoder(data.zoom));
        buffer.put(setEncoder(data.focus));
        buffer.put((byte) 0x00); // Reserved
        buffer.put((byte) 0x00); // Reserved
        buffer.put((byte) checksum(buffer.array()));
        return buffer.array();
    }

    public static byte[] setPosition(float pos) {
        long position = (long) (pos * 64 * 256);
        byte[] data = ByteBuffer.allocate(8).order(ByteOrder.BIG_ENDIAN).putLong(position).array();
        return new byte[]{data[4], data[5], data[6]};
    }

    public static byte[] setRotation(float rot) {
        long rotation = (long) (rot * 32768 * 256);
        byte[] data = ByteBuffer.allocate(8).order(ByteOrder.BIG_ENDIAN).putLong(rotation).array();
        return new byte[]{data[4], data[5], data[6]};
    }

    public static byte[] setEncoder(int enc) {
        ByteBuffer buffer = ByteBuffer.allocate(8).order(ByteOrder.BIG_ENDIAN);
        buffer.putInt(0);
        buffer.putInt(enc);
        return buffer.array();
    }

    public static float getPosition(byte[] data, int offset) {
        int value = ((data[offset] & 0xFF) << 24) | ((data[offset + 1] & 0xFF) << 16) | ((data[offset + 2] & 0xFF) << 8);
        return (float) value / 64 / 256;
    }

    public static float getRotation(byte[] data, int offset) {
        int value = ((data[offset] & 0xFF) << 24) | ((data[offset + 1] & 0xFF) << 16) | ((data[offset + 2] & 0xFF) << 8);
        return (float) value / 32768 / 256;
    }

    public static int getEncoder(byte[] data, int offset) {
        byte[] valueBytes = new byte[4];
        valueBytes[0] = 0x00;
        valueBytes[1] = data[offset + 1];
        valueBytes[2] = data[offset + 2];
        valueBytes[3] = data[offset + 3];
        ByteBuffer buffer = ByteBuffer.wrap(valueBytes).order(ByteOrder.BIG_ENDIAN);
        return buffer.getInt();
    }

    public static int checksum(byte[] data) {
        int sum = 64;
        for (int i = 0; i < 28; i++) {
            sum -= (data[i] & 0xFF);
        }
        return modulo(sum, 256);
    }

    public static int modulo(int d, int m) {
        int res = d % m;
        if ((res < 0 && m > 0) || (res > 0 && m < 0)) {
            return res + m;
        }
        return res;
    }
}