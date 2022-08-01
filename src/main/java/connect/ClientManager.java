package colabcode.connect;

import java.io.IOException;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import colabcode.exception.PacketInconsistencyException;
import colabcode.gui.DocListener;
import colabcode.gui.MainFrame;

public class ClientManager extends ConnectionManager {

    private int id;
    private ThreadedSocket socket;
    private String name;
    private String password;

    private javax.swing.text.Document document;
    private javax.swing.event.DocumentListener documentListener;

    public ClientManager(MainFrame parent, String hostname, int port, String password, String name) {
        super(parent);
        try {
            socket = new ThreadedSocket(this, hostname, port);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "ERROR INITIALIZING SOCKET");
        }
        this.password = password;
        this.name = name;
    }

    @Override
    public void onData(byte[] data) {
        Packet p = null;
        try {
            p = Protocol.receiveTranslate(data);
        } catch (PacketInconsistencyException e) {
            JOptionPane.showMessageDialog(null, "INCONSISTENT PACKET RECEIVED !");
            return;
        }

        if (p.action == Protocol.CLIENTLISTDATA) {
            onClientListData(p);
        } else if (p.action == Protocol.NEWCLIENT) {
            onNewClient(p);
        } else if (p.action == Protocol.UNAUTHORIZED) {
            onUnauthorized(p);
        } else if (p.action == Protocol.ACCEPT) {
            onAccept(p);
        } else if (p.action == Protocol.ASSIGNID) {
            onAssignID(p);
        } else if (p.action == Protocol.ADDTEXT) {
            onInsert(p, document, documentListener);
        } else if (p.action == Protocol.REMOVETEXT) {
            onRemove(p, document, documentListener);
        } else if (p.action == Protocol.SHARE) {
            onShare(p);
        } else if (p.action == Protocol.EOF) {
            onEOF(p);
        } else if (p.action == Protocol.DISCONNECT) {
            onDisconnect(p);
        } else {
            System.out.println("UNIMPLEMENTED ACTION IN PACKET !");
        }
    }

    @Override
    public void sendInsert(int offset, String data) {
        Packet p = new Packet();
        p.action = Protocol.ADDTEXT;
        p.clientID = this.id;
        p.startPos = offset;
        p.data = data;

        socket.writeData(Protocol.sendTranslate(p));
    }

    @Override
    public void sendRemove(int start, int offset) {
        Packet p = new Packet();
        p.action = Protocol.REMOVETEXT;
        p.clientID = this.id;
        p.startPos = start;
        p.endPos = offset;

        socket.writeData(Protocol.sendTranslate(p));
    }

    @Override
    public void onShare(String s, Document d, DocumentListener dl) {

    }

    @Override
    public void destroy() {
        Packet p = new Packet();
        p.action = Protocol.DISCONNECT;
        p.clientID = id;

        socket.writeData(Protocol.sendTranslate(p));
        socket.close();
        socket = null;
    }

    @Override
    public void onSocketException(ThreadedSocket s, Exception e) {
        JOptionPane.showMessageDialog(null, "SOCKET EXCEPTION : " + e.getCause());
        mainFrame.killConnectionManager();
    }

    private void onClientListData(Packet p) {
        mainFrame.getUserList().removeAllClients();
        StringTokenizer s = new StringTokenizer(p.data, "\0");
        while (s.hasMoreTokens()) {
            mainFrame.getUserList().addClient(s.nextToken());
        }
    }

    private void onUnauthorized(Packet p) {
        JOptionPane.showMessageDialog(null, "UNAUTHORIZED : " + p.data);
        mainFrame.killConnectionManager();
    }

    private void onAccept(Packet p) {
        Packet q = new Packet();
        q.clientID = id;

        socket.writeData(Protocol.sendTranslate(q));

        q.action = Protocol.GETCLIENTLIST;

        socket.writeData(Protocol.sendTranslate(q));
    }

    private void onAssignID(Packet p) {
        this.id = p.startPos;
        sendRequest();
    }

    private void onShare(Packet p) {
        mainFrame.getDocument().removeUndoableEditListener(mainFrame.getUndoManager());
        String s = p.data;
        String n = "";
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '.') {
                break;
            }
            n = n + s.charAt(i);
        }
        String fName = s.substring(n.length() + 1, Integer.valueOf(n) + n.length() + 1);
        s = s.substring(Integer.valueOf(n) + n.length() + 1);
        javax.swing.JTextArea newOpen = MainFrame.addNewTab(fName, s, "");
        document = newOpen.getDocument();
        documentListener = new DocListener(mainFrame.connectionManager, newOpen);
        document.addDocumentListener(documentListener);
        mainFrame.document = document;
        mainFrame.documentListener = documentListener;
        mainFrame.getDocument().addUndoableEditListener(mainFrame.getUndoManager());
    }

    private void onEOF(Packet p) {
        JOptionPane.showMessageDialog(null, "EOF : SERVER SHUTDOWN");
        mainFrame.killConnectionManager();
    }

    private void onDisconnect(Packet p) {
        mainFrame.getUserList().removeClient(p.data);
    }

    private void sendRequest() {
        Packet p = new Packet();
        p.action = Protocol.REQUEST;
        p.clientID = id;
        p.data = new String("" + name + "\0" + password);

        socket.writeData(Protocol.sendTranslate(p));
    }
}
