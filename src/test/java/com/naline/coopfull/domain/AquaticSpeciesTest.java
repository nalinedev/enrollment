package com.naline.coopfull.domain;

import static com.naline.coopfull.domain.AquaticSpeciesTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AquaticSpeciesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AquaticSpecies.class);
        AquaticSpecies aquaticSpecies1 = getAquaticSpeciesSample1();
        AquaticSpecies aquaticSpecies2 = new AquaticSpecies();
        assertThat(aquaticSpecies1).isNotEqualTo(aquaticSpecies2);

        aquaticSpecies2.setId(aquaticSpecies1.getId());
        assertThat(aquaticSpecies1).isEqualTo(aquaticSpecies2);

        aquaticSpecies2 = getAquaticSpeciesSample2();
        assertThat(aquaticSpecies1).isNotEqualTo(aquaticSpecies2);
    }
}
