package taskmanager.executor;

import taskmanager.command.AbstractCommand;
import taskmanager.state.utils.StateManager;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class StatExecutor extends ThreadPoolExecutor {

    private Map<String, Long> statMap = new ConcurrentHashMap<>();

    public StatExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        statMap.merge(
                (t == null ? "COMPLETED " : "FAILED ") + r.toString(),
                1L, Long::sum);
        StateManager.clearState((AbstractCommand) r);
    }

    public Map<String, Long> getStatMap() {
        return Collections.unmodifiableMap(statMap);
    }
}
