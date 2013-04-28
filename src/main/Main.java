/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import chat.ChatPanel;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author jrvdvyve
 */
public class Main {
    
    private static void createAndShowGUI(){
        JFrame loginvenster = new JFrame("SO2 Demo");
        loginvenster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel paneel = new JPanel();
        
        JButton login = new JButton("Login");
        JLabel username = new JLabel("Username:");
        JLabel portnumber = new JLabel("Port number:");
        JTextField usernametf = new JTextField();
        JTextField portnumbertf = new JTextField();
        paneel.setLayout(new GridLayout(3,2));
        paneel.add(username);
        paneel.add(usernametf);
        paneel.add(portnumber);
        paneel.add(portnumbertf);
        paneel.add(login);
        loginvenster.add(paneel);
        login.addActionListener(new LoginListener(usernametf, portnumbertf));
        loginvenster.setVisible(true);
        loginvenster.pack();
        
        
        
    }
   
    
    public static void main(String[] args){
        javax.swing.SwingUtilities.invokeLater(new Runnable () {
            @Override
            public void run(){
                createAndShowGUI();
            }
        });        
    }
    
    
}
