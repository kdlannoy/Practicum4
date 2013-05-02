package main;


import java.io.IOException;
import java.net.*;
import java.util.SortedSet;
import java.util.TreeSet;

public class NetScanner implements Runnable {

    String str;
    int nub;
    static SortedSet<String> adressen = new TreeSet<String>();

    public NetScanner(String str, int nub) {
        this.str = str;
        this.nub = nub;
    }
    /*  
     public void checkHost()
     {
     for(int i=0; i<256;i++)
     {
     if(IntelAddress.getByName(str+"."+i).isReachable(1000))
     System.out.println("host"+str+"."+i+"is connected");
     }

     }
     */

    public void run() {
        try {
            for (int i = nub; i < nub + 3; i++) {
                if (i<256 && InetAddress.getByName(str + "." + i).isReachable(2000)) {
                    System.out.println("host " + str + "." + i + " is connected");
                    System.out.println(InetAddress.getByName(str+"."+i).getCanonicalHostName());
                    adressen.add(str+"."+i);
                }else{
                }

            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public SortedSet<String> getAdressen() {
        return adressen;
    }

    
    public static void main(String[] args) throws UnknownHostException, IOException {
        //System.out.println("give subnet");
        //Scanner out = new Scanner(System.in);
        //NetScanner n = new NetScanner(out.next());
        
        //System.out.println(InetAddress.getByName("192.168.0.135").isReachable(5000));
        //System.out.println(InetAddress.getByName("192.168.0.247").isReachable(20000));


        Thread t[] = new Thread[86];
        for (int i = 0; i < 86; i++) {
            t[i] = new Thread(new NetScanner("172.16.0", i * 3));
        }
        for (int i = 0; i < 86; i++) {
            t[i].start();
        }
        
        
    }


}