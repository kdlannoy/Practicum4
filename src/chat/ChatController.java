/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import eventbroker.Event;
import eventbroker.EventListener;
import eventbroker.EventPublisher;

/**
 *
 * @author jrvdvyve
 */
public class ChatController extends EventPublisher implements EventListener{
    
    ChatModel model;

    public ChatController(ChatModel model) {
        this.model = model;
        eventbroker.EventBroker.getEventBroker().addEventListener(this);
    }

    
    
    public void sendMessage(ChatMessage m){
        model.addMessage(m);
        eventbroker.EventBroker.getEventBroker().addEvent(this, m);
    }
    
    @Override
    public void handleEvent(Event e){
        model.addMessage((ChatMessage)e);
        
    }
}
