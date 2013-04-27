package network;

import alarmevent.*;
import java.io.*;

public class Server {

    public static void main(String[] args) throws IOException {
        int serverPort = 5555;
        Network netwerk = new Network(serverPort);
        //Server moet events ontvangen en versturen naar Hospitals, FireDepartments en shit

        new Hospital("UZ");
        new Hospital("AZ");
        new PoliceDepartment();
        new FireDepartment();

        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader in = new BufferedReader(isr);
        System.out.println("Press enter to terminate: ");
        if (in.readLine().equals("")) {
            netwerk.terminate();
        }


    }
}
