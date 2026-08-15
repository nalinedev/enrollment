package com.naline.coopfull.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class IdentityDocumentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static IdentityDocument getIdentityDocumentSample1() {
        return new IdentityDocument()
            .id(1L)
            .documentType("documentType1")
            .documentNumber("documentNumber1")
            .issuingAuthority("issuingAuthority1")
            .issuingCountry("issuingCountry1")
            .verificationComment("verificationComment1");
    }

    public static IdentityDocument getIdentityDocumentSample2() {
        return new IdentityDocument()
            .id(2L)
            .documentType("documentType2")
            .documentNumber("documentNumber2")
            .issuingAuthority("issuingAuthority2")
            .issuingCountry("issuingCountry2")
            .verificationComment("verificationComment2");
    }

    public static IdentityDocument getIdentityDocumentRandomSampleGenerator() {
        return new IdentityDocument()
            .id(longCount.incrementAndGet())
            .documentType(UUID.randomUUID().toString())
            .documentNumber(UUID.randomUUID().toString())
            .issuingAuthority(UUID.randomUUID().toString())
            .issuingCountry(UUID.randomUUID().toString())
            .verificationComment(UUID.randomUUID().toString());
    }
}
