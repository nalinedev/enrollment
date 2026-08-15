package com.naline.coopfull.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class CooperativeUserTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static CooperativeUser getCooperativeUserSample1() {
        return new CooperativeUser().id(1L);
    }

    public static CooperativeUser getCooperativeUserSample2() {
        return new CooperativeUser().id(2L);
    }

    public static CooperativeUser getCooperativeUserRandomSampleGenerator() {
        return new CooperativeUser().id(longCount.incrementAndGet());
    }
}
