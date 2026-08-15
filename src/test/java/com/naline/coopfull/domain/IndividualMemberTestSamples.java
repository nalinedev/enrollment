package com.naline.coopfull.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class IndividualMemberTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static IndividualMember getIndividualMemberSample1() {
        return new IndividualMember()
            .id(1L)
            .firstName("firstName1")
            .middleName("middleName1")
            .lastName("lastName1")
            .maidenName("maidenName1")
            .gender("gender1")
            .birthPlace("birthPlace1")
            .nationality("nationality1")
            .email("email1")
            .phoneNumber("phoneNumber1")
            .occupation("occupation1");
    }

    public static IndividualMember getIndividualMemberSample2() {
        return new IndividualMember()
            .id(2L)
            .firstName("firstName2")
            .middleName("middleName2")
            .lastName("lastName2")
            .maidenName("maidenName2")
            .gender("gender2")
            .birthPlace("birthPlace2")
            .nationality("nationality2")
            .email("email2")
            .phoneNumber("phoneNumber2")
            .occupation("occupation2");
    }

    public static IndividualMember getIndividualMemberRandomSampleGenerator() {
        return new IndividualMember()
            .id(longCount.incrementAndGet())
            .firstName(UUID.randomUUID().toString())
            .middleName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .maidenName(UUID.randomUUID().toString())
            .gender(UUID.randomUUID().toString())
            .birthPlace(UUID.randomUUID().toString())
            .nationality(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .phoneNumber(UUID.randomUUID().toString())
            .occupation(UUID.randomUUID().toString());
    }
}
