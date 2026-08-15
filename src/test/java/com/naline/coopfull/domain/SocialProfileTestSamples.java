package com.naline.coopfull.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SocialProfileTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static SocialProfile getSocialProfileSample1() {
        return new SocialProfile()
            .id(1L)
            .numberOfChildren(1)
            .numberOfDependents(1)
            .educationLevel("educationLevel1")
            .housingStatus("housingStatus1")
            .socialCategory("socialCategory1");
    }

    public static SocialProfile getSocialProfileSample2() {
        return new SocialProfile()
            .id(2L)
            .numberOfChildren(2)
            .numberOfDependents(2)
            .educationLevel("educationLevel2")
            .housingStatus("housingStatus2")
            .socialCategory("socialCategory2");
    }

    public static SocialProfile getSocialProfileRandomSampleGenerator() {
        return new SocialProfile()
            .id(longCount.incrementAndGet())
            .numberOfChildren(intCount.incrementAndGet())
            .numberOfDependents(intCount.incrementAndGet())
            .educationLevel(UUID.randomUUID().toString())
            .housingStatus(UUID.randomUUID().toString())
            .socialCategory(UUID.randomUUID().toString());
    }
}
