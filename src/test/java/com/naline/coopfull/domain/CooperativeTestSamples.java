package com.naline.coopfull.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CooperativeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Cooperative getCooperativeSample1() {
        return new Cooperative()
            .id(1L)
            .code("code1")
            .name("name1")
            .legalName("legalName1")
            .registrationNumber("registrationNumber1")
            .taxNumber("taxNumber1")
            .email("email1")
            .phone("phone1")
            .website("website1");
    }

    public static Cooperative getCooperativeSample2() {
        return new Cooperative()
            .id(2L)
            .code("code2")
            .name("name2")
            .legalName("legalName2")
            .registrationNumber("registrationNumber2")
            .taxNumber("taxNumber2")
            .email("email2")
            .phone("phone2")
            .website("website2");
    }

    public static Cooperative getCooperativeRandomSampleGenerator() {
        return new Cooperative()
            .id(longCount.incrementAndGet())
            .code(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .legalName(UUID.randomUUID().toString())
            .registrationNumber(UUID.randomUUID().toString())
            .taxNumber(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .phone(UUID.randomUUID().toString())
            .website(UUID.randomUUID().toString());
    }
}
