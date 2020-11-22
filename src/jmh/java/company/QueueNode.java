package company;

public class QueueNode {
    Package pack;
    QueueNode next;

    public QueueNode(Package pack, QueueNode next) {
        this.pack = pack;
        this.next = next;
    }
    public QueueNode(Package pack) {
        this.pack = pack;
        this.next = null;
    }

    public void setNext(QueueNode next) {
        this.next = next;
    }

    public Package getNum() {
        return pack;
    }

    public QueueNode getNext() {
        return next;
    }
}
