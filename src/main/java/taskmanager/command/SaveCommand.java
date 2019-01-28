package taskmanager.command;

public class SaveCommand extends AbstractCommand {
    SaveCommand(String[] command) {
        super(command);
    }

    @Override
    public void execute() throws Exception {
        writeToFile(consume(), getArg(1, "Out filename"));
    }
}
