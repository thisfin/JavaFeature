package win.sourcecode.feature.nio;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierTest2 {
    private CyclicBarrier cyclicBarrier = new CyclicBarrier(2, () -> System.out.println(3)); // 达到临界点优先执行

    private void test() {
        new Thread(() -> {
            try {
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println(1);
        }).start();

        try {
            cyclicBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        System.out.println(2);
    }

    public static void main(String[] args) {
        new CyclicBarrierTest2().test();
    }
}
