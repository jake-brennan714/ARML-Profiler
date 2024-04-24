package net.jakebrennan.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ArmlPlayerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ArmlPlayer getArmlPlayerSample1() {
        return new ArmlPlayer().id(1L).playerID(1L).firstName("firstName1").lastName("lastName1").tenhouName("tenhouName1");
    }

    public static ArmlPlayer getArmlPlayerSample2() {
        return new ArmlPlayer().id(2L).playerID(2L).firstName("firstName2").lastName("lastName2").tenhouName("tenhouName2");
    }

    public static ArmlPlayer getArmlPlayerRandomSampleGenerator() {
        return new ArmlPlayer()
            .id(longCount.incrementAndGet())
            .playerID(longCount.incrementAndGet())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .tenhouName(UUID.randomUUID().toString());
    }
}
