package network;

import eventbroker.Event;
import eventbroker.EventListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Network extends eventbroker.EventPublisher implements EventListener {

    private Connection connection = null;
    private List<Connection> lijst = null;
    private ConnectionListener listener = null;

    /*
     * Client zijde constructor
     */
    public Network() {
        //aanmaken Connection-object, gebruik makend van een nieuwe Socket (met IP-adres en poort-nummer van server)
        eventbroker.EventBroker.getEventBroker().addEventListener(this);
    }

    /*
     * Server zijde constructor
     */
    public Network(int serverPort) {
        //ConnectionListener aanmaken, dat wacht op de serverPort inkomende verzoeken
        //bij detectie inkomende verbinding, wordt een Connection-object aangemaakt, 
        //waarbij de socket gebruikt wordt die door accept() methode wordt teruggegeven


        /*
         * Lijst initialiseren
         * 
         * ConnectionListener Initialiseren
         * 
         * listener laten starten met luisteren
         */
        lijst = new ArrayList<Connection>();
        listener = new ConnectionListener(this, serverPort);
        new Thread(listener).start();

    }

    public Connection connect(InetAddress address, int port) {
        //client zijde connectie
        try {
            Socket socket = new Socket(address, port);
            connection = new Connection(socket, this);
            connection.receive(); //connectie is gemaakt, en nu mag je beginnen ontvangen
        } catch (IOException e) {
            System.err.println(e);
        }
        return connection;
    }

    public Connection connect(Socket socket) {
        //server zijde connectie -> connectionlistener
        Connection conn = new Connection(socket, this);
        conn.receive();
        lijst.add(conn);
        return conn;
    }

    @Override
    public void handleEvent(Event e) {
        //wordt alleen gebruikt als je als client bezig bent
        //events worden gestuurd naar de server
        if(connection != null){
            connection.send(e);
        }
    }

    public void terminate() {
        if (listener != null) {
            listener.terminate();
        }
        if (lijst != null) {
            for (Connection connection1 : lijst) {
                connection1.close();
            }
        }
        if (connection != null) {
            connection.close();
        }
    }

    void disconnect(Connection aThis) {
        for (Connection connection1 : lijst) {
            if(connection1.equals(aThis)){
                lijst.remove(aThis);
            }
        }
    }
}
