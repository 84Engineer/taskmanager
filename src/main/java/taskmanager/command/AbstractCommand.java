package taskmanager.command;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public abstract class AbstractCommand implements Runnable, Serializable {

    String name;
    String[] command;
    Long prevId;
    Long nextId;
    Long id;
    boolean completed;
    transient SynchronousQueue<String> in;
    transient SynchronousQueue<String> out;

    AbstractCommand() {

    }

    AbstractCommand(String[] command, long id) {
        this.name = command[0];
        this.command = command;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            completed = true;
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

    public Long getId() {
        return id;
    }

    public Long getPrevId() {
        return prevId;
    }

    public void setPrevId(Long prevId) {
        this.prevId = prevId;
    }

    public Long getNextId() {
        return nextId;
    }

    public void setNextId(Long nextId) {
        this.nextId = nextId;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
