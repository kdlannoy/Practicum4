package eventbroker;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

final public class EventBroker implements Runnable {

    protected Set<EventListener> listeners = new HashSet<EventListener>();
    protected final static EventBroker broker = new EventBroker();
    protected Map<String, HashSet<EventListener>> filteredListeners = new HashMap<String, HashSet<EventListener>>();
    private LinkedList<QueueItem> queue = new LinkedList<QueueItem>();
    private boolean addPossibility = true;
    private EventPublisher last = null;
    private boolean stop = false;
    private boolean finishWork = false;

    private EventBroker() {
    }

    public LinkedList<QueueItem> getQueue() {
        return queue;
    }

    
    public static EventBroker getEventBroker() {
        return broker;
    }

    public void addEventListener(EventListener s) {
        listeners.add(s);
    }

    public void addEventListener(String type, EventListener s) {
        if (filteredListeners.get(type) == null) {
            filteredListeners.put(type, new HashSet<EventListener>());
        }
        filteredListeners.get(type).add(s);
    }

    public void removeEventListener(EventListener s) {
        listeners.remove(s);
    }

    public EventPublisher getLastEvent() {
        return last;
    }
    
    

    public synchronized void addEvent(EventPublisher source, Event e) {

        synchronized (queue) {
            last = source;
            queue.add(new QueueItem(source, e));
            queue.notifyAll();
        }


    }

    private void process(EventPublisher source, Event e) {
        for (EventListener l : listeners) {
            if (l != source) {
                l.handleEvent(e); // prevent loops !
            }
        }
        if (filteredListeners.get(e.getType()) != null) {
            for (EventListener el : filteredListeners.get(e.getType())) {
                el.handleEvent(e);
            }
        }
    }

    @Override
    public void run() {
        while (!stop) {
            QueueItem q = null;
            boolean notEmpty = false;
            if (finishWork) {
                synchronized (queue) {
                    while (!queue.isEmpty()) {
                        q = queue.removeFirst();
                        process(q.getSource(), q.getEvent());
                    }
                    stop = true;
                }
            } else {
                synchronized (queue) {
                    if (!queue.isEmpty()) {
                        q = queue.removeFirst();
                        notEmpty = true;
                    } else {
                            try {
                                queue.wait();
                            } catch (InterruptedException ex) {
                                System.err.println(ex);
                            }

                        
                    }
                    if (notEmpty) {
                        process(q.getSource(), q.getEvent());
                    }
                }
            }
        }
    }

    

    

    public void start() {
        new Thread(this).start();
    }

    public void stop() {
        finishWork = true;
        while (!queue.isEmpty()) {
        }
    }

    private class QueueItem {

        private Event e;
        private EventPublisher source;

        public QueueItem(EventPublisher source, Event e) {
            this.e = e;
            this.source = source;
        }

        public Event getEvent() {
            return e;
        }

        public EventPublisher getSource() {
            return source;
        }
    }
}
