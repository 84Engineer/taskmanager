package taskmanager.command;

import taskmanager.events.Events;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.function.Supplier;

import static taskmanager.events.Events.DOWNLOAD_FAILED;
import static taskmanager.events.Events.FILE_DOWNLOADED;

public class DownloadCommand extends AbstractCommand {

    DownloadCommand(String[] command) {
        super(command);
    }

    @Override
    public Events call() {
        return executeInGroup(() -> {
            try (InputStream in = new URL(command[1]).openStream()) {
                Files.copy(in, Paths.get(command[2]), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                return DOWNLOAD_FAILED;
            }
            return FILE_DOWNLOADED;
        });
    }

    private Events executeInGroup(Supplier<Events> callable) {
        Events res = callable.get();

        synchronized (lock) {
            eventQueue.offer(res);
            lock.notifyAll();
        }
        return res;
    }
}
