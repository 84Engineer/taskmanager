package taskmanager.command;

import taskmanager.events.Events;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.joining;

public class CountWordsCommand extends AbstractCommand<Stream<String>, String> {

    CountWordsCommand(String[] command) {
        super(command);
    }

    @Override
    public Events call() throws Exception {
        Map<String, Long> allWords = getAllWords(consume());
        publish(toOutputFormat(allWords));
        return Events.WORDS_COUNTED;
    }


    private Map<String, Long> getAllWords(Stream<String> input) {
        return input.map(l -> l.split("\\W+"))
                .flatMap(Stream::of).map(String::toLowerCase).distinct()
                .collect(Collectors.groupingByConcurrent(Function.identity(), counting()));
    }

    private String toOutputFormat(Map<String, Long> wordMap) {
        return wordMap.entrySet().stream()
                .map(e -> e.getKey() + ": " + e.getValue())
                .collect(joining(lineSeparator()));
    }


//    private void saveWords(List<String> words, String outputFile) throws IOException {
//        Files.write(
//                Paths.get(outputFile),
//                String.format("Words count is %d%s%s",
//                        words.size(),
//                        System.getProperty("line.separator"),
//                        String.join(", ", words))
//                        .getBytes());
//    }
}
