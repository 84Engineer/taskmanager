package taskmanager.command;

import java.util.List;

public interface Command {
    void execute();
    String getName();
    List<String> getArguments();
}
