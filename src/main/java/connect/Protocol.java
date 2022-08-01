package colabcode.connect;

import colabcode.exception.PacketInconsistencyException;

public class Protocol {

    public static final byte CLIENTLISTDATA = 1;
    public static final byte GETCLIENTLIST = 2;
    public static final byte NEWCLIENT = 3;
    public static final byte REQUEST = 4;
    public static final byte UNAUTHORIZED = 5;
    public static final byte ACCEPT = 6;
    public static final byte ASSIGNID = 7;
    public static final byte ADDTEXT = 8;
    public static final byte REMOVETEXT = 9;
    public static final byte SHARE = 10;
    public static final byte EOF = 11;
    public static final byte DISCONNECT = 12;

    public static byte[] sendTranslate(Packet action) {
        byte bAction = action.action;
        byte[] bClientInt = intToByte(action.clientID);
        byte[] bStartInt = intToByte(action.startPos);
        byte[] bStopInt = intToByte(action.endPos);

        if (action.data == null) {
            action.data = new String();
        }

        int n;
        byte[] bString = action.data.getBytes();
        int length = 1 + 4 + 4 + 4 + bString.length;
        byte[] b = new byte[length];

        b[0] = bAction;

        n = 0;
        while (n < 4) {
            b[1 + n] = bClientInt[n];
            ++n;
        }
        n = 0;
        while (n < 4) {
            b[5 + n] = bStartInt[n];
            ++n;
        }
        n = 0;
        while (n < 4) {
            b[9 + n] = bStopInt[n];
            ++n;
        }
        n = 0;
        while (n < bString.length) {
            b[13 + n] = bString[n];
            ++n;
        }

        return b;
    }

    public static Packet receiveTranslate(byte[] data) throws PacketInconsistencyException {
        if (data.length < 13) {
            throw new PacketInconsistencyException("PACKET TOO SMALL");
        }

        Packet action = new Packet();

        action.action = data[0];

        int n;
        byte[] id = new byte[4];
        byte[] start = new byte[4];
        byte[] stop = new byte[4];

        n = 0;
        while (n < 4) {
            id[n] = data[1 + n];
            ++n;
        }
        action.clientID = byteToInt(id);
        n = 0;
        while (n < 4) {
            start[n] = data[5 + n];
            ++n;
        }
        action.startPos = byteToInt(start);
        n = 0;
        while (n < 4) {
            stop[n] = data[9 + n];
            ++n;
        }
        action.endPos = byteToInt(stop);
        StringBuffer buf = new StringBuffer();
        n = 0;
        try {
            while (true) {
                buf.append((char) data[13 + n]);
                ++n;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("ERROR: Protocol::receiveTranslate()");
        }
        action.data = new String(buf);

        return action;
    }

    public static byte[] intToByte(int i) {
        byte[] b = new byte[4];
        int n = 0;
        while (n < 4) {
            b[n] = (byte) ((i >> ((3 - n) * 8)) & 0xFF);
            ++n;
        }

        return b;
    }

    public static int byteToInt(byte[] b) throws NumberFormatException {
        if (b.length != 4) {
            throw new NumberFormatException("CANNOT CONVERT BYTE TO INT");
        }
        int i = 0, n = 0;
        while (n < 4) {
            i = (i << 8) | (((int) b[n]) & 0xFF);
            ++n;
        }

        return i;
    }
}
