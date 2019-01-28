package taskmanager.main;

import taskmanager.command.AbstractCommand;
import taskmanager.command.CommandFactory;
import taskmanager.events.Events;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class TaskManager {

    public static void main(String[] args) throws Exception {

        if (args.length == 0) {
            throw new IllegalArgumentException("Commands file not specified.");
        }

        List<AbstractCommand> commands = extractCommands(args[0]);

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 10);
        List<Future<Events>> results = commands.stream().map(executorService::submit).collect(toList());
        executorService.shutdown();

        startStatDeamon(results, 5000);

        executorService.awaitTermination(1, TimeUnit.MINUTES);

        printReport(results);

    }

    private static List<AbstractCommand> extractCommands(String file) throws IOException {
        return CommandFactory.getCommands(Files.readAllLines(Paths.get(file)));
    }

    private static void startStatDeamon(List<Future<Events>> results, long periodicity) {
        Thread cmd = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(periodicity);
                    printReport(results);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        cmd.setDaemon(true);
        cmd.start();
    }

    public static void printReport(List<Future<Events>> results) {
        System.out.println("***Application report***");

        List<Future<Events>> doneTasks = results.stream().filter(Future::isDone).collect(toList());
        List<Exception> exceptions = new ArrayList<>();

        doneTasks.stream().map(f -> {
            try {
                return f.get();
            } catch (Exception e) {
                exceptions.add(e);
                return Events.EXCEPTION;
            }
        }).collect(toMap(
                identity(),
                k -> 1,
                (Integer i1, Integer i2) -> i1 + i2,
                () -> new EnumMap<>(Events.class)))
                .forEach((r, c) -> System.out.println(r.getLabel() + ": " + c));

        System.out.println("Unfinished jobs: " + (results.size() - doneTasks.size()));

        if (!exceptions.isEmpty()) {
            System.out.println("Exceptions thrown by tasks: ");
            exceptions.forEach(e -> {
                e.printStackTrace();
                System.out.println("*******************************************");
            });
        }
    }

}
