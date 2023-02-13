package d1.project.resourcesharingmgmt.common.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * @author liuyi
 */
public class EventBus {

    private static EventBus instance;
    private final HashMap<String, List<IEvent>> dict = new HashMap<>();

    private EventBus() {

    }

    public static EventBus getInstance() {
        synchronized (EventBus.class) {
            if (instance == null) {
                instance = new EventBus();
            }
            return instance;
        }
    }

    public void on(String message, IEvent userListener) {
        synchronized (EventBus.class) {
            if (!dict.containsKey(message)) {
                dict.put(message, new ArrayList<>());
            }
            List<IEvent> listeners = dict.get(message);
            listeners.add(userListener);
        }
    }

    public void fire(String message, Collection<String> data) {
        if (dict.containsKey(message)) {
            List<IEvent> listeners = dict.get(message);
            listeners.forEach(listener -> listener.invoke(data));
        }
    }

}
