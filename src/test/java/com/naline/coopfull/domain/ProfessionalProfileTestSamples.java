package com.naline.coopfull.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProfessionalProfileTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static ProfessionalProfile getProfessionalProfileSample1() {
        return new ProfessionalProfile()
            .id(1L)
            .employmentStatus("employmentStatus1")
            .employerName("employerName1")
            .jobTitle("jobTitle1")
            .profession("profession1")
            .sector("sector1")
            .yearsOfExperience(1)
            .employerLocation("employerLocation1");
    }

    public static ProfessionalProfile getProfessionalProfileSample2() {
        return new ProfessionalProfile()
            .id(2L)
            .employmentStatus("employmentStatus2")
            .employerName("employerName2")
            .jobTitle("jobTitle2")
            .profession("profession2")
            .sector("sector2")
            .yearsOfExperience(2)
            .employerLocation("employerLocation2");
    }

    public static ProfessionalProfile getProfessionalProfileRandomSampleGenerator() {
        return new ProfessionalProfile()
            .id(longCount.incrementAndGet())
            .employmentStatus(UUID.randomUUID().toString())
            .employerName(UUID.randomUUID().toString())
            .jobTitle(UUID.randomUUID().toString())
            .profession(UUID.randomUUID().toString())
            .sector(UUID.randomUUID().toString())
            .yearsOfExperience(intCount.incrementAndGet())
            .employerLocation(UUID.randomUUID().toString());
    }
}
