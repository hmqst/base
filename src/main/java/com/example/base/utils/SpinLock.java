package com.example.base.utils;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author benben
 * @program base
 * @Description 自旋锁
 * @date 2021-06-26 13:38
 */
public class SpinLock {

    private AtomicReference<Thread> reference = new AtomicReference<>();

    public void lock() {
        Thread thread = Thread.currentThread();
        while (!reference.compareAndSet(null, thread)) {

        }
    }

    public void unLock() {
        Thread thread = Thread.currentThread();
        reference.compareAndSet(thread, null);
    }
}
