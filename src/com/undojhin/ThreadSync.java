package com.undojhin;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadSync {

    private static ThreadSync thrdsync;
    private static Thread t1, t2, t3, t4, t5;
    private static final Random rand = new Random();
    private static Semaphore sm = new Semaphore(2);// 信号量 允许2个线程 true表示先进先出
    private static Semaphore wsm = new Semaphore(1);// 信号量 允许1个线程
    String text = "Beginning of the Book";// 代表书本
    AtomicInteger readerCount = new AtomicInteger(0); // 记录当前读者数量
    AtomicInteger writerCount = new AtomicInteger(0); // 记录当前写者数量

    // 随机休眠一定时间
    private void busy() {
        try {
            Thread.sleep(rand.nextInt(1000) + 1000);
        } catch (InterruptedException e) {
        }
    }

    // 写函数
    void write(String sentence) {
        System.out.println(Thread.currentThread().getName()
                + " started to WRITE");
        text += "\n" + sentence;
        System.out.println(text);
        System.out.println("End of Book\n");
        System.out.println(Thread.currentThread().getName()
                + " finished WRITING");
    }

    // 读函数
    void read() {

        System.out.println("\n" + Thread.currentThread().getName()
                + " started to READ");
        // System.out.println(text);
        // System.out.println("End of Book\n");

    }

    // 写者
    private class Writer implements Runnable {

        ThreadSync ts;

        Writer(ThreadSync ts) {
            super();
            this.ts = ts;

        }

        public void run() {
            while (true) {
                if (readerCount.get() == 0) { // 当没有读者时才 可以写
                    try {
                        //System.out.println("write---readerCount= "+readerCount.get());
                        //System.out.println("write---writerCount= "+writerCount.get());
                        wsm.acquire(); // 信号量获取允许
                        writerCount.getAndIncrement();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ThreadSync.class.getName()).log(
                                Level.SEVERE, null, ex);
                    }
                    String new_sentence = new String("\tnew line in Book");
                    busy();
                    ts.write(new_sentence);
                    wsm.release(); // 信号量释放
                    writerCount.getAndDecrement();

                    busy();

                }
            } // of while
        }
    }

    // 读者
    private class Reader implements Runnable {

        ThreadSync ts;

        Reader(ThreadSync ts) {
            super();
            this.ts = ts;

        }

        public void run() {
            while (true) {
                if (writerCount.get() == 0) { // 没有写者时 才可以读
                    try {
                        //System.out.println("Read---readerCount= "+readerCount.get());
                        //System.out.println("Read---writerCount= "+writerCount.get());
                        sm.acquire(); // 读者获取允许
                        readerCount.getAndIncrement();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ThreadSync.class.getName()).log(
                                Level.SEVERE, null, ex);
                    }
                    // System.out.print(t);

                    ts.read();
                    busy();
                    System.out.println(Thread.currentThread().getName()
                            + " end of read");
                    sm.release(); // 释放允许
                    readerCount.getAndDecrement();
                    busy();
                }
            } // of while
        }
    }

    // 创建两个读者 一个写者
    public void startThreads() {
        ThreadSync ts = new ThreadSync();
        t1 = new Thread(new Writer(ts), "Writer # 1");
        t2 = new Thread(new Writer(ts), "Writer # 2");
        t3 = new Thread(new Reader(ts), "Reader # 1");
        t4 = new Thread(new Reader(ts), "Reader # 2");
        // t5 = new Thread(new Reader(ts), "Reader # 3");
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        // t5.start();
    }

    public static void main(String[] args) {
        thrdsync = new ThreadSync();
        System.out.println("Lets begin...\n");
        thrdsync.startThreads();
    }
}

