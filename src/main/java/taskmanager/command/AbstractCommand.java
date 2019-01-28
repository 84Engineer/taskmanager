package taskmanager.command;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public abstract class AbstractCommand implements Runnable {

    String name;
    String[] command;
    SynchronousQueue<String> in;
    SynchronousQueue<String> out;

    AbstractCommand(String[] command) {
        this.name = command[0];
        this.command = command;
    }

    @Override
    public void run() {
        try {
            execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract void execute() throws Exception;

    String getArg(int index, String argName) {
        if (index < command.length) {
            return command[index];
        }
        throw new IllegalArgumentException(argName + " not passed.");
    }

    public String getName() {
        return name;
    }

    public void setIn(SynchronousQueue<String> in) {
        this.in = in;
    }

    public void setOut(SynchronousQueue<String> out) {
        this.out = out;
    }

    void publish(String res) throws InterruptedException {
        if (out != null) {
            out.offer(res, 1, TimeUnit.MINUTES);
        }
    }

    String consume() throws InterruptedException {
        if (in != null) {
            return in.poll(1, TimeUnit.MINUTES);
        }
        return null;
    }

    void writeToFile(String output, String filePath) throws IOException {
        Files.write(Paths.get(filePath),
                output.getBytes());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
