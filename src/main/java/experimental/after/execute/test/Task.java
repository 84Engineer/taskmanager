package experimental.after.execute.test;

import java.util.concurrent.Callable;

public class Task implements Callable<Integer> {

    private int num;

    @Override
    public Integer call() throws Exception {
        return num = num++;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
