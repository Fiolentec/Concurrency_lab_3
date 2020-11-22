package company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;

import static company.DrawGraph.createAndShowGui;



public class Main {
    final static int N = 1000;
    final static int K = 10;
    final static int ringLength = 10;

    public static void main(String[] args) {
        TokenRing tokenRing = new TokenRing(ringLength);
        tokenRing.start();
        for (int i = 0; i < N; ++i) {
            tokenRing.computePackage(K);
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tokenRing.stopAll();
        int packagesNum = 0;
        ArrayList<Long> times = new ArrayList<>();
        for (RingNode r:tokenRing.array) {
            packagesNum+=r.times.size();
            times.addAll(r.times);
        }
        filterTop90(times);
        ListIterator<Long> iterator = times.listIterator();
        Long sum = 0L;
        Long max = 0L;
        while (iterator.hasNext()){
            Long m = iterator.next();
            long e = m / 10000;
            iterator.set(e);
            sum+= e;
            if (e > max)
                max = e;
        }
        System.out.println("Всего пакетов обработано: "+packagesNum);
        System.out.println("Топ медленных пакетов (5%): " + times.size());
        System.out.println("Среднее значение latency: " + sum/times.size());
        System.out.println("Максимальное значение latency:" + max);
        createAndShowGui(times);
    }


    private static void filterTop90(ArrayList<Long> times) {
        ArrayList<Long> timesClone = (ArrayList<Long>) times.clone();
        Collections.sort(timesClone);
        long top90 = timesClone.get((int) (timesClone.size()*0.95));
        times.removeIf(r->r< top90);
    }
}
