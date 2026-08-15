package com.naline.coopfull.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CropDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CropDTO.class);
        CropDTO cropDTO1 = new CropDTO();
        cropDTO1.setId(1L);
        CropDTO cropDTO2 = new CropDTO();
        assertThat(cropDTO1).isNotEqualTo(cropDTO2);
        cropDTO2.setId(cropDTO1.getId());
        assertThat(cropDTO1).isEqualTo(cropDTO2);
        cropDTO2.setId(2L);
        assertThat(cropDTO1).isNotEqualTo(cropDTO2);
        cropDTO1.setId(null);
        assertThat(cropDTO1).isNotEqualTo(cropDTO2);
    }
}
