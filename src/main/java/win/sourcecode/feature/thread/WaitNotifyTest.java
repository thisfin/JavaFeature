package win.sourcecode.feature.thread;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.concurrent.TimeUnit;

// 顺序为 wait -> notify -> (running 或 sleep)
public class WaitNotifyTest {
    static boolean flag = true;

    public static void main(String[] args)
            throws InterruptedException {
        Object lock = new Object();

        new Thread(() -> {
            synchronized (lock) {
                while (flag) {
                    outThreadInfo(Thread.currentThread(), "wait");
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                outThreadInfo(Thread.currentThread(), "running");
            }
        }, "wait").start();

        TimeUnit.SECONDS.sleep(2);

        new Thread(() -> {
            synchronized (lock) {
                outThreadInfo(Thread.currentThread(), "hold lock, notify");
                lock.notifyAll();
                flag = false;
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            synchronized (lock) {
                outThreadInfo(Thread.currentThread(), "hold lock again, sleep");
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "notify").start();
    }

    private static void outThreadInfo(Thread thread, String message) {
        System.out.println(thread.getName() + " flag is true, " + message + " @" + DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).format(LocalDateTime.now()));
    }
}
