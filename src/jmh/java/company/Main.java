package company;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static company.DrawGraph.createAndShowGui;


public class Main {
    final static int N = 12000;
    final static int K = 24;
    final static int ringLength = 12;
    final static int warmupIteration = 5;
    static int a = 1;
    final static boolean sequentialLoad = false;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Program started");
        for (int i = 0; i < warmupIteration; i++) {
            measure(true);
        }
        measure(false);
    }

    private static void measure(Boolean warmup) throws InterruptedException {
        long startTime = System.nanoTime();
        TokenRing tokenRing = new TokenRing(ringLength);
        tokenRing.start();

        if (sequentialLoad) {
            for (int i = 0; i < N / K; ++i) {
                new Thread(() -> {
                    for (int j = 0; j < K; j++) {
                        try {
                            tokenRing.computePackageConcreteNode(j);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        } else {
            for (int i = 0; i < N / (10 * K); ++i) {
                tokenRing.computePackage(K);
            }
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tokenRing.stopAll();
        int packagesNum = 0;
        long elapsedTime = ((System.nanoTime() - startTime) / 1000000) - 2000;
        String str = "";
        if (warmup)
            str = String.format("Warmup iteration № %s. Time: %s ms", a, elapsedTime);
        else
            str = String.format("Measure iteration. Time: %s ms", elapsedTime);
        System.out.println(str);
        a++;
        if (!warmup) {
            ArrayList<Long> times = new ArrayList<>();
            for (RingNode r : tokenRing.array) {
                packagesNum += r.times.size();
                times.addAll(r.times);
            }
            filterTop90(times);
//            filterTop90(times);
            ListIterator<Long> iterator = times.listIterator();
            long sum = 0L;
            long max = 0L;
            while (iterator.hasNext()) {
                Long m = iterator.next();
                long e = m / 10000;
                iterator.set(e);
                sum += e;
                if (e > max)
                    max = e;
            }
//            StringBuilder sb=new StringBuilder();
//            for (Long l:times) {
//                sb.append(l);
//                sb.append(",");
//            }
//            String strr = sb.toString();
//            Path file = Paths.get("example.txt");
//            try {
//                Files.write(file, Collections.singleton(strr), StandardCharsets.UTF_8);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            System.out.println(times);
            System.out.println("Всего пакетов обработано: " + packagesNum);
            System.out.println("Топ медленных пакетов (5%): " + times.size());
            System.out.println("Среднее значение latency: " + sum / times.size());
            System.out.println("Максимальное значение latency:" + max);
            System.out.println("Throughput (pkg/ms): " + (double) packagesNum / elapsedTime);
            createAndShowGui(times);
        }
    }


    private static void filterTop90(ArrayList<Long> times) {
        ArrayList<Long> timesClone = (ArrayList<Long>) times.clone();
        Collections.sort(timesClone);
        long top90 = timesClone.get((int) (timesClone.size() * 0.95));
        times.removeIf(r -> r < top90);
    }
}
