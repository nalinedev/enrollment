package com.naline.coopfull.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CooperativeBranchTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static CooperativeBranch getCooperativeBranchSample1() {
        return new CooperativeBranch().id(1L).code("code1").name("name1").description("description1").phone("phone1").email("email1");
    }

    public static CooperativeBranch getCooperativeBranchSample2() {
        return new CooperativeBranch().id(2L).code("code2").name("name2").description("description2").phone("phone2").email("email2");
    }

    public static CooperativeBranch getCooperativeBranchRandomSampleGenerator() {
        return new CooperativeBranch()
            .id(longCount.incrementAndGet())
            .code(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .phone(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString());
    }
}
