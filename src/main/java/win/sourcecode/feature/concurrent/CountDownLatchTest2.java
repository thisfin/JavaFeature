package win.sourcecode.feature.concurrent;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchTest2 {
    public static void main(String[] args)
            throws InterruptedException {
        new CountDownLatchTest2().test();
    }

    private void test()
            throws InterruptedException {
        int count = 100;

        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(count);

        for (int i = 0; i < count; ++i) { // create and start threads
            new Thread(() -> {
                try {
                    startSignal.await();
                    // do some thing
                    doneSignal.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        // do some thing
        startSignal.countDown();      // let all threads proceed
        // do some thing
        doneSignal.await();           // wait for all to finish
    }
}
