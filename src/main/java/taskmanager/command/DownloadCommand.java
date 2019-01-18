package taskmanager.command;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static taskmanager.command.ExecutionReport.*;
import static taskmanager.command.ExecutionReport.DOWNLOAD_FAILED;

public class DownloadCommand extends AbstractCommand {

    DownloadCommand(String[] command) {
        super(command);
    }

    @Override
    public ExecutionReport call() throws Exception {
        Thread.sleep(10000);
        URL website = new URL(command[1]);
        try (InputStream in = website.openStream()) {
            Files.copy(in, Paths.get(command[2]), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            return DOWNLOAD_FAILED;
        }
        return FILE_DOWNLOADED;
    }
}
