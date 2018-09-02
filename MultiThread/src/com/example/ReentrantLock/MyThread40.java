package com.example.ReentrantLock;

public class MyThread40 extends Thread{
    private ThreadDomain40 td;

    public MyThread40(ThreadDomain40 td) {
        this.td = td;
    }

    public void run() {
        td.await();
    }
}
