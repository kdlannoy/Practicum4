/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import eventbroker.Event;

/**
 *
 * @author jrvdvyve
 */
public class ChatMessage extends Event {

    private String sender;
    
    public ChatMessage(String sender, String message) {
        super("chatmessage", message);
        this.sender = sender;

    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return super.getMessage();
    }
}
