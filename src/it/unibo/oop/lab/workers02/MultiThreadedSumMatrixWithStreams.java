package it.unibo.oop.lab.workers02;

import java.util.stream.IntStream;

/**
 * This is a implementation of the sum using streams.
 */
public class MultiThreadedSumMatrixWithStreams implements SumMatrix {

    private final int nthread;

    /**
     * Construct a multithreaded matrix sum.
     * 
     * @param nthread
     *            number of threads.
     */
    public MultiThreadedSumMatrixWithStreams(final int nthread) {
        super();
        if (nthread < 1) {
            throw new IllegalArgumentException();
        }
        this.nthread = nthread;
    }

    @Override
    public double sum(final double[][] matrix) {
        final int size = matrix.length / nthread + matrix.length % nthread;
        return IntStream
                .iterate(0, start -> start + size)
                .limit(nthread)
                .parallel()
                .mapToDouble(start -> {
                    double res = 0;
                    System.out.println("Working from position " + start + " to position " + (start + size - 1));
                    for (int i = start; i < matrix.length && i < start + size; i++) {
                        for (final double d : matrix[i]) {
                            res += d;
                        }
                    }
                    return res;
                })
                .sum();
    }
}
