package com.naline.coopfull.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AgriculturalActivityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static AgriculturalActivity getAgriculturalActivitySample1() {
        return new AgriculturalActivity().id(1L).areaUnit("areaUnit1").certification("certification1");
    }

    public static AgriculturalActivity getAgriculturalActivitySample2() {
        return new AgriculturalActivity().id(2L).areaUnit("areaUnit2").certification("certification2");
    }

    public static AgriculturalActivity getAgriculturalActivityRandomSampleGenerator() {
        return new AgriculturalActivity()
            .id(longCount.incrementAndGet())
            .areaUnit(UUID.randomUUID().toString())
            .certification(UUID.randomUUID().toString());
    }
}
