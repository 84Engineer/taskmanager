package taskmanager.command;

import taskmanager.events.Events;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class DownloadCommand extends AbstractCommand {

    DownloadCommand(String[] command) {
        super(command);
    }

    @Override
    public Events execute() throws Exception {
        String filePath = getArg(1, "File name");
        String[] parts = filePath.split(File.separator);
        String fileName = parts[parts.length - 1];
        try (InputStream in = new URL(filePath).openStream()) {
            Path file = Paths.get(fileName);
            Files.copy(in, file, StandardCopyOption.REPLACE_EXISTING);
            if (out != null) {
                Files.copy(file, out);
            }
        }
        return Events.FILE_DOWNLOADED;
    }

}
