package com.naline.coopfull.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LivestockProductionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LivestockProductionDTO.class);
        LivestockProductionDTO livestockProductionDTO1 = new LivestockProductionDTO();
        livestockProductionDTO1.setId(1L);
        LivestockProductionDTO livestockProductionDTO2 = new LivestockProductionDTO();
        assertThat(livestockProductionDTO1).isNotEqualTo(livestockProductionDTO2);
        livestockProductionDTO2.setId(livestockProductionDTO1.getId());
        assertThat(livestockProductionDTO1).isEqualTo(livestockProductionDTO2);
        livestockProductionDTO2.setId(2L);
        assertThat(livestockProductionDTO1).isNotEqualTo(livestockProductionDTO2);
        livestockProductionDTO1.setId(null);
        assertThat(livestockProductionDTO1).isNotEqualTo(livestockProductionDTO2);
    }
}
