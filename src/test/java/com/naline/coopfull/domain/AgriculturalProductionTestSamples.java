package com.naline.coopfull.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AgriculturalProductionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static AgriculturalProduction getAgriculturalProductionSample1() {
        return new AgriculturalProduction()
            .id(1L)
            .areaUnit("areaUnit1")
            .productionUnit("productionUnit1")
            .numberOfPlants(1)
            .productionYear(1);
    }

    public static AgriculturalProduction getAgriculturalProductionSample2() {
        return new AgriculturalProduction()
            .id(2L)
            .areaUnit("areaUnit2")
            .productionUnit("productionUnit2")
            .numberOfPlants(2)
            .productionYear(2);
    }

    public static AgriculturalProduction getAgriculturalProductionRandomSampleGenerator() {
        return new AgriculturalProduction()
            .id(longCount.incrementAndGet())
            .areaUnit(UUID.randomUUID().toString())
            .productionUnit(UUID.randomUUID().toString())
            .numberOfPlants(intCount.incrementAndGet())
            .productionYear(intCount.incrementAndGet());
    }
}
