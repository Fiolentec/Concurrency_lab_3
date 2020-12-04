package company;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class BenchmarkTest {

    public static void main(String[] args) throws Exception {
        final Options options = new OptionsBuilder()
                .include(BenchmarkTest.class.getName())
                .forks(1)
                .threadGroups(1, 2, 4, 8)
                .build();

        new Runner(options).run();
    }

    final static int N = 1000;
    final static int K = 10;
    final static int ringLength = 10;
    TokenRing tokenRing;

    @Setup
    public void prepare() {
        tokenRing = new TokenRing(ringLength);
        tokenRing.start();
    }

    @Benchmark
    public void testWork() {
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
