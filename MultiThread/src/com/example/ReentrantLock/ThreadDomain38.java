package com.example.ReentrantLock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadDomain38 {
    private Lock lock = new ReentrantLock();

    public void testMethod() {
        try {
            lock.lock();
            for (int i=0; i<2;i++) {
                System.out.println("Thread name = " + Thread.currentThread().getName() + ", i =" + i );
            }
        } finally {
            lock.unlock();
        }
    }
}
