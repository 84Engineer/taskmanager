package taskmanager.command;

import taskmanager.events.Events;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static taskmanager.events.Events.*;

public class CountWordsCommand extends AbstractCommand {

    CountWordsCommand(String[] command) {
        super(command);
    }

    @Override
    public Events call() {

        Supplier<Events> result = () -> {
            try {
                String inputFile = command[1];
                String outputFile = command[2];
                List<String> allWords = getAllWords(inputFile);
                saveWords(allWords, outputFile);
            } catch (IOException e) {
                return WORDS_COUNT_FAILED;
            }
            return WORDS_COUNTED;
        };

        return eventQueue == null
                ? result.get()
                : executeInGroup(result);

    }

    private Events executeInGroup(Supplier<Events> callable) {
        while (true) {
            synchronized (lock) {
                try {
                    if (eventQueue.peek() == FILE_DOWNLOADED) {
                        //proceed
                        eventQueue.poll();
                        break;
                    } else if (eventQueue.peek() == DOWNLOAD_FAILED) {
                        eventQueue.poll();
                        eventQueue.add(WORDS_COUNT_FAILED);
                        lock.notifyAll();
                        return WORDS_COUNT_FAILED;
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


    private List<String> getAllWords(String inputFile) throws IOException {
        return Files.lines(Paths.get(inputFile), Charset.forName("UTF-8")).map(l -> l.split("\\W+"))
                .flatMap(Stream::of).map(String::toLowerCase).distinct()
                .collect(Collectors.toList());
    }

    private void saveWords(List<String> words, String outputFile) throws IOException {
        Files.write(
                Paths.get(outputFile),
                String.format("Words count is %d%s%s",
                        words.size(),
                        System.getProperty("line.separator"),
                        String.join(", ", words))
                        .getBytes());
    }
}
