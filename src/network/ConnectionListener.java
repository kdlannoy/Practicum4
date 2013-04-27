package network;

// listens for incoming connections
import eventbroker.Event;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionListener implements Runnable {

    private ServerSocket server = null;
    private Network netwerk = null; // de server
    private boolean run = false;

    public ConnectionListener(Network network, int serverPort) {
        this.netwerk = network;
        try {
            server = new ServerSocket(serverPort);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void terminate() {
        run = false;
        if (server != null) {
            try {
                server.close();
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    @Override
    public void run() {
        run = true;
        while (run) {
            try {
                Socket socket = server.accept();
                netwerk.connect(socket); //verbinden met de server
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }
}
