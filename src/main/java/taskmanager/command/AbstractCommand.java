package taskmanager.command;

import taskmanager.events.Events;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Callable;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public abstract class AbstractCommand<IN, OUT> implements Callable<Events> {

    String name;
    String[] command;
    SynchronousQueue<IN> in;
    SynchronousQueue<OUT> out;

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

    public void setIn(SynchronousQueue<IN> in) {
        this.in = in;
    }

    public void setOut(SynchronousQueue<OUT> out) {
        this.out = out;
    }

    void publish(OUT res) throws InterruptedException {
        if (out != null) {
            out.offer(res, 1, TimeUnit.MINUTES);
        }
    }

    IN consume() throws InterruptedException {
        if (in != null) {
            return in.poll(1, TimeUnit.MINUTES);
        }
        return null;
    }

    void writeToFile(String output, String filePath) throws IOException {
        Files.write(Paths.get(filePath),
                output.getBytes());
    }
}
