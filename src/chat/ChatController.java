/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import eventbroker.EventPublisher;

/**
 *
 * @author jrvdvyve
 */
public class ChatController extends EventPublisher{
    
    ChatModel model;

    public ChatController(ChatModel model) {
        this.model = model;
    }

    
    public void sendMessage(ChatMessage m){
        model.addMessage(m);
        eventbroker.EventBroker.getEventBroker().addEvent(this, m);
    }
}
