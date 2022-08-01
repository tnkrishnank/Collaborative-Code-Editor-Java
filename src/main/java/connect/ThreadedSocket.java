package colabcode.connect;

import java.io.*;
import java.net.*;

import colabcode.util.ByteBuffer;
import colabcode.exception.PacketInconsistencyException;

public class ThreadedSocket extends Thread {

    private Socket socket;
    private ConnectionManager manager;
    private InputStream in;
    private OutputStream out;
    private ByteBuffer buffer;
    private int curLength = 0;
    private int curPosition = 0;

    public ThreadedSocket(ConnectionManager cm, Socket s) throws IOException {
        super("THREADED SOCKET");
        this.in = s.getInputStream();
        this.out = s.getOutputStream();
        this.manager = cm;
        this.start();
    }

    public ThreadedSocket(ConnectionManager cm, String hostname, int port) throws IOException {
        super("THREADED SOCKET");
        this.socket = new Socket(hostname, port);
        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();
        this.manager = cm;
        this.start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                this.curLength = receivePacketSize();
                collectData();
            }
        } catch (Exception e) {
            System.out.println("ERROR: ThreadedSocket::run()");
        }
    }

    private void collectData() throws IOException, SocketException, PacketInconsistencyException {
        curPosition = 0;
        byte b;
        this.buffer = new ByteBuffer();
        while (true) {
            b = (byte) in.read();
            if (curLength == curPosition) {
                if (b != 0) {
                    throw new PacketInconsistencyException("NON TERMINATED EXCEPTION");
                }
                manager.onData(buffer.getBytes());
                break;
            } else {
                buffer.append(b);
                curPosition++;
            }
        }
    }

    private int receivePacketSize() throws IOException, PacketInconsistencyException, SocketException {
        byte[] size = new byte[4];
        int n = 0;
        while (n < 4) {
            size[n] = (byte) in.read();
            n++;
        }
        int iSize = Protocol.byteToInt(size);
        if (iSize > 65535) {
            throw new PacketInconsistencyException("PACKET TOO LARGE");
        }

        return iSize;
    }

    public void writeData(byte[] data) {
        try {
            out.write(Protocol.intToByte(data.length));
            out.write(data);
            out.write((byte) 0);
        } catch (IOException e) {
            System.out.println("ERROR: ThreadedSocket::writeData()");
        }
    }

    public void writeData(Packet p) {
        byte[] data = Protocol.sendTranslate(p);
        try {
            out.write(Protocol.intToByte(data.length));
            out.write(data);
            out.write((byte) 0);
        } catch (IOException e) {
            System.out.println("ERROR: ThreadedSocket::writeData()");
        }
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("ERROR: ThreadedSocket::close()");
        }
    }
}
