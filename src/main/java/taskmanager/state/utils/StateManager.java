package taskmanager.state.utils;

import taskmanager.command.AbstractCommand;
import taskmanager.command.CommandPipe;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Stream.of;

public class StateManager {

    public static final String SAVED_FOLDER_PATH = "src/main/resources/saved/";

    public static void saveCommands(List<AbstractCommand> cmds) throws IOException {
        for (AbstractCommand cmd : cmds) {
            saveCommandState(cmd);
        }
    }

    public static void saveCommandState(AbstractCommand cmd) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(getFilePath(cmd)))) {
            out.writeObject(cmd);
        }
    }

    public static void saveCommandPipe(CommandPipe pipe) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SAVED_FOLDER_PATH + pipe.toString()))) {
            out.writeObject(pipe);
        }
    }

    public static List<AbstractCommand> restoreCommandState() throws IOException, ClassNotFoundException {

        List<AbstractCommand> commands = new ArrayList<>();
        for (File file : new File(SAVED_FOLDER_PATH).listFiles()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = in.readObject();
                if (obj instanceof AbstractCommand) {
                    commands.add((AbstractCommand) obj);
                } else if (obj instanceof CommandPipe) {
                    commands.addAll(((CommandPipe) obj).getCommands());
                } else {
                    System.out.println("Skipping restoring object of type: " + obj.getClass().getSimpleName());
                }
            }
        }
        return commands;
    }

    private static String getFilePath(AbstractCommand cmd) {
        return SAVED_FOLDER_PATH + getFileName(cmd);
    }

    private static String getFileName(AbstractCommand cmd) {
        return cmd.toString() + "_" + cmd.getId();
    }

    public static void clearState() {
        File[] files = new File(SAVED_FOLDER_PATH).listFiles();
        if (files != null) {
            of(files).forEach(File::delete);
        }
    }

    public static void clearState(AbstractCommand cmd) {
        File[] files = new File(SAVED_FOLDER_PATH).listFiles();
        if (files != null) {
            of(files)
                    .filter(f -> f.getName().equals(getFileName(cmd)))
                    .findFirst().ifPresent(File::delete);
        }
    }

    public static void clearState(CommandPipe pipe) {
        File[] files = new File(SAVED_FOLDER_PATH).listFiles();
        if (files != null) {
            of(files)
                    .filter(f -> f.getName().equals(pipe.toString()))
                    .findFirst().ifPresent(File::delete);
        }
    }

}
