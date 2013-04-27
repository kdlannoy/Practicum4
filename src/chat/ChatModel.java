/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author jrvdvyve
 */
public class ChatModel {
    
    private String name;
    private List<ChatMessage> berichten;
    private List<ChangeListener> luisteraars;

    public ChatModel() {
        berichten = new ArrayList<ChatMessage>();
        luisteraars = new ArrayList<ChangeListener>();
    }
    
    public void addMessage(ChatMessage c){
        berichten.add(c);
        fireStateChanged();
        
    }
    
    public void addChangeListener(ChangeListener l){
        luisteraars.add(l);
    }

    public void removeChangeListener(ChangeListener l){
        luisteraars.remove(l);
    }
    public List<ChatMessage> getMessage() {
        return berichten;
    }
    
    

    public String getName() {
        return name;
        
    }

    public void setName(String gebruiker) {
        this.name = gebruiker;
        fireStateChanged();
    }
    
    private void fireStateChanged(){
        for (ChangeListener  l : luisteraars) {
            l.stateChanged(new ChangeEvent(l));
            
        }
    }
    
    
    //ChangeListener = panel
    /*
     * 
     * lijst van changelisteners
     * 
     * fireStateChanged
     * 
     * 
     */
    
    
    
    
    
    
}
