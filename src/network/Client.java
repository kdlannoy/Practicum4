package network;

import alarmevent.*;
import java.io.*;
import java.net.InetAddress;

public class Client {

    
    public static void main(String[] args) throws IOException {
        //Callcenters aanmaken en shit
        Network netwerk = new Network();
        
        netwerk.connect(InetAddress.getLocalHost(), 5555);
        
        
        EmergencyCallCenter callCenter = new EmergencyCallCenter("112");
        EmergencyCallCenter callCenterPolice = new EmergencyCallCenter("101");
        
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader in = new BufferedReader(isr);
        
        String temp = "";
        String temp2 = "";
        while(!temp.equals("stop")){
            System.out.println("To stop, just type \"stop\"");
            System.out.println("Type of the emergencye: ");
            temp = in.readLine();
            if(!temp.equals("stop")){
                System.out.println("Adress: ");
            temp2 = in.readLine();
            callCenter.incomingCall(temp, temp2);
            callCenterPolice.incomingCall(temp, temp2);
            }            
        }
        netwerk.terminate();
        
               
    }
}
