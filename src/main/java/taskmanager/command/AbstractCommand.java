package taskmanager.command;

import taskmanager.events.Events;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

public abstract class AbstractCommand implements Callable<Events> {

    String name;
    String[] command;
    PipedInputStream in;
    PipedOutputStream out;

    AbstractCommand(String[] command) {
        this.name = command[0];
        this.command = command;
    }

    String getArg(int index, String argName) {
        if (index < command.length) {
            return command[index];
        }
        throw new IllegalArgumentException(argName + " not passed.");
    }

    public String getName() {
        return name;
    }

    public void setIn(PipedInputStream in) {
        this.in = in;
    }

    public void setOut(PipedOutputStream out) {
        this.out = out;
    }


    void writeToFile(String output, String filePath) throws IOException {
        Files.write(Paths.get(filePath), output.getBytes());
    }
}
