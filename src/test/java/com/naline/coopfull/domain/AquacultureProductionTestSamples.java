package com.naline.coopfull.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AquacultureProductionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static AquacultureProduction getAquacultureProductionSample1() {
        return new AquacultureProduction()
            .id(1L)
            .numberOfAnimals(1)
            .productionUnit("productionUnit1")
            .mortalityCount(1)
            .stockingCount(1)
            .harvestedCount(1);
    }

    public static AquacultureProduction getAquacultureProductionSample2() {
        return new AquacultureProduction()
            .id(2L)
            .numberOfAnimals(2)
            .productionUnit("productionUnit2")
            .mortalityCount(2)
            .stockingCount(2)
            .harvestedCount(2);
    }

    public static AquacultureProduction getAquacultureProductionRandomSampleGenerator() {
        return new AquacultureProduction()
            .id(longCount.incrementAndGet())
            .numberOfAnimals(intCount.incrementAndGet())
            .productionUnit(UUID.randomUUID().toString())
            .mortalityCount(intCount.incrementAndGet())
            .stockingCount(intCount.incrementAndGet())
            .harvestedCount(intCount.incrementAndGet());
    }
}
