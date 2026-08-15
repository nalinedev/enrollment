package com.naline.coopfull.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MemberDocumentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static MemberDocument getMemberDocumentSample1() {
        return new MemberDocument()
            .id(1L)
            .originalFileName("originalFileName1")
            .storedFileName("storedFileName1")
            .contentType("contentType1")
            .fileSize(1L)
            .storagePath("storagePath1")
            .checksum("checksum1");
    }

    public static MemberDocument getMemberDocumentSample2() {
        return new MemberDocument()
            .id(2L)
            .originalFileName("originalFileName2")
            .storedFileName("storedFileName2")
            .contentType("contentType2")
            .fileSize(2L)
            .storagePath("storagePath2")
            .checksum("checksum2");
    }

    public static MemberDocument getMemberDocumentRandomSampleGenerator() {
        return new MemberDocument()
            .id(longCount.incrementAndGet())
            .originalFileName(UUID.randomUUID().toString())
            .storedFileName(UUID.randomUUID().toString())
            .contentType(UUID.randomUUID().toString())
            .fileSize(longCount.incrementAndGet())
            .storagePath(UUID.randomUUID().toString())
            .checksum(UUID.randomUUID().toString());
    }
}
