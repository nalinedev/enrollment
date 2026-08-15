package com.naline.coopfull.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class LivestockTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static LivestockType getLivestockTypeSample1() {
        return new LivestockType().id(1L).code("code1").name("name1").scientificName("scientificName1").category("category1");
    }

    public static LivestockType getLivestockTypeSample2() {
        return new LivestockType().id(2L).code("code2").name("name2").scientificName("scientificName2").category("category2");
    }

    public static LivestockType getLivestockTypeRandomSampleGenerator() {
        return new LivestockType()
            .id(longCount.incrementAndGet())
            .code(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .scientificName(UUID.randomUUID().toString())
            .category(UUID.randomUUID().toString());
    }
}
