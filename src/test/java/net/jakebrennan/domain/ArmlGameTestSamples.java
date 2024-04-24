package net.jakebrennan.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ArmlGameTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ArmlGame getArmlGameSample1() {
        return new ArmlGame().id(1L).gameID(1L);
    }

    public static ArmlGame getArmlGameSample2() {
        return new ArmlGame().id(2L).gameID(2L);
    }

    public static ArmlGame getArmlGameRandomSampleGenerator() {
        return new ArmlGame().id(longCount.incrementAndGet()).gameID(longCount.incrementAndGet());
    }
}
