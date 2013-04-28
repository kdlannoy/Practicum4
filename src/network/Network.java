package network;

import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Network extends EventPublisher implements EventListener {

    private int serverPort;
    private Connection c = null;
    private ConnectionListener connectionListener;

    public Network() { // Clientconstructor
        serverPort = 0;
        EventBroker.getEventBroker().addEventListener(this);
    }

    public Network(int serverPort) { // Serverconstructor
        this.serverPort = serverPort;
        EventBroker.getEventBroker().addEventListener(this);
        EventBroker e = EventBroker.getEventBroker();
        connectionListener = new ConnectionListener(this, serverPort);
        connectionListener.start();
    }

    public Connection connect(InetAddress address, int port) {
        try {
            Socket socket = new Socket(address, port);
            c = new Connection(socket, this);
            c.receive();
        } catch (IOException e) {
            System.err.println(e);
        }
        return c;
    }

    public Connection connect(Socket socket) {
        c = new Connection(socket, this);
        c.receive();
        return c;
    }

    @Override
    public void handleEvent(Event e) {
        if (c != null) {
            c.send(e);
        }
    }

    public void terminate() {
        if (c != null) {
            c.close();
        }
    }
}
