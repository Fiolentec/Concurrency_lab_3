package company;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class RingNode {
    private final int QU = 1;
    RingNode next;
    Queue queue1;
    ArrayBlockingQueue<Package> queue2;
    SynchronousQueue<Package> queue3;
    int nodeId;
    ArrayList<Long> times = new ArrayList<>();

    public RingNode(RingNode n, int nodeId) {
        switch (QU) {
            case 1:
                queue1 = new Queue();
                break;
            case 2:
                queue2 = new ArrayBlockingQueue<>(2);
                break;
            case 3:
                queue3 = new SynchronousQueue<>();
                break;
        }

        this.nodeId = nodeId;
        next = n;
    }

    public RingNode(int nodeId) {
        switch (QU) {
            case 1:
                queue1 = new Queue();
                break;
            case 2:
                queue2 = new ArrayBlockingQueue<>(2);
                break;
            case 3:
                queue3 = new SynchronousQueue<>();
                break;
        }
        this.nodeId = nodeId;
        next = null;
    }

    public void setNext(RingNode n) {
        next = n;
    }

    public void start() throws InterruptedException {
        while (true) {
            Package pack = null;
            switch (QU) {
                case 1:
                    pack = queue1.dequeue();
                    break;
                case 2:
                    pack = queue2.take();
                    break;
                case 3:
                    pack = queue3.take();
                    break;
            }
            if (pack.consumerId==-1)
                return;
            if (pack.consumerId != nodeId) {
                switch (QU) {
                    case 1:
                        next.queue1.enqueue(pack);
                        break;
                    case 2:
                        next.queue2.put(pack);
                        break;
                    case 3:
                        next.queue3.put(pack);
                        break;
                }
            } else {
                times.add((System.nanoTime() - pack.startTime));
            }
        }

    }

    public void handlePackage(Package pack) throws InterruptedException {
//        queue.enqueue(pack);
        switch (QU) {
            case 1:
                queue1.enqueue(pack);
                break;
            case 2:
                queue2.put(pack);
                break;
            case 3:
                queue3.put(pack);
                break;
        }
    }
}
