package com.naline.coopfull.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class LivestockActivityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static LivestockActivity getLivestockActivitySample1() {
        return new LivestockActivity()
            .id(1L)
            .name("name1")
            .areaUnit("areaUnit1")
            .numberOfAnimals(1)
            .employees(1)
            .feedSource("feedSource1")
            .waterSource("waterSource1")
            .certification("certification1");
    }

    public static LivestockActivity getLivestockActivitySample2() {
        return new LivestockActivity()
            .id(2L)
            .name("name2")
            .areaUnit("areaUnit2")
            .numberOfAnimals(2)
            .employees(2)
            .feedSource("feedSource2")
            .waterSource("waterSource2")
            .certification("certification2");
    }

    public static LivestockActivity getLivestockActivityRandomSampleGenerator() {
        return new LivestockActivity()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .areaUnit(UUID.randomUUID().toString())
            .numberOfAnimals(intCount.incrementAndGet())
            .employees(intCount.incrementAndGet())
            .feedSource(UUID.randomUUID().toString())
            .waterSource(UUID.randomUUID().toString())
            .certification(UUID.randomUUID().toString());
    }
}
