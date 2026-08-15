package com.naline.coopfull.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MemberTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Member getMemberSample1() {
        return new Member().id(1L).memberNumber("memberNumber1").exitReason("exitReason1");
    }

    public static Member getMemberSample2() {
        return new Member().id(2L).memberNumber("memberNumber2").exitReason("exitReason2");
    }

    public static Member getMemberRandomSampleGenerator() {
        return new Member()
            .id(longCount.incrementAndGet())
            .memberNumber(UUID.randomUUID().toString())
            .exitReason(UUID.randomUUID().toString());
    }
}
