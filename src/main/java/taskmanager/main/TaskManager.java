package taskmanager.main;

import taskmanager.command.Command;
import taskmanager.command.CommandFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class TaskManager {

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter command file location:");
        String input = sc.nextLine();

        List<Command> commands = extractCommands(input);

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.invokeAll(commands);

        executorService.shutdown();

//        CommandFactory.getCommand(input).execute();
    }

    private static List<Command> extractCommands(String file) throws IOException {
        return Files.lines(Paths.get(file)).map(CommandFactory::getCommand)
                .collect(Collectors.toList());
    }

}
