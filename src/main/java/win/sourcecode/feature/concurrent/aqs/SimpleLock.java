package win.sourcecode.feature.concurrent.aqs;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class SimpleLock
        extends AbstractQueuedSynchronizer {
    @Override
    protected boolean tryAcquire(int unused) {
        if (super.compareAndSetState(0, 1)) {
            super.setExclusiveOwnerThread(Thread.currentThread());
            return true;
        }
        return false;
    }

    @Override
    protected boolean tryRelease(int unused) {
        super.setExclusiveOwnerThread(null);
        super.setState(0);
        return true;
    }

    public void lock() {
        super.acquire(1);
    }

    public boolean tryLock() {
        return super.tryAcquire(1);
    }

    public void unlock() {
        super.release(1);
    }

    public boolean isLocked() {
        return super.isHeldExclusively();
    }

    public static void main(String[] args)
            throws InterruptedException {
        final SimpleLock simpleLock = new SimpleLock();
        simpleLock.lock();

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                simpleLock.lock();
                System.out.println(Thread.currentThread().getId() + " acquired the lock!");
                simpleLock.unlock();
            }).start();
            Thread.sleep(100);
        }

        System.out.println("main thread unlock!");
        simpleLock.unlock();
    }
}
