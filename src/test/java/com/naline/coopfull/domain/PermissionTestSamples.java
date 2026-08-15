package com.naline.coopfull.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PermissionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Permission getPermissionSample1() {
        return new Permission().id(1L).code("code1").name("name1").resource("resource1").action("action1");
    }

    public static Permission getPermissionSample2() {
        return new Permission().id(2L).code("code2").name("name2").resource("resource2").action("action2");
    }

    public static Permission getPermissionRandomSampleGenerator() {
        return new Permission()
            .id(longCount.incrementAndGet())
            .code(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .resource(UUID.randomUUID().toString())
            .action(UUID.randomUUID().toString());
    }
}
