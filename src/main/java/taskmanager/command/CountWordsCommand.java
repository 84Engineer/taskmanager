package taskmanager.command;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CountWordsCommand extends AbstractCommand<String, String> {

    CountWordsCommand(String[] command) {
        super(command);
    }

    @Override
    public String call() throws Exception {

        String inputFile = consume(command[1]);
        String outputFile = previous != null ? command[1] : command[2];
        List<String> allWords = getAllWords(inputFile);
        saveWords(allWords, outputFile);
        return outputFile;
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
