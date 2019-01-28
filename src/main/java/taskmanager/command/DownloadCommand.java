package taskmanager.command;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class DownloadCommand extends AbstractCommand {

    DownloadCommand(String[] command) {
        super(command);
    }

    @Override
    public void execute() throws Exception {
        String filePath = getArg(1, "File name");
        String[] parts = filePath.split(File.separator);
        String fileName = parts[parts.length - 1];
        try (InputStream in = new URL(filePath).openStream()) {
            Files.copy(in, Paths.get(fileName), StandardCopyOption.REPLACE_EXISTING);
            publish(fileName);
        }
    }

}
