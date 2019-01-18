package taskmanager;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.stream.IntStream;

import static java.util.stream.IntStream.range;

public class Test {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();

//        List<Future<?>> results = new ArrayList<>();

        List<Callable<Void>> calls = new ArrayList<>();

        for (int i = 1; i < 5; i++) {
            calls.add(() -> {
                try {
                    Thread.sleep( 10000 );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            });
        }

        List<Future<Void>> results = executorService.invokeAll(calls);

        executorService.shutdown();

        Scanner sc = new Scanner(System.in);

        while (true) {
            sc.nextLine();
            results.forEach(f -> System.out.println(f.isDone()));
        }

//        results.forEach(f -> System.out.println(f.isDone()));


    }

}
