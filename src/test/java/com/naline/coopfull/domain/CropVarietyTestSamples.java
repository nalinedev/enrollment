package com.naline.coopfull.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CropVarietyTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static CropVariety getCropVarietySample1() {
        return new CropVariety()
            .id(1L)
            .code("code1")
            .name("name1")
            .origin("origin1")
            .maturityDays(1)
            .diseaseResistance("diseaseResistance1");
    }

    public static CropVariety getCropVarietySample2() {
        return new CropVariety()
            .id(2L)
            .code("code2")
            .name("name2")
            .origin("origin2")
            .maturityDays(2)
            .diseaseResistance("diseaseResistance2");
    }

    public static CropVariety getCropVarietyRandomSampleGenerator() {
        return new CropVariety()
            .id(longCount.incrementAndGet())
            .code(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .origin(UUID.randomUUID().toString())
            .maturityDays(intCount.incrementAndGet())
            .diseaseResistance(UUID.randomUUID().toString());
    }
}
