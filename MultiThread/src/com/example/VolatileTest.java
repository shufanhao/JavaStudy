package com.example;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * https://www.cnblogs.com/dolphin0520/p/3920373.html
 * 实践volatile关键字
 */
public class VolatileTest {

    public volatile int inc = 0;

    Lock lock = new ReentrantLock();

    public AtomicInteger incAtomic = new AtomicInteger();

    /** 因为inc ++ 不是原子的，所以最后输出不是10000
    public void increase() {
        inc ++ ;
    }*/

    /** 采用synchronized 可以实现原子
    public synchronized void increase() {
        inc++;
    }
    **/

    /**
     * 采用 lock
    public void increase() {
        lock.lock();
        try {
            inc++;
        } finally {
            lock.unlock();
        }
    }
     */
    /**
     * 采用 AtomicInteger
     */
    public void increase() {
        incAtomic.incrementAndGet();
    }

    public static void main(String[] args) {
        final VolatileTest test = new VolatileTest();
        for (int i=0;i <10; i++) {
            new Thread() {
                public void run() {
                    for (int j=0; j< 1000; j++) {
                        test.increase();
                    }
                }
            }.start();
        }
        while (Thread.activeCount() > 2) {
            Thread.yield();
        }
        System.out.println(test.inc);
        System.out.println(test.incAtomic);

    }
}
