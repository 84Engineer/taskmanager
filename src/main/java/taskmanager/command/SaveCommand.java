package taskmanager.command;

import taskmanager.events.Events;

import java.nio.file.Files;
import java.nio.file.Paths;

public class SaveCommand extends AbstractCommand<String, Void> {
    SaveCommand(String[] command) {
        super(command);
    }

    @Override
    public Events call() throws Exception {
        Files.write(Paths.get(getArg(1, "Out filename")), consume().getBytes());
        return Events.FILE_SAVED;
    }
}
