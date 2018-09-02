package com.example;
// https://blog.csdn.net/u012545728/article/details/80843595
// 不可重入锁
/*public class LockTest {
    private boolean isLocked = false;
    public synchronized void lock() {
        try {
            while (isLocked) {
                wait();
            }
        } catch (InterruptedException e) {
            System.out.println(e);
        }

        isLocked = true;
    }

    public synchronized void unlock() {
        isLocked = false;
        notify();
    }

    public static class Count {
        LockTest lock = new LockTest();
        public void print() {
            lock.lock();
            doAdd();
            // 不可重入的，因为执行doAdd的时候，没法执行。
            lock.unlock();
        }
        public void doAdd() {
            lock.lock();
            // Do something
            lock.unlock();
        }
    }
}*/
// 可重入锁
public class LockTest {
    boolean isLocked = true;
    Thread lockedBy = null;
    int lockedCount = 0;
    public synchronized void lock() throws InterruptedException {
        Thread thread = Thread.currentThread();
        while (isLocked && lockedBy != thread) {
            wait();
        }
        isLocked = true;
        lockedCount ++;
        lockedBy = thread;
    }

    public synchronized void unlock() {
        if (Thread.currentThread() == this.lockedBy) {
            lockedCount --;
            if (lockedCount == 0) {
                isLocked = false;
                notify();
            }
        }
    }
}

