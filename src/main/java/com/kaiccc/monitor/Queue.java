package com.kaiccc.monitor;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 任务队列
 */
public class Queue {

    private final int maxSize ;
    private AtomicInteger count = new AtomicInteger(0) ;
    private LinkedList<String> list = new LinkedList<>() ;
    private final Object lock = new Object() ;

    public int size(){
        return count.get() ;
    }

    public Queue(int size){
        this.maxSize = size ;
    }

    public void put(String str){
        synchronized(lock){
            while(size() == this.maxSize){
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            list.add(str) ;
            //计数器增加
            count.incrementAndGet() ;
            lock.notify();
        }
    }

    public String take(){
        String res;
        synchronized(lock){
            int minSize = 0;
            while(size() == minSize){
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            res = list.removeFirst();
            //计数器减1
            count.decrementAndGet() ;
            lock.notify();
        }
        return res ;
    }
}
