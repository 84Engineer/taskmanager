package taskmanager.command;

import taskmanager.events.Events;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Supplier;

import static taskmanager.events.Events.FILE_DELETED;
import static taskmanager.events.Events.FILE_DELETION_FAILED;

public class DeleteCommand extends AbstractCommand {

    DeleteCommand(String[] command) {
        super(command);
    }

    @Override
    public Events call() throws Exception {
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
            synchronized (eventQueue) {
                try {
                    if (eventQueue.peek() == Events.WORDS_COUNTED) {
                        eventQueue.poll();
                        Events res = callable.get();
                        eventQueue.offer(res);
                        eventQueue.notifyAll();
                        return res;
                    } else if (eventQueue.peek() == Events.WORDS_COUNT_FAILED) {
                        eventQueue.poll();
                        Events res = FILE_DELETION_FAILED;
                        eventQueue.offer(res);
                        eventQueue.notifyAll();
                        return res;
                    } else {
                        eventQueue.wait();
                    }
                } catch (InterruptedException e) {
                    //TODO check?
                }
            }
        }
    }

}
