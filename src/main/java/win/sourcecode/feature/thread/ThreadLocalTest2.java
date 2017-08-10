package win.sourcecode.feature.thread;

public class ThreadLocalTest2 {
    private ThreadLocal<Integer> seqNum = ThreadLocal.withInitial(() -> 0);

    private void test() {
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                for (int j = 0; j < 3; j++) {
                    seqNum.set(seqNum.get() + 1);
                    System.out.println("thread[" + Thread.currentThread().getName() + "] sn[" + seqNum.get() + "]");
                }
            }).start();
        }
    }

    public static void main(String[] args) {
        new ThreadLocalTest2().test();
    }
}
