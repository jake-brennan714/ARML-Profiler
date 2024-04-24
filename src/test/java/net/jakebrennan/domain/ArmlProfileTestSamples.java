package net.jakebrennan.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ArmlProfileTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ArmlProfile getArmlProfileSample1() {
        return new ArmlProfile().id(1L).feedEV(1L);
    }

    public static ArmlProfile getArmlProfileSample2() {
        return new ArmlProfile().id(2L).feedEV(2L);
    }

    public static ArmlProfile getArmlProfileRandomSampleGenerator() {
        return new ArmlProfile().id(longCount.incrementAndGet()).feedEV(longCount.incrementAndGet());
    }
}
