package taskmanager.command;

import taskmanager.events.Events;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.SynchronousQueue;

public class CommandFactory {

    public static final String COMMAND_SEPARATOR = ">";

    public static List<AbstractCommand> getCommands(List<String> lines) {
        List<AbstractCommand> result = new ArrayList<>();
        for (String commandLine : lines) {
            if (isGroup(commandLine)) {
                List<AbstractCommand<?, ?>> commands = new ArrayList<>();
                for (String cmd : commandLine.split(COMMAND_SEPARATOR)) {
                    AbstractCommand command = getCommand(cmd.trim());
                    result.add(command);
                    commands.add(command);
                }

                for (int i = 1; i < commands.size(); i++) {
                    SynchronousQueue<?> queue = new SynchronousQueue<>();
                    commands.get(i - 1).setOut(queue);
                }
            } else {
                result.add(getCommand(commandLine));
            }
        }
        return result;
    }

    private static boolean isGroup(String commandLine) {
        return commandLine.contains(COMMAND_SEPARATOR);
    }

    public static AbstractCommand getCommand(String input) {
        String[] command = input.split(" ");

        switch (command[0].toLowerCase()) {
            case "d":
            case "download":
                return new DownloadCommand(command);
            case "countwords":
            case "cw":
                return new CountWordsCommand(command);
            case "delete":
            case "del":
                return new DeleteCommand(command);
            case "save":
            case "s":
                return new SaveCommand(command);
            default:
                throw new IllegalArgumentException("Unknown command: " + command[0]);
        }
    }

}
