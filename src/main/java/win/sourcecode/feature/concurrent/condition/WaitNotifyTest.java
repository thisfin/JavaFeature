package win.sourcecode.feature.concurrent.condition;

import java.util.PriorityQueue;

public class WaitNotifyTest {
    private final int QUEUE_SIZE = 10;
    private PriorityQueue queue = new PriorityQueue(QUEUE_SIZE);

    private void test() {
        new Thread(() -> {
            for (; ; ) {
                synchronized (queue) {
                    while (queue.size() == 0) {
                        try {
                            System.out.println("队列空 等待数据");
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            queue.notify();
                        }
                    }
                    queue.poll(); // 移走队首数据
                    queue.notify();
                    System.out.println("从队列中取走一个元素, 队列剩余空间 " + (QUEUE_SIZE - queue.size()));
                }
            }
        }).start();

        new Thread((() -> {
            for (; ; ) {
                synchronized (queue) {
                    while (queue.size() == QUEUE_SIZE) {
                        try {
                            System.out.println("队列满 等待有空余空间");
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            queue.notify();
                        }
                    }
                    queue.offer(1);
                    queue.notify();
                    System.out.println("向队列中插入一个元素, 队列剩余空间 " + (QUEUE_SIZE - queue.size()));
                }
            }
        })).start();
    }

    public static void main(String[] args) {
        new WaitNotifyTest().test();
    }
}
