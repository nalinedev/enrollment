package com.naline.coopfull.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AppUserTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static AppUser getAppUserSample1() {
        return new AppUser()
            .id(1L)
            .employeeNumber("employeeNumber1")
            .firstName("firstName1")
            .lastName("lastName1")
            .phoneNumber("phoneNumber1")
            .jobTitle("jobTitle1")
            .department("department1");
    }

    public static AppUser getAppUserSample2() {
        return new AppUser()
            .id(2L)
            .employeeNumber("employeeNumber2")
            .firstName("firstName2")
            .lastName("lastName2")
            .phoneNumber("phoneNumber2")
            .jobTitle("jobTitle2")
            .department("department2");
    }

    public static AppUser getAppUserRandomSampleGenerator() {
        return new AppUser()
            .id(longCount.incrementAndGet())
            .employeeNumber(UUID.randomUUID().toString())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .phoneNumber(UUID.randomUUID().toString())
            .jobTitle(UUID.randomUUID().toString())
            .department(UUID.randomUUID().toString());
    }
}
