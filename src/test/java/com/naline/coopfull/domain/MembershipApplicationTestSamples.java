package com.naline.coopfull.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MembershipApplicationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static MembershipApplication getMembershipApplicationSample1() {
        return new MembershipApplication().id(1L).applicationNumber("applicationNumber1");
    }

    public static MembershipApplication getMembershipApplicationSample2() {
        return new MembershipApplication().id(2L).applicationNumber("applicationNumber2");
    }

    public static MembershipApplication getMembershipApplicationRandomSampleGenerator() {
        return new MembershipApplication().id(longCount.incrementAndGet()).applicationNumber(UUID.randomUUID().toString());
    }
}
