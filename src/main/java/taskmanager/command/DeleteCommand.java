package taskmanager.command;

import taskmanager.events.Events;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Supplier;

import static taskmanager.events.Events.*;

public class DeleteCommand extends AbstractCommand {

    DeleteCommand(String[] command) {
        super(command);
    }

    @Override
    public Events call() {
        return executeInGroup(() -> {
            try {
                Files.delete(Paths.get(command[1]));
                return FILE_DELETED;
            } catch (IOException e) {
                return FILE_DELETION_FAILED;
            }
        });
    }

    private Events executeInGroup(Supplier<Events> callable) {
        while (true) {
            synchronized (lock) {
                try {
                    if (eventQueue.peek() == WORDS_COUNTED) {
                        //proceed
                        eventQueue.poll();
                        break;
                    } else if (eventQueue.peek() == WORDS_COUNT_FAILED) {
                        eventQueue.poll();
                        eventQueue.add(FILE_DELETION_FAILED);
                        lock.notifyAll();
                        return FILE_DELETION_FAILED;
                    } else {
                        lock.wait();
                    }
                } catch (InterruptedException e) {
                    //TODO -- ?
                }
            }
        }

        Events result = callable.get();

        synchronized (lock) {
            eventQueue.add(result);
            lock.notifyAll();
        }

        return result;
    }

}
