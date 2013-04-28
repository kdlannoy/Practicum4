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
            final JFrame chatvenster = new JFrame("Chatvenster");
            chatvenster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            chatvenster.add(chat.ChatPanel.createChat("Chatvenster"));
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
                    } catch (UnknownHostException ex) {
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
