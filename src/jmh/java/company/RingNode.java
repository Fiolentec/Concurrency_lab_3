package company;

import java.util.ArrayList;

public class RingNode {
    RingNode next;
    Queue queue;
    int nodeId;
    ArrayList<Long> times = new ArrayList<>();

    public RingNode(RingNode n, int nodeId) {
        queue = new Queue();
        this.nodeId = nodeId;
        next = n;
    }

    public RingNode(int nodeId) {
        queue = new Queue();
        this.nodeId = nodeId;
        next = null;
    }

    public void setNext(RingNode n) {
        next = n;
    }

    public void start() {
        while (true) {
            Package pack = queue.dequeue();
            if (pack == null)
                return;
            if (pack.consumerId != nodeId) {
                next.queue.enqueue(pack);
            } else {
                times.add((System.nanoTime() - pack.startTime));
//                System.out.println(System.nanoTime() - pack.startTime);
            }
        }
    }

    public void handlePackage(Package pack) {
        queue.enqueue(pack);
    }
}
