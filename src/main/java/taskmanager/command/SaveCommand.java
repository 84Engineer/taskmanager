package taskmanager.command;

import taskmanager.events.Events;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SaveCommand extends AbstractCommand {
    SaveCommand(String[] command) {
        super(command);
    }

    @Override
    public Events execute() throws Exception {
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
            sb.append(System.lineSeparator());
        }
        writeToFile(
                sb.toString(),
                getArg(1, "Out filename"));
        return Events.FILE_SAVED;
    }
}
