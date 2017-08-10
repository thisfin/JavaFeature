package win.sourcecode.feature.concurrent.condition;

import java.util.PriorityQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionTest {
    private final int QUEUE_SIZE = 10;
    private PriorityQueue queue = new PriorityQueue(QUEUE_SIZE);
    private Lock lock = new ReentrantLock();
    private Condition notFullCondition = lock.newCondition();
    private Condition notEmptyCondition = lock.newCondition();

    private void test() {
        new Thread(() -> {
            for (; ; ) {
                lock.lock();
                try {
                    while (queue.size() == 0) {
                        try {
                            System.out.println("队列空 等待数据");
                            notEmptyCondition.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    queue.poll(); // 移走队首数据
                    notFullCondition.signal();
                    System.out.println("从队列中取走一个元素, 队列剩余空间 " + (QUEUE_SIZE - queue.size()));
                } finally {
                    lock.unlock();
                }
            }
        }).start();

        new Thread((() -> {
            for (; ; ) {
                lock.lock();
                try {
                    while (queue.size() == QUEUE_SIZE) {
                        try {
                            System.out.println("队列满 等待有空余空间");
                            notFullCondition.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    queue.offer(1);
                    notEmptyCondition.signal();
                    System.out.println("向队列中插入一个元素, 队列剩余空间 " + (QUEUE_SIZE - queue.size()));
                } finally {
                    lock.unlock();
                }
            }
        })).start();
    }

    public static void main(String[] args) {
        new ConditionTest().test();
    }
}
