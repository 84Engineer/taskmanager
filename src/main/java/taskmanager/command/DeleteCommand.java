package taskmanager.command;

import taskmanager.events.Events;

import java.nio.file.Files;
import java.nio.file.Paths;

public class DeleteCommand extends AbstractCommand/*<String, Void> */{

    DeleteCommand(String[] command) {
        super(command);
    }

    @Override
    public void execute() {
//        Files.delete(Paths.get(previous != null ? previous.get() : command[1]));
//        return null;
    }

}
