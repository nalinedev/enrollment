package com.naline.coopfull.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class OrganizationMemberTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static OrganizationMember getOrganizationMemberSample1() {
        return new OrganizationMember()
            .id(1L)
            .legalName("legalName1")
            .tradeName("tradeName1")
            .registrationNumber("registrationNumber1")
            .taxNumber("taxNumber1")
            .legalForm("legalForm1")
            .email("email1")
            .phoneNumber("phoneNumber1")
            .website("website1");
    }

    public static OrganizationMember getOrganizationMemberSample2() {
        return new OrganizationMember()
            .id(2L)
            .legalName("legalName2")
            .tradeName("tradeName2")
            .registrationNumber("registrationNumber2")
            .taxNumber("taxNumber2")
            .legalForm("legalForm2")
            .email("email2")
            .phoneNumber("phoneNumber2")
            .website("website2");
    }

    public static OrganizationMember getOrganizationMemberRandomSampleGenerator() {
        return new OrganizationMember()
            .id(longCount.incrementAndGet())
            .legalName(UUID.randomUUID().toString())
            .tradeName(UUID.randomUUID().toString())
            .registrationNumber(UUID.randomUUID().toString())
            .taxNumber(UUID.randomUUID().toString())
            .legalForm(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .phoneNumber(UUID.randomUUID().toString())
            .website(UUID.randomUUID().toString());
    }
}
