package taskmanager.command;

public class SaveCommand extends AbstractCommand {
    SaveCommand(String[] command, long id) {
        super(command, id);
    }

    @Override
    public void execute() throws Exception {
        writeToFile(consume(), getArg(1, "Out filename"));
    }
}
