package taskmanager.command;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CommandPipe implements Serializable {

    private Long id;
    public List<AbstractCommand> cmds = new ArrayList<>();

    CommandPipe(Long id) {
        this.id = id;
    }

    public void addCommand(AbstractCommand cmd) {
        cmds.add(cmd);
    }

    public List<AbstractCommand> getCommands() {
        return cmds;
    }

    public void removeCommand(AbstractCommand cmd) {
        cmds.remove(cmd);
    }

    public int size() {
        return cmds.size();
    }

    @Override
    public String toString() {
        return "CommandPipe_" + id;
    }
}
