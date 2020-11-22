package company;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class BenchmarkTest {
    final static int N = 1000;
    final static int K = 10;
    final static int ringLength = 10;
    TokenRing tokenRing;
    @Setup
    public void prepare(){
        tokenRing = new TokenRing(ringLength);
        tokenRing.start();
    }

    @GenerateMicroBenchmark
    public void testWork(){
        for (int i = 0; i < N; ++i) {
            tokenRing.computePackage(K);
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tokenRing.stopAll();
    }
}
