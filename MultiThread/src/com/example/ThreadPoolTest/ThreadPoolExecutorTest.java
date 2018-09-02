package com.example.ThreadPoolTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolExecutorTest {
    public static void main(String args[]) {
        // 创建一个可缓存线程
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        for (int i=0; i<10; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cachedThreadPool.execute(new Runnable() {
                public void run() {
                    System.out.println(Thread.currentThread().getName()+"正在被执行");
                }
            });
        }
    }
}
