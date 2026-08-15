package com.naline.coopfull.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AquaticSpeciesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AquaticSpeciesDTO.class);
        AquaticSpeciesDTO aquaticSpeciesDTO1 = new AquaticSpeciesDTO();
        aquaticSpeciesDTO1.setId(1L);
        AquaticSpeciesDTO aquaticSpeciesDTO2 = new AquaticSpeciesDTO();
        assertThat(aquaticSpeciesDTO1).isNotEqualTo(aquaticSpeciesDTO2);
        aquaticSpeciesDTO2.setId(aquaticSpeciesDTO1.getId());
        assertThat(aquaticSpeciesDTO1).isEqualTo(aquaticSpeciesDTO2);
        aquaticSpeciesDTO2.setId(2L);
        assertThat(aquaticSpeciesDTO1).isNotEqualTo(aquaticSpeciesDTO2);
        aquaticSpeciesDTO1.setId(null);
        assertThat(aquaticSpeciesDTO1).isNotEqualTo(aquaticSpeciesDTO2);
    }
}
