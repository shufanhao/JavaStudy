package com.example.ReentrantLock;

public class MyThread38 implements Runnable {
    private ThreadDomain38 threadDomain38;

    public MyThread38(ThreadDomain38 threadDomain38) {
        this.threadDomain38 = threadDomain38;
    }

    @Override
    public void run() {
        threadDomain38.testMethod();
    }
}
