package network;

import eventbroker.Event;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/*
 * Versturen en ontvangen van events 
 * 
 * 
 * 
 */
public class Connection {
    /*
     * Verbinding tussen server en client
     */

    private Socket socket = null;
    private ObjectInputStream input = null;
    private ObjectOutputStream output = null;
    private Network network = null;
    private boolean running = false;

    public Connection(Socket socket, Network network) {
        this.socket = socket;
        this.network = network;
        try {
            output = new ObjectOutputStream(socket.getOutputStream());
            output.flush();
            input = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.err.println(e);
        }



    }

    public void send(Event e) {
        try {
            output.writeObject(e);
            output.flush();

        } catch (IOException er) {
            System.err.println(er);
        }
    }

    public void receive() {
        running = true;
        new Thread(new ReceiverThread()).start();


    }

    public void close() {
        running = false;
        try {
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
            if(socket != null){
                socket.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
    
    private void RemoveMyself(){
        network.disconnect(this);
    }

    public class ReceiverThread implements Runnable {

        @Override
        public void run() {

            do {

                //event binnenkrijgen
                try {
                    eventbroker.Event event = (eventbroker.Event) input.readObject();
                    network.publishEvent(event);
                } catch (IOException e) {
                    System.err.println(e);
                    running = false; // als het een IOException is, dan kan het zijn dat er geen verbinding meer is tussen server en client
                    if(!socket.isConnected()){
                        RemoveMyself();
                    }
                } catch (ClassNotFoundException ex) {
                    System.err.println(ex);
                }
            } while (running);

        }
    }
}
