package com.naline.coopfull.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class LivestockProductionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static LivestockProduction getLivestockProductionSample1() {
        return new LivestockProduction()
            .id(1L)
            .numberOfAnimals(1)
            .averageAgeMonths(1)
            .productionUnit("productionUnit1")
            .mortalityCount(1)
            .birthCount(1)
            .soldCount(1);
    }

    public static LivestockProduction getLivestockProductionSample2() {
        return new LivestockProduction()
            .id(2L)
            .numberOfAnimals(2)
            .averageAgeMonths(2)
            .productionUnit("productionUnit2")
            .mortalityCount(2)
            .birthCount(2)
            .soldCount(2);
    }

    public static LivestockProduction getLivestockProductionRandomSampleGenerator() {
        return new LivestockProduction()
            .id(longCount.incrementAndGet())
            .numberOfAnimals(intCount.incrementAndGet())
            .averageAgeMonths(intCount.incrementAndGet())
            .productionUnit(UUID.randomUUID().toString())
            .mortalityCount(intCount.incrementAndGet())
            .birthCount(intCount.incrementAndGet())
            .soldCount(intCount.incrementAndGet());
    }
}
