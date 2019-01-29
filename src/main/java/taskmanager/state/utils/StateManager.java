package taskmanager.state.utils;

import taskmanager.command.AbstractCommand;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Stream.of;

public class StateManager {

    public static final String SAVED_FOLDER_PATH = "src/resources/saved/";

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

    public static List<AbstractCommand> restoreCommandState() throws IOException, ClassNotFoundException {

        List<AbstractCommand> commands = new ArrayList<>();
        for (File file : new File(SAVED_FOLDER_PATH).listFiles()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                commands.add((AbstractCommand) in.readObject());
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
        of(new File(SAVED_FOLDER_PATH).listFiles()).forEach(File::delete);
    }

    public static void clearState(AbstractCommand cmd) {
        of(new File(SAVED_FOLDER_PATH).listFiles())
                .filter(f -> f.getName().equals(getFileName(cmd)))
                .findFirst().ifPresent(File::delete);
    }

}
