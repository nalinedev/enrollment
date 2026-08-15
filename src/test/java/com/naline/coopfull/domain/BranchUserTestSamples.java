package com.naline.coopfull.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class BranchUserTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static BranchUser getBranchUserSample1() {
        return new BranchUser().id(1L);
    }

    public static BranchUser getBranchUserSample2() {
        return new BranchUser().id(2L);
    }

    public static BranchUser getBranchUserRandomSampleGenerator() {
        return new BranchUser().id(longCount.incrementAndGet());
    }
}
