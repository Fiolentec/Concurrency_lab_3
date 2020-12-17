package company;

import java.util.Random;

public class TokenRing {
    int length;
    RingNode[] array;

    public TokenRing(int desiredLength) {
        array = new RingNode[desiredLength];
        length = desiredLength;
        RingNode node = new RingNode(0);
        array[0] = node;
        addNodes(desiredLength);

    }

    private void addNodes(int len) {
        for (int i = 1; i < len; i++) {
            RingNode node = new RingNode(i);
            array[i] = node;
        }

        for (int i = 1; i < len - 1; i++) {
            array[i].setNext(array[i + 1]);
        }
        array[0].setNext(array[1]);
        array[len - 1].setNext(array[0]);
    }

    public void computePackage(int K) throws InterruptedException {
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < K; j++) {
                    try {
                        array[random.nextInt(length - 1)]
                                .handlePackage(new Package(random.nextInt(length - 1), System.nanoTime()));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public void computePackageConcreteAll(int n, int k) throws InterruptedException {
        array[n].handlePackage(new Package(k, System.nanoTime()));
    }

    public void computePackageConcreteNode(int n) throws InterruptedException {
        Random random = new Random();
        array[n].handlePackage(new Package(random.nextInt(length - 1), System.nanoTime()));
    }

    public void start() {
        for (RingNode r : array) {
            new Thread(
                    () -> {
                        try {
                            r.start();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
            ).start();
        }
    }

    public void stopAll() throws InterruptedException {
        for (RingNode r : array) {
            r.handlePackage(new Package(-1,0L));
        }
    }
}
