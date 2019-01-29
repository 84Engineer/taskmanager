package taskmanager.command;

public class DeleteCommand extends AbstractCommand/*<String, Void> */{

    DeleteCommand(String[] command, long id) {
        super(command, id);
    }

    @Override
    public void execute() {
//        Files.delete(Paths.get(previous != null ? previous.get() : command[1]));
//        return null;
    }

}
