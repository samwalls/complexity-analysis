package uk.ac.st_andrews;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import uk.ac.st_andrews.complexity.fourier.Complex;
import uk.ac.st_andrews.complexity.fourier.DFT;
import uk.ac.st_andrews.complexity.fourier.FFT;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
public class Benchmarker {

    @State(Scope.Benchmark)
    private class RandomComplexGenerator {

        private Random rand;
        private int length;

        public RandomComplexGenerator(int length) {
            this.length = length;
            rand = new Random();
        }

        private Complex nextRandomComplex() {
            // make a random complex number, with real and imaginary components between -10 and 10
            return new Complex(rand.nextDouble(), rand.nextDouble());
        }

        public Complex[] randomInputs() {
            Complex[] randomInput = new Complex[inputLength];
            for (int i = 0; i < inputLength; i++) {
                randomInput[i] = nextRandomComplex();
            }
            return randomInput;
        }
    }

    private Complex[] randomInput;

    /**
     * set of input lengths which get benchmarked for
     */
    @Param({"1", "2", "4", "8", "16", "32", "64", "128", "256", "512", "1024", "2048", "4096", "8192"})
    private int inputLength;

    /**
     * This is run before each benchmark method
     */
    @Setup
    public void setup() {
        randomInput = new RandomComplexGenerator(inputLength).randomInputs();
    }

    private Complex[] inputs() {
        return randomInput;
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void measureDft() {
        Complex[] output = DFT.dft(inputs());
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void measureFft() {
        Complex[] output = FFT.fft(inputs());
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(Benchmarker.class.getSimpleName())
                .measurementBatchSize(5)
                .measurementIterations(10)
                .warmupBatchSize(5)
                .warmupForks(3)
                .warmupIterations(5)
                .forks(3)
                .threads(Runtime.getRuntime().availableProcessors()) // use as many threads as possible
                .build();
        new Runner(opt).run();
    }

}
