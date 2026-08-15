package com.naline.coopfull.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EconomicActivityTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static EconomicActivityType getEconomicActivityTypeSample1() {
        return new EconomicActivityType().id(1L).code("code1").name("name1").sector("sector1");
    }

    public static EconomicActivityType getEconomicActivityTypeSample2() {
        return new EconomicActivityType().id(2L).code("code2").name("name2").sector("sector2");
    }

    public static EconomicActivityType getEconomicActivityTypeRandomSampleGenerator() {
        return new EconomicActivityType()
            .id(longCount.incrementAndGet())
            .code(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .sector(UUID.randomUUID().toString());
    }
}
