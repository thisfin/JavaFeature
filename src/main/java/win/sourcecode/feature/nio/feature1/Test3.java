package win.sourcecode.feature.nio.feature1;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Test3 {
    public static void main(String[] args) {
        FutureTask<Integer> futureTask = new FutureTask<>(Test1.callable);
        new Thread(futureTask).start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("main thread is run");

        try {
            System.out.println("future result: " + futureTask.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("all over");
    }
}
