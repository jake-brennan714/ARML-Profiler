package net.jakebrennan.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ArmlGameScoreTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ArmlGameScore getArmlGameScoreSample1() {
        return new ArmlGameScore().id(1L).score(1L);
    }

    public static ArmlGameScore getArmlGameScoreSample2() {
        return new ArmlGameScore().id(2L).score(2L);
    }

    public static ArmlGameScore getArmlGameScoreRandomSampleGenerator() {
        return new ArmlGameScore().id(longCount.incrementAndGet()).score(longCount.incrementAndGet());
    }
}
