package taskmanager.command;

public class CommandFactory {

    public static AbstractCommand getCommand(String input) {
        String[] command = input.split(" ");

        switch (command[0].toLowerCase()) {
            case "d":
            case "download":
                return new DownloadCommand(command);
            case "countwords":
            case "cw":
            case "count":
                return new CountWordsCommand(command);
            default:
                throw new IllegalArgumentException("Unknown command: " + command[0]);
        }
    }

}
