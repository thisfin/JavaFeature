package win.sourcecode.feature.thread;

// 变量线程私有
public class ThreadLocalTest {
    private static ThreadLocal<Integer> seqNum = ThreadLocal.withInitial(() -> 0);

    public int getNextNum() {
        seqNum.set(seqNum.get() + 1);
        return seqNum.get();
    }

    public static void main(String[] args) {
        ThreadLocalTest sn = new ThreadLocalTest();
        TestClient tc1 = new TestClient(sn);
        TestClient tc2 = new TestClient(sn);
        TestClient tc3 = new TestClient(sn);
        tc1.start();
        tc2.start();
        tc3.start();
    }

    private static class TestClient
            extends Thread {
        private ThreadLocalTest sn;

        public TestClient(ThreadLocalTest sn) {
            this.sn = sn;
        }

        @Override
        public void run() {
            for (int i = 0; i < 3; i++) {
                System.out.println("thread[" + Thread.currentThread().getName() + "] sn[" + sn.getNextNum() + "]");
            }
        }
    }
}
