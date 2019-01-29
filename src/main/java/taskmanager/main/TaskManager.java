package taskmanager.main;

import taskmanager.command.AbstractCommand;
import taskmanager.command.CommandFactory;
import taskmanager.executor.StatExecutor;
import taskmanager.state.utils.StateManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public class TaskManager {

    public static void main(String[] args) throws Exception {

        if (args.length == 0) {
            throw new IllegalArgumentException("Commands file not specified.");
        }

        List<AbstractCommand> commands = args[0].equalsIgnoreCase("--restore")
                ? CommandFactory.restoreCommands()
                : extractCommands(args[0]);

        StatExecutor executorService = new StatExecutor(
                0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<>());

        commands.forEach(executorService::execute);
        executorService.shutdown();

        Map<String, Long> statMap = executorService.getStatMap();

        startStatDaemon(statMap, 2000);

        executorService.awaitTermination(1, TimeUnit.MINUTES);

        printReport(statMap);

    }

    private static List<AbstractCommand> extractCommands(String file) throws IOException {
        StateManager.clearState();
        return CommandFactory.getCommands(Files.readAllLines(Paths.get(file)));
    }

    private static void startStatDaemon(Map<String, Long> statMap, long periodicity) {
        Thread cmd = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(periodicity);
                    printReport(statMap);
//                    System.exit(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        cmd.setDaemon(true);
        cmd.start();
    }


    public static void printReport(Map<String, Long> statMap) {
        System.out.println("***Application report***");

        List<Map.Entry<String, Long>> statList = new ArrayList<>(statMap.entrySet());
        statList.sort(Map.Entry.comparingByKey());

        statList.forEach(e -> System.out.println(e.getKey() + ": " + e.getValue()));
    }
}
