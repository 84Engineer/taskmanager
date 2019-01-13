package taskmanager.command;

public class CommandFactory {

    public static Command getCommand(String input) {
        String[] command = input.split(" ");

        switch (command[0].toLowerCase()) {
            case "d":
            case "download":
                return new DownloadCommand(command);
            case "countwords":
            case "cw":
            case "count":
                return new CountWordsCommand(command);
            case "q":
            case "quit":
                //TODO change
                System.exit(0);
            default:
                throw new IllegalArgumentException("Unknown command: " + command[0]);
        }
    }

}
