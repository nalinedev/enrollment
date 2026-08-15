package com.naline.coopfull.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AquacultureActivityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static AquacultureActivity getAquacultureActivitySample1() {
        return new AquacultureActivity()
            .id(1L)
            .name("name1")
            .areaUnit("areaUnit1")
            .waterSource("waterSource1")
            .numberOfProductionUnits(1)
            .productionUnitDescription("productionUnitDescription1")
            .employees(1)
            .certification("certification1");
    }

    public static AquacultureActivity getAquacultureActivitySample2() {
        return new AquacultureActivity()
            .id(2L)
            .name("name2")
            .areaUnit("areaUnit2")
            .waterSource("waterSource2")
            .numberOfProductionUnits(2)
            .productionUnitDescription("productionUnitDescription2")
            .employees(2)
            .certification("certification2");
    }

    public static AquacultureActivity getAquacultureActivityRandomSampleGenerator() {
        return new AquacultureActivity()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .areaUnit(UUID.randomUUID().toString())
            .waterSource(UUID.randomUUID().toString())
            .numberOfProductionUnits(intCount.incrementAndGet())
            .productionUnitDescription(UUID.randomUUID().toString())
            .employees(intCount.incrementAndGet())
            .certification(UUID.randomUUID().toString());
    }
}
