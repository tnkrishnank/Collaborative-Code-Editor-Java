package colabcode.connect;

import java.net.*;
import java.io.IOException;

public class Listener extends Thread {

    private int port;
    private ServerManager manager;
    private ServerSocket serverSocket;

    public Listener(ServerManager manager, int port) {
        super("LISTENER");
        this.manager = manager;
        this.port = port;
        this.start();
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                if (this.isInterrupted()) {
                    break;
                }
                Socket client = new Socket();
                client = serverSocket.accept();
                manager.onListenerAcceptedClient(new ThreadedSocket(manager, client));
            }
        } catch (SocketException e) {
            System.out.println("ERROR: Listener::runText()");
        } catch (IOException e) {
            System.out.println("ERROR: Listener::runText()");
        }
    }

    public void close() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("ERROR: Listener::close()");
        }
    }
}
