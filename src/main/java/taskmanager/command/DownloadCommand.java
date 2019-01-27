package taskmanager.command;

import taskmanager.events.Events;

import java.io.*;
import java.net.URL;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

public class DownloadCommand extends AbstractCommand<Void, Object> {

    DownloadCommand(String[] command) {
        super(command);
    }

    @Override
    public Events call() throws Exception {
        String filePath = getArg(1, "File name");
        String[] parts = filePath.split(File.separator);
        String fileName = parts[parts.length - 1];
        try (InputStream in = new URL(filePath).openStream()) {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(in);
            bufferedInputStream.
            while (in.)
            Files.copy(in, Paths.get(fileName), StandardCopyOption.REPLACE_EXISTING);
            publish(fileName);
        }
        return Events.FILE_DOWNLOADED;
    }

}
