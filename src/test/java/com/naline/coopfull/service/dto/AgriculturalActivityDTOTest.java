package com.naline.coopfull.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AgriculturalActivityDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AgriculturalActivityDTO.class);
        AgriculturalActivityDTO agriculturalActivityDTO1 = new AgriculturalActivityDTO();
        agriculturalActivityDTO1.setId(1L);
        AgriculturalActivityDTO agriculturalActivityDTO2 = new AgriculturalActivityDTO();
        assertThat(agriculturalActivityDTO1).isNotEqualTo(agriculturalActivityDTO2);
        agriculturalActivityDTO2.setId(agriculturalActivityDTO1.getId());
        assertThat(agriculturalActivityDTO1).isEqualTo(agriculturalActivityDTO2);
        agriculturalActivityDTO2.setId(2L);
        assertThat(agriculturalActivityDTO1).isNotEqualTo(agriculturalActivityDTO2);
        agriculturalActivityDTO1.setId(null);
        assertThat(agriculturalActivityDTO1).isNotEqualTo(agriculturalActivityDTO2);
    }
}
