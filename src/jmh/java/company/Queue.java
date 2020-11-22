package company;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Queue {
    private static final int capacity = 5;
    private final AtomicInteger permits = new AtomicInteger(capacity);
    private volatile Boolean stop = false;
    QueueNode head;
    QueueNode tail;
    Condition notEmptyCondition, notFullCondition;
    private final ReentrantLock enqLock, deqLock;

    public Queue() {
        head = tail = new QueueNode(null, null);
        enqLock = new ReentrantLock();
        deqLock = new ReentrantLock();
        notFullCondition = enqLock.newCondition();
        notEmptyCondition = deqLock.newCondition();
    }

    public void enqueue(Package pack) {
        boolean wakeUpDeq = false;
        enqLock.lock();
        try {
            while (permits.get() == 0 && pack!=null) {
                notFullCondition.await();
            }
            QueueNode node = new QueueNode(pack);
            tail.next = node;
            tail = node;
            if (permits.getAndDecrement() == capacity)
                wakeUpDeq = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            enqLock.unlock();
        }
        if (wakeUpDeq) {
            deqLock.lock();
            try {
                notEmptyCondition.signalAll();
            } finally {
                deqLock.unlock();
            }
        }
    }

    public Package dequeue() {
        Boolean wakeUpEnq = false;
        deqLock.lock();
        QueueNode node = null;
        try {
            while (permits.get() == capacity) notEmptyCondition.await();
            node = head.next;
            head = node;
            if (permits.getAndIncrement() == 0)
                wakeUpEnq = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            deqLock.unlock();
        }
        if (wakeUpEnq) {
            enqLock.lock();
            try {
                notFullCondition.signalAll();
            } finally {
                enqLock.unlock();
            }
        }
        return node.pack;
    }

}
