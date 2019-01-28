package taskmanager.command;

import taskmanager.events.Events;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.joining;

public class CountWordsCommand extends AbstractCommand {

    CountWordsCommand(String[] command) {
        super(command);
    }

    @Override
    public Events execute() throws Exception {
        Stream<String> lines = in != null
                ? new BufferedReader(new InputStreamReader(in)).lines()
                : Files.lines(Paths.get(getArg(1, "Path to file")));
        Map<String, Long> allWords = getAllWords(lines);
        String result = toOutputFormat(allWords);
        if (out != null) {
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(out));
            bufferedWriter.write(result);
            bufferedWriter.flush();
        } else {
            writeToFile(result, getArg(2, "Out filename"));
        }
        return Events.WORDS_COUNTED;
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
