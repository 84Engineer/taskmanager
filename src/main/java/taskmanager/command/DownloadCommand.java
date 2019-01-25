package taskmanager.command;

import taskmanager.events.Events;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.stream.Stream;

public class DownloadCommand extends AbstractCommand<Void, Stream<String>> {

    DownloadCommand(String[] command) {
        super(command);
    }

    @Override
    public Events call() throws Exception {
        try (InputStream in = new URL(getArg(1, "File name")).openStream()) {
            publish(new BufferedReader(new InputStreamReader(in)).lines());
        }
        return Events.FILE_DOWNLOADED;
    }

}
