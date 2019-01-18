package taskmanager.command;

import java.util.concurrent.Callable;

public abstract class AbstractCommand implements Callable<ExecutionReport> {

    String name;
    String[] command;

    AbstractCommand(String[] command) {
        this.name = command[0];
        this.command = command;
    }

    public String getName() {
        return name;
    }

}
