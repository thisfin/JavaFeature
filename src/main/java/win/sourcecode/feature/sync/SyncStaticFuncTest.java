package win.sourcecode.feature.sync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class SyncStaticFuncTest {
    public static void main(String[] args) {
        // Class 单例?
        System.out.println(Bean1.class.getSuperclass() == Bean2.class.getSuperclass());

        Semaphore semaphore1 = new Semaphore(20);
        ExecutorService executorService1 = Executors.newFixedThreadPool(20);
        for (int i = 0; i < 100000; i++) {
            executorService1.submit(() -> {
                try {
                    semaphore1.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Bean.adda();
                semaphore1.release();
            });
        }

        Semaphore semaphore2 = new Semaphore(20);
        ExecutorService executorService2 = Executors.newCachedThreadPool();
        for (int i = 0; i < 100000; i++) {
            executorService2.submit(() -> {
                try {
                    semaphore2.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Bean.addb();
                semaphore2.release();
            });
        }

        executorService1.shutdown();
        executorService2.shutdown();
        System.out.println(Bean.adda());
        System.out.println(Bean.addb());
    }
}

class Bean {
    private static int a = 0;
    private static int b = 0;

    public static synchronized int adda() { // 静态方法锁测试
        a = a + 1;
        return a;
    }

    public static int addb() {
        b = b + 1;
        return b;
    }
}

class Bean1
        extends Bean {
}

class Bean2
        extends Bean {
}
