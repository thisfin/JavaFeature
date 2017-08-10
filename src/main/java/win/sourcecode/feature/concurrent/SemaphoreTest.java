package win.sourcecode.feature.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class SemaphoreTest {
    private Semaphore semaphore = new Semaphore(3);

    private void test() {
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        for (int i = 0; i < 20; i++) {
            int finalI = i;
            executorService.execute(() -> {
                try {
                    semaphore.acquire();
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println(finalI);
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            });
        }
        executorService.shutdown();

        Runnable runnable = ()->{
            System.out.println("");
        };

        Supplier<String> supplier = () -> {
            return "hello";
        };
    }

    public static void main(String[] args) {
        new SemaphoreTest().test();
    }
}
