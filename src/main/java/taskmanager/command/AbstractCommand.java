package taskmanager.command;

public abstract class AbstractCommand implements Command {

    String name;
    String[] command;

    AbstractCommand(String[] command) {
        this.name = command[0];
        this.command = command;
    }

    public String getName() {
        return name;
    }

}
