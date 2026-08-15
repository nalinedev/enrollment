package com.naline.coopfull.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AquacultureProductionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AquacultureProductionDTO.class);
        AquacultureProductionDTO aquacultureProductionDTO1 = new AquacultureProductionDTO();
        aquacultureProductionDTO1.setId(1L);
        AquacultureProductionDTO aquacultureProductionDTO2 = new AquacultureProductionDTO();
        assertThat(aquacultureProductionDTO1).isNotEqualTo(aquacultureProductionDTO2);
        aquacultureProductionDTO2.setId(aquacultureProductionDTO1.getId());
        assertThat(aquacultureProductionDTO1).isEqualTo(aquacultureProductionDTO2);
        aquacultureProductionDTO2.setId(2L);
        assertThat(aquacultureProductionDTO1).isNotEqualTo(aquacultureProductionDTO2);
        aquacultureProductionDTO1.setId(null);
        assertThat(aquacultureProductionDTO1).isNotEqualTo(aquacultureProductionDTO2);
    }
}
