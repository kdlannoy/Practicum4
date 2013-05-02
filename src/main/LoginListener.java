/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import chat.ChatPanel;
import eventbroker.EventBroker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import network.Network;

/**
 *
 * @author jrvdvyve
 */
public class LoginListener implements ActionListener {

    private JTextField usernametf;
    private JTextField portnumbertf;
    private boolean correct = true;
    private ArrayList<String> ipadressen = new ArrayList<String>();

    public LoginListener(JTextField usernametf, JTextField portnumbertf) {
        this.usernametf = usernametf;
        this.portnumbertf = portnumbertf;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (usernametf.getText().length() <= 0 || !isPositiveInt(portnumbertf.getText())) {
            correct = false;
            usernametf.setText("");
            portnumbertf.setText("");
        } else {
            //start threads om ip adressen te zoeken
            
            String adress = "192.168.0";
            final NetScanner lijst[] = new NetScanner[86];
            Thread t[] = new Thread[86];
            for (int i = 0; i < 86; i++) {
                NetScanner test = new NetScanner(adress, i*3);
                lijst[i]=test;
                t[i] = new Thread(lijst[i]);
            }
            for (int i = 0; i < 86; i++) {
                t[i].start();
            }
            
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        Thread.sleep(6000);
                        for (int i = 0; i < 86; i++) {
                            ipadressen.addAll(lijst[i].getAdressen());
                        }
                        
                        System.out.println(ipadressen.toString());
                        
                    } catch (InterruptedException ex) {
                        Logger.getLogger(LoginListener.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }).start();
            
            final JFrame chatvenster = new JFrame("Chatvenster");
            chatvenster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            try {
                chatvenster.add(chat.ChatPanel.createChat(InetAddress.getLocalHost().getHostName().toString() + "/" + usernametf.getText()));
            } catch (UnknownHostException ex) {
                Logger.getLogger(LoginListener.class.getName()).log(Level.SEVERE, null, ex);
            }
            JMenuBar mb = new JMenuBar();
            chatvenster.setJMenuBar(mb);
            JMenu menu = new JMenu("Menu");
            JMenuItem mi = new JMenuItem("Connect...");
            menu.add(mi);
            mb.add(menu);

//            eventbroker.EventBroker.getEventBroker().start();
//            
//            Network network = new Network(Integer.parseInt(portnumbertf.getText()));
//            try {
//                network.connect(InetAddress.getLocalHost(), 2345);
//            } catch (UnknownHostException ex) {
//                Logger.getLogger(LoginListener.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//            mi.addActionListener(new ActionListener() {
//
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    JDialog dialog = new JDialog(chatvenster, "Dialoog", true);
//                    dialog.add(new JLabel("Insert IP address and server port"));
//                    dialog.add(new JTextField());
//                    //veranderen naar showdialog
//                    dialog.setVisible(true);
//                }
//            });

            EventBroker.getEventBroker().start();
            final Network nwServer = new Network(Integer.parseInt(portnumbertf.getText()));
            ChatPanel cp = ChatPanel.createChat(usernametf.getText());
            chatvenster.getRootPane().setDefaultButton(cp.getjButton1());
            JMenuBar menubar = new JMenuBar();
            menubar.add(new JMenu("Menu").add(new JMenuItem(new AbstractAction("Connect ...") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String address = (String) JOptionPane.showInputDialog(chatvenster, "Insert IP address and port number");
                    try {
                        nwServer.connect(InetAddress.getByName(address.split(":")[0]), Integer.parseInt(address.split(":")[1]));

                    } catch (UnknownHostException  ex) {
                        System.out.println(ex);
                    }catch(NullPointerException ex){
                        System.out.println(ex);
                    }

                }
            })));
            chatvenster.setJMenuBar(menubar);
            chatvenster.repaint();

            chatvenster.setVisible(true);
            chatvenster.pack();


        }
    }

    private boolean isPositiveInt(String s) {
        int tmp = -1;
        try {
            tmp = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        if (tmp > 0) {
            return true;
        } else {
            return false;
        }
    }
}
