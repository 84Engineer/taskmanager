package taskmanager.command;

import taskmanager.events.Events;

import java.util.Queue;
import java.util.concurrent.Callable;

public abstract class AbstractCommand implements Callable<Events> {

    String name;
    String[] command;

    Queue<Events> eventQueue;

    AbstractCommand(String[] command) {
        this.name = command[0];
        this.command = command;
    }

    public String getName() {
        return name;
    }

    public void setEventQueue(Queue<Events> eventQueue) {
        this.eventQueue = eventQueue;
    }

}
