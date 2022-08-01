package colabcode.util;

public class ByteBuffer {

    private byte[] data;
    private int size;
    private int capacity;

    public ByteBuffer() {
        this(16);
    }

    public ByteBuffer(int capacity) {
        data = new byte[capacity];
        size = 0;
        this.capacity = capacity;
    }

    public void append(byte arg) {
        if (size + 1 > capacity) {
            grow(4);
        }
        data[size] = arg;
        size++;
    }

    public void append(byte[] arg) {
        if (size + arg.length > capacity) {
            grow(arg.length);
        }
        for (int i = 0; i < arg.length; i++) {
            data[size] = arg[i];
            size++;
        }
    }

    private void grow(int amount) {
        capacity = capacity + amount;
        byte[] newArray = new byte[capacity];
        for (int i = 0; i < size; i++) {
            newArray[i] = data[i];
        }
        data = newArray;
    }

    public byte[] getBytes() {
        byte b[] = new byte[size];
        for (int i = 0; i < size; i++) {
            b[i] = data[i];
        }

        return b;
    }
}