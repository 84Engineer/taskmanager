package taskmanager.command;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.SynchronousQueue;

public class CommandFactory {

    public static final String COMMAND_SEPARATOR = ">";

    public static List<AbstractCommand> getCommands(List<String> lines) {
        List<AbstractCommand> result = new ArrayList<>();
        for (String commandLine : lines) {
            if (isGroup(commandLine)) {
                List<AbstractCommand> groupCmds = new ArrayList<>();
                for (String cmd : commandLine.split(COMMAND_SEPARATOR)) {
                    groupCmds.add(getCommand(cmd.trim()));
                }
                connectCommands(groupCmds);
                result.addAll(groupCmds);
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

        AbstractCommand cmd;

        switch (command[0].toLowerCase()) {
            case "d":
            case "download":
                cmd = new DownloadCommand(command);
                break;
            case "countwords":
            case "cw":
                cmd = new CountWordsCommand(command);
                break;
            case "delete":
            case "del":
                cmd = new DeleteCommand(command);
                break;
            case "save":
            case "s":
                cmd = new SaveCommand(command);
                break;
            default:
                throw new IllegalArgumentException("Unknown command: " + command[0]);
        }

        return cmd;
    }

    private static void connectCommands(List<AbstractCommand> commands) {
        for (int i = 1; i < commands.size(); i++) {
            SynchronousQueue<String> queue = new SynchronousQueue<>();
            commands.get(i - 1).setOut(queue);
            commands.get(i).setIn(queue);
        }
    }

}
