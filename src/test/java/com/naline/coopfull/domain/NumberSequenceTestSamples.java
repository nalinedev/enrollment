package com.naline.coopfull.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class NumberSequenceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static NumberSequence getNumberSequenceSample1() {
        return new NumberSequence().id(1L).prefix("prefix1").year(1).currentValue(1L).padding(1);
    }

    public static NumberSequence getNumberSequenceSample2() {
        return new NumberSequence().id(2L).prefix("prefix2").year(2).currentValue(2L).padding(2);
    }

    public static NumberSequence getNumberSequenceRandomSampleGenerator() {
        return new NumberSequence()
            .id(longCount.incrementAndGet())
            .prefix(UUID.randomUUID().toString())
            .year(intCount.incrementAndGet())
            .currentValue(longCount.incrementAndGet())
            .padding(intCount.incrementAndGet());
    }
}
