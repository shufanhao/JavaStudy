package com.example.ReentrantLock;

//https://www.cnblogs.com/xrq730/p/4855155.html
public class Main {

    public static void main(String args[]) throws Exception {
        ThreadDomain38 threadDomain38 = new ThreadDomain38();
        MyThread38 mt0 = new MyThread38(threadDomain38);
        MyThread38 mt1 = new MyThread38(threadDomain38);
        MyThread38 mt2 = new MyThread38(threadDomain38);

        mt0.run();
        mt1.run();
        mt2.run();


        ThreadDomain40 td = new ThreadDomain40();
        MyThread40 mt = new MyThread40(td);
        mt.start();
        Thread.sleep(3000);
        td.signal();
    }
}
