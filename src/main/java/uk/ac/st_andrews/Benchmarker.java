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

    @Param({"1", "2", "3", "4", "5", "6", "7", "8", "9",
            "10", "20", "30", "40", "50", "60", "70", "80", "90",
            "100", "200", "300", "400", "500", "600", "700", "800", "900",
            "1000", "2000", "3000", "4000", "5000", "6000", "7000", "8000", "9000",
            "10000", "20000", "30000", "40000", "50000", "60000", "70000", "80000", "90000"
    })
    private int inputLength;

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
                .forks(1)
                .build();
        new Runner(opt).run();
    }

}
