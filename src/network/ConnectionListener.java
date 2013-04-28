package network;

// listens for incoming connections
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionListener extends Thread {

    private Network network;
    private int serverPort;
    private Connection connection = null;
    private ServerSocket listen;

    public ConnectionListener(Network network, int serverPort) {
        this.network = network;
        this.serverPort = serverPort;
        try {
            listen = new ServerSocket(serverPort);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    @Override
    public void run() {
        try {
            //while (true) {
                Socket client = listen.accept();
                connection = network.connect(client);
            //}
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void terminate() {
        try {
            listen.close();
            if(connection != null) {
                connection.close();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
