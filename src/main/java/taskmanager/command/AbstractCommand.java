package taskmanager.command;

import taskmanager.events.Events;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public abstract class AbstractCommand<P, T> implements Callable<P> {

    String name;
    String[] command;
    Future<T> previous;

    AbstractCommand(String[] command) {
        this.name = command[0];
        this.command = command;
    }

    AbstractCommand(String[] command, Future<T> previous) {
        this(command);
        this.previous = previous;
    }

    public String getName() {
        return name;
    }

}
