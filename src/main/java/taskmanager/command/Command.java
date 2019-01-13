package taskmanager.command;

import java.util.concurrent.Callable;

public interface Command extends Callable<Void> {

    @Override
    default Void call() throws Exception {
        execute();
        return null;
    }

    void execute() throws Exception;
    String getName();
}
