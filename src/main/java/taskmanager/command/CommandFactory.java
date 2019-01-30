package taskmanager.command;

import taskmanager.state.utils.StateManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.SynchronousQueue;

public class CommandFactory {

    public static final String COMMAND_SEPARATOR = ">";
    private static long commandSequence = 0L;
    private static long commandPipeSequence = 0L;

    public static List<AbstractCommand> getCommands(List<String> lines) throws IOException {
        List<AbstractCommand> result = new ArrayList<>();
        for (String commandLine : lines) {
            if (isGroup(commandLine)) {
                CommandPipe pipe = new CommandPipe(commandPipeSequence++);
                for (String cmd : commandLine.split(COMMAND_SEPARATOR)) {
                    AbstractCommand command = getCommand(cmd.trim());
                    pipe.addCommand(command);
                    command.setCommandPipe(pipe);
                }
                connectCommands(pipe.getCommands());
                StateManager.saveCommandPipe(pipe);
                result.addAll(pipe.getCommands());
            } else {
                AbstractCommand command = getCommand(commandLine);
                result.add(command);
                StateManager.saveCommandState(command);
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
        long id = commandSequence++;

        switch (command[0].toLowerCase()) {
            case "d":
            case "download":
                cmd = new DownloadCommand(command, id);
                break;
            case "countwords":
            case "cw":
                cmd = new CountWordsCommand(command, id);
                break;
            case "delete":
            case "del":
                cmd = new DeleteCommand(command, id);
                break;
            case "save":
            case "s":
                cmd = new SaveCommand(command, id);
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

    public static List<AbstractCommand> restoreCommands() throws IOException, ClassNotFoundException {
        return StateManager.restoreCommandState();
    }
}
