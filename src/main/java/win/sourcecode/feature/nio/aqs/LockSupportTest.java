package win.sourcecode.feature.nio.aqs;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

// 阻塞 解除
public class LockSupportTest {
    private static Thread main;

    public static void main(String[] args) {
        main = Thread.currentThread();

        System.out.println("sub start ");
        new Thread(() -> {

            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(Thread.currentThread().getName() + " wakup other");
            LockSupport.unpark(main);
        }, "sub").start();

        System.out.println("main block");
        LockSupport.park(main);

        System.out.println("main coutinue");
    }
}
