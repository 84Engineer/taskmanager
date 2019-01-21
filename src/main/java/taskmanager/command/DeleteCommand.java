package taskmanager.command;

import java.nio.file.Files;
import java.nio.file.Paths;

public class DeleteCommand extends AbstractCommand<Void, String> {

    DeleteCommand(String[] command) {
        super(command);
    }

    @Override
    public Void call() throws Exception {
        Files.delete(Paths.get(previous != null ? previous.get() : command[1]));
        return null;
    }

}
