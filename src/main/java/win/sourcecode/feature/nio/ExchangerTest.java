package win.sourcecode.feature.nio;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

public class ExchangerTest {
    private Exchanger<String> exchanger = new Exchanger<>();

    private void test() {
        new Thread(() -> {
            String name = "A";
            try {
                String result1 = exchanger.exchange(name);
                System.out.println("A result: " + result1);
                String result2 = exchanger.exchange(name);
                System.out.println("A result: " + result2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            String name = "B";
            try {
                TimeUnit.SECONDS.sleep(2);
                String result = exchanger.exchange(name);
                System.out.println("B result: " + result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            String name = "C";
            try {
                TimeUnit.SECONDS.sleep(2);
                String result = exchanger.exchange(name);
                System.out.println("C result: " + result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        System.out.println("main over");
    }

    public static void main(String[] args) {
        new ExchangerTest().test();
    }
}
