package taskmanager.command;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class DownloadCommand extends AbstractCommand {

    DownloadCommand(String[] command) {
        super(command);
    }

    public void execute() throws Exception {
        URL website = new URL(command[1]);
        try (InputStream in = website.openStream()) {
            Files.copy(in, Paths.get(command[2]), StandardCopyOption.REPLACE_EXISTING);
        }
    }

}
