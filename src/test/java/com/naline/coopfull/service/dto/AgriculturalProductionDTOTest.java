package com.naline.coopfull.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AgriculturalProductionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AgriculturalProductionDTO.class);
        AgriculturalProductionDTO agriculturalProductionDTO1 = new AgriculturalProductionDTO();
        agriculturalProductionDTO1.setId(1L);
        AgriculturalProductionDTO agriculturalProductionDTO2 = new AgriculturalProductionDTO();
        assertThat(agriculturalProductionDTO1).isNotEqualTo(agriculturalProductionDTO2);
        agriculturalProductionDTO2.setId(agriculturalProductionDTO1.getId());
        assertThat(agriculturalProductionDTO1).isEqualTo(agriculturalProductionDTO2);
        agriculturalProductionDTO2.setId(2L);
        assertThat(agriculturalProductionDTO1).isNotEqualTo(agriculturalProductionDTO2);
        agriculturalProductionDTO1.setId(null);
        assertThat(agriculturalProductionDTO1).isNotEqualTo(agriculturalProductionDTO2);
    }
}
