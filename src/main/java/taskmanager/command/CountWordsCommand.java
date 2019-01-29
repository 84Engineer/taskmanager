package taskmanager.command;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.joining;

public class CountWordsCommand extends AbstractCommand {
    CountWordsCommand(){}

    CountWordsCommand(String[] command, long id) {
        super(command, id);
    }

    @Override
    public void execute() throws Exception {
        String file = in != null ? consume() : getArg(1, "Path to file");
        Map<String, Long> allWords = getAllWords(Files.lines(
                Paths.get(file)));
        String result = toOutputFormat(allWords);
        if (out != null) {
            publish(result);
        } else {
            writeToFile(result, getArg(2, "Out filename"));
        }
    }


    private Map<String, Long> getAllWords(Stream<String> input) {
        return input.map(l -> l.split("\\W+"))
                .flatMap(Stream::of).filter(w -> !w.isEmpty()).map(String::toLowerCase)
                .collect(Collectors.groupingByConcurrent(Function.identity(), counting()));
    }

    private String toOutputFormat(Map<String, Long> wordMap) {
        List<Map.Entry<String, Long>> entries = new ArrayList<>(wordMap.entrySet());
        entries.sort(Map.Entry.comparingByValue());
        return entries.stream()
                .map(e -> e.getKey() + ": " + e.getValue())
                .collect(joining(lineSeparator()));
    }


}
