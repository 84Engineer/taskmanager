package experimental.after.execute.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class Test {

    public static void main(String[] args) throws InterruptedException {

        ExecutorService service = new CustomExecutor();
        Task task = new Task();
        service.submit(task);

        service.shutdown();
        service.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println(task.getNum());
    }

}
