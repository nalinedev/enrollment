package com.naline.coopfull.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CropVarietyDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CropVarietyDTO.class);
        CropVarietyDTO cropVarietyDTO1 = new CropVarietyDTO();
        cropVarietyDTO1.setId(1L);
        CropVarietyDTO cropVarietyDTO2 = new CropVarietyDTO();
        assertThat(cropVarietyDTO1).isNotEqualTo(cropVarietyDTO2);
        cropVarietyDTO2.setId(cropVarietyDTO1.getId());
        assertThat(cropVarietyDTO1).isEqualTo(cropVarietyDTO2);
        cropVarietyDTO2.setId(2L);
        assertThat(cropVarietyDTO1).isNotEqualTo(cropVarietyDTO2);
        cropVarietyDTO1.setId(null);
        assertThat(cropVarietyDTO1).isNotEqualTo(cropVarietyDTO2);
    }
}
