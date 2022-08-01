package colabcode.connect;

import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import colabcode.gui.MainFrame;

public abstract class ConnectionManager {

    protected MainFrame mainFrame;

    public ConnectionManager(MainFrame frame) {
        this.mainFrame = frame;
    }

    public abstract void onData(byte[] data);

    public abstract void sendInsert(int offset, String data);

    public abstract void sendRemove(int start, int offset);

    public abstract void onShare(String s, Document d, DocumentListener dl);

    public abstract void destroy();

    protected void onNewClient(Packet p) {
        mainFrame.getUserList().addClient(p.data);
    }

    protected void onInsert(Packet p, Document d, DocumentListener dl) {
        mainFrame.insertText(p.startPos, p.data, d, dl);
    }

    protected void onRemove(Packet p, Document d, DocumentListener dl) {
        mainFrame.removeText(p.startPos, p.endPos, d, dl);
    }

    public abstract void onSocketException(ThreadedSocket s, Exception e);
}
