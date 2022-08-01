package colabcode.connect;

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;

import colabcode.exception.PacketInconsistencyException;
import colabcode.gui.MainFrame;

public class ServerManager extends ConnectionManager {

    private LinkedList clients;
    private int nextID = 1;
    private Listener listener;
    private String password;
    private int maxConnections = 8;

    private javax.swing.text.Document document;
    private javax.swing.event.DocumentListener documentListener;

    public ServerManager(MainFrame parent, int port, String password, String name) {
        super(parent);
        listener = new Listener(this, port);
        clients = new LinkedList();
        this.password = password;
        mainFrame.getUserList().addClient(name);
        Client c = new Client();
        c.id = 0;
        c.name = name;
        c.socket = null;
        clients.add(c);
    }

    @Override
    synchronized public void onData(byte[] data) {
        Packet p = null;
        try {
            p = Protocol.receiveTranslate(data);
        } catch (PacketInconsistencyException e) {
            System.out.println("ERROR: ServerManager::onData()");
            return;
        }

        if (p.action == Protocol.GETCLIENTLIST) {
            onClientRequestList(p);
        } else if (p.action == Protocol.REQUEST) {
            onClientRequest(p);
        } else if (p.action == Protocol.ADDTEXT) {
            onInsert(p, this.document, this.documentListener);
        } else if (p.action == Protocol.REMOVETEXT) {
            onRemove(p, this.document, this.documentListener);
        } else if (p.action == Protocol.DISCONNECT) {
            onClientDisconnect(p);
        } else {
            System.out.println("UNIMPLEMENTED ACTION IN PACKET !");
        }
    }

    @Override
    public void sendInsert(int offset, String data) {
        Packet p = new Packet();
        p.action = Protocol.ADDTEXT;
        p.startPos = offset;
        p.data = data;

        sendPacketToAll(p);
    }

    @Override
    public void sendRemove(int offset, int length) {
        Packet p = new Packet();
        p.action = Protocol.REMOVETEXT;
        p.startPos = offset;
        p.endPos = length;

        sendPacketToAll(p);
    }

    @Override
    public void onShare(String s, javax.swing.text.Document d, javax.swing.event.DocumentListener dl) {
        Packet p = new Packet();
        p.action = Protocol.SHARE;

        String fName;
        int index = MainFrame.tabEditor.getSelectedIndex();
        List<javax.swing.JLabel> labelList = new ArrayList<>();
        MainFrame.getAllLabels(MainFrame.tabEditor, labelList);
        fName = labelList.get(index).getText();
        s = Integer.toString(fName.length()) + "." + fName + s;

        p.data = s;
        this.document = d;
        this.documentListener = dl;

        sendPacketToAll(p);
    }

    @Override
    public void destroy() {
        Packet p = new Packet();
        p.action = Protocol.EOF;

        listener.close();
        int n = 0;
        int size = clients.size();
        Client c;
        while (n < size) {
            c = ((Client) clients.get(n));
            if (c.id != 0) {
                c.socket.close();
            }
            ++n;
        }

        sendPacketToAll(p);
    }

    @Override
    public void onSocketException(ThreadedSocket s, Exception e) {
        int n = 0;
        int size = clients.size();
        Client c;
        while (n < size) {
            c = (Client) clients.get(n);
            if (c.socket == s) {
                Packet q = new Packet();
                q.action = Protocol.DISCONNECT;
                q.data = c.name;
                sendPacketToAllExceptID(q, q.clientID);
                clients.remove(n);
                break;
            }
            ++n;
        }
    }

    private void onClientRequestList(Packet p) {
        StringBuffer buf = new StringBuffer();
        synchronized (clients) {
            int size = clients.size();
            int pos = 0;
            while (pos < size) {
                buf.append(((Client) clients.get(pos)).name);
                buf.append('\0');
                pos++;
            }
        }
        Packet q = new Packet();
        q.action = Protocol.CLIENTLISTDATA;
        q.data = new String(buf);
        getSocketByID(p.clientID).writeData(Protocol.sendTranslate(q));
    }

    private void onClientRequest(Packet p) {
        Packet sendPacket = new Packet();
        ThreadedSocket s = getSocketByID(p.clientID);
        String[] tokens = p.data.split("\0");

        String name = tokens[0], password;
        try {
            password = tokens[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            password = new String();
        }

        if (!this.password.equals(password) || p.clientID < 1) {
            sendPacket.action = Protocol.UNAUTHORIZED;
            sendPacket.data = new String("INCORRECT PASSWORD");
        } else if (!checkName(name)) {
            sendPacket.action = Protocol.UNAUTHORIZED;
            sendPacket.data = new String("USERNAME ALREADY IN USE");
        } else {
            sendPacket.action = Protocol.ACCEPT;
            getClientByID(p.clientID).auth = true;
            getClientByID(p.clientID).name = name;
            mainFrame.getUserList().addClient(name);

            Packet q = new Packet();
            q.action = Protocol.NEWCLIENT;
            q.data = name;
            sendPacketToAllExceptID(q, p.clientID);
        }

        s.writeData(Protocol.sendTranslate(sendPacket));
    }

    public void onInsert(Packet p) {
        if (!mainFrame.insertText(p.startPos, p.data, document, documentListener)) {
            Packet q = new Packet();
            getSocketByID(p.clientID).writeData(Protocol.sendTranslate(q));
        } else {
            sendPacketToAllExceptID(p, p.clientID);
        }
    }

    public void onRemove(Packet p) {
        if (!mainFrame.removeText(p.startPos, p.endPos, document, documentListener)) {
            Packet q = new Packet();
            getSocketByID(p.clientID).writeData(Protocol.sendTranslate(q));
        } else {
            sendPacketToAllExceptID(p, p.clientID);
        }
    }

    private void onClientDisconnect(Packet p) {
        Client c = getClientByID(p.clientID);
        Packet q = new Packet();
        q.action = Protocol.DISCONNECT;
        q.data = c.name;
        sendPacketToAllExceptID(q, p.clientID);
        clients.remove(c);
        mainFrame.getUserList().removeClient(c.name);
    }

    synchronized private boolean checkName(String name) {
        if (name.equals("")) {
            return false;
        }
        int n = 0, size = clients.size();
        while (n < size) {
            try {
                if (((Client) clients.get(n)).name.equalsIgnoreCase(name)) {
                    return false;
                }
            } catch (NullPointerException e) {
                System.out.println("ERROR: ServerManager::checkName()");
            }
            ++n;
        }

        return true;
    }

    synchronized private Client getClientByID(int id) {
        if (id == 0) {
            return null;
        }
        int n = 0;
        int size = clients.size();
        while (n < size) {
            if (((Client) clients.get(n)).id == id) {
                break;
            }
            ++n;
        }
        if (((Client) clients.getLast()).id != id) {
            return null;
        }

        return (Client) clients.get(n);
    }

    private ThreadedSocket getSocketByID(int id) {
        return getClientByID(id).socket;
    }

    public void onListenerAcceptedClient(ThreadedSocket s) {
        if (clients.size() >= maxConnections) {
            Packet p = new Packet();
            p.action = Protocol.UNAUTHORIZED;
            p.data = new String("SERVER FULL");
        } else {
            Client c = new Client();
            c.id = this.nextID++;
            c.socket = s;

            Packet p = new Packet();
            p.action = Protocol.ASSIGNID;
            p.startPos = c.id;

            byte[] by = Protocol.sendTranslate(p);
            s.writeData(by);
            clients.add(c);
        }
    }

    private void sendPacketToAll(Packet p) {
        byte[] by = Protocol.sendTranslate(p);

        int size = clients.size();
        int pos = 0;
        Client c;
        while (pos < size) {
            c = (Client) clients.get(pos);
            if (c.id != 0) {
                c.socket.writeData(by);
            }
            ++pos;
        }
    }

    private void sendPacketToAllExceptID(Packet p, int id) {
        byte[] by = Protocol.sendTranslate(p);
        int curid;
        int pos = 0;
        int size = clients.size();
        while (pos < size) {
            curid = ((Client) clients.get(pos)).id;
            if (curid != id && curid != 0) {
                (((Client) clients.get(pos)).socket).writeData(by);
            }
            pos++;
        }
    }
}
