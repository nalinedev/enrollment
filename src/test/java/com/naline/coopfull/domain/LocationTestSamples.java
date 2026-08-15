package com.naline.coopfull.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class LocationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Location getLocationSample1() {
        return new Location()
            .id(1L)
            .code("code1")
            .name("name1")
            .addressLine1("addressLine11")
            .addressLine2("addressLine21")
            .postalCode("postalCode1")
            .description("description1");
    }

    public static Location getLocationSample2() {
        return new Location()
            .id(2L)
            .code("code2")
            .name("name2")
            .addressLine1("addressLine12")
            .addressLine2("addressLine22")
            .postalCode("postalCode2")
            .description("description2");
    }

    public static Location getLocationRandomSampleGenerator() {
        return new Location()
            .id(longCount.incrementAndGet())
            .code(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .addressLine1(UUID.randomUUID().toString())
            .addressLine2(UUID.randomUUID().toString())
            .postalCode(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
