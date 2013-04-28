package network;

import eventbroker.Event;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection {

    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Network network;
    private boolean connected;

    public Connection(Socket socket, Network network) {
        try {
            this.socket = socket;
            this.network = network;
            output = new ObjectOutputStream(socket.getOutputStream());
            output.flush();
            input = new ObjectInputStream(socket.getInputStream());
            connected = true;

        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void send(Event e) {
        try {
            output.writeObject(e);
            output.flush();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    public void receive() {
        new ReceiverThread().start();
    }

    private class ReceiverThread extends Thread {

        @Override
        public void run() {
            try {
                while (true) {
                    if (input != null) {
                        Event e = (Event) input.readObject();
                        network.publishEvent(e);

                        if (!connected) {
                            return;
                        }
                    }
                }
            } catch (IOException e) {
                //System.err.println(e);
                // Als de client afgesloten zal hier een SocketException optreden
            } catch (ClassNotFoundException e) {
                System.err.println(e);
            }
        }
    }

    public void close() {
        connected = false;
    }
}
