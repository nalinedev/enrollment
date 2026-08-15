package com.naline.coopfull.domain;

import static com.naline.coopfull.domain.LocationTestSamples.*;
import static com.naline.coopfull.domain.LocationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LocationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Location.class);
        Location location1 = getLocationSample1();
        Location location2 = new Location();
        assertThat(location1).isNotEqualTo(location2);

        location2.setId(location1.getId());
        assertThat(location1).isEqualTo(location2);

        location2 = getLocationSample2();
        assertThat(location1).isNotEqualTo(location2);
    }

    @Test
    void parentTest() {
        Location location = getLocationRandomSampleGenerator();
        Location locationBack = getLocationRandomSampleGenerator();

        location.setParent(locationBack);
        assertThat(location.getParent()).isEqualTo(locationBack);

        location.parent(null);
        assertThat(location.getParent()).isNull();
    }
}
