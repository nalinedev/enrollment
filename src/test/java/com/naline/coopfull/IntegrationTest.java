package com.naline.coopfull;

import com.naline.coopfull.config.AsyncSyncConfiguration;
import com.naline.coopfull.config.EmbeddedSQL;
import com.naline.coopfull.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
    classes = {
        CoopfullApp.class,
        JacksonConfiguration.class,
        AsyncSyncConfiguration.class,
        com.naline.coopfull.config.JacksonHibernateConfiguration.class,
    }
)
@EmbeddedSQL
public @interface IntegrationTest {}
