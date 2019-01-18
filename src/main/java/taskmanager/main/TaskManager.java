package taskmanager.main;

import taskmanager.command.AbstractCommand;
import taskmanager.command.CommandFactory;
import taskmanager.events.Events;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class TaskManager {

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter command file location:");
        String input = sc.nextLine();

        List<AbstractCommand> commands = extractCommands(input);

        ExecutorService executorService = Executors.newCachedThreadPool();
        List<Future<Events>> results = commands.stream().map(executorService::submit).collect(toList());
        executorService.shutdown();

        startStatDeamon(sc, results);

        executorService.awaitTermination(1, TimeUnit.MINUTES);

        printReport(results);

    }

    private static List<AbstractCommand> extractCommands(String file) throws IOException {
        return CommandFactory.getCommands(Files.readAllLines(Paths.get(file)));
//        return Files.lines(Paths.get(file)).map(CommandFactory::getCommand)
//                .collect(toList());
    }

    private static void startStatDeamon(Scanner sc, List<Future<Events>> results) {
        Thread cmd = new Thread(() -> {
            while (true) {
                String in = sc.nextLine();
                if (in.toLowerCase().trim().equals("stat")) {
                    printReport(results);
                }
            }
        });

        cmd.setDaemon(true);
        cmd.start();
    }

    public static void printReport(List<Future<Events>> results) {
        System.out.println("***Application report***");
        //TODO print res according to exceptions thrown by get() method

        List<Future<Events>> doneTasks = results.stream().filter(Future::isDone).collect(toList());

        doneTasks.stream().map(f -> {
            try {
                return f.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return Events.UNKNOWN;
            } catch (ExecutionException e) {
                e.printStackTrace();
                return Events.UNKNOWN;
            }
        }).collect(toMap(
                identity(),
                k -> 1,
                (Integer i1, Integer i2) -> i1 + i2,
                () -> new EnumMap<>(Events.class)))
                .forEach((r, c) -> System.out.println(r.getLabel() + ": " + c));

        System.out.println("Unfinished jobs: " + (results.size() - doneTasks.size()));
    }

}
