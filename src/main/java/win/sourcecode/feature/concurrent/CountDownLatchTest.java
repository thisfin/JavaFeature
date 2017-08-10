package win.sourcecode.feature.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CountDownLatchTest {
    private CountDownLatch countDownLatch = new CountDownLatch(2);

    private void test()
            throws InterruptedException {
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(1);
            countDownLatch.countDown();
            System.out.println(2);
            countDownLatch.countDown();
        }).start();
        countDownLatch.await();
        System.out.println(3);
    }

    public static void main(String[] args)
            throws InterruptedException {
        new CountDownLatchTest().test();
    }
}
