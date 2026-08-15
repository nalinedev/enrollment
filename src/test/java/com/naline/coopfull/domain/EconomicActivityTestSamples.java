package com.naline.coopfull.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class EconomicActivityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static EconomicActivity getEconomicActivitySample1() {
        return new EconomicActivity().id(1L).name("name1").numberOfEmployees(1);
    }

    public static EconomicActivity getEconomicActivitySample2() {
        return new EconomicActivity().id(2L).name("name2").numberOfEmployees(2);
    }

    public static EconomicActivity getEconomicActivityRandomSampleGenerator() {
        return new EconomicActivity()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .numberOfEmployees(intCount.incrementAndGet());
    }
}
