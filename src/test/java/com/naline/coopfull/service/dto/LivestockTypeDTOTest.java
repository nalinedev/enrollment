package com.naline.coopfull.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LivestockTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LivestockTypeDTO.class);
        LivestockTypeDTO livestockTypeDTO1 = new LivestockTypeDTO();
        livestockTypeDTO1.setId(1L);
        LivestockTypeDTO livestockTypeDTO2 = new LivestockTypeDTO();
        assertThat(livestockTypeDTO1).isNotEqualTo(livestockTypeDTO2);
        livestockTypeDTO2.setId(livestockTypeDTO1.getId());
        assertThat(livestockTypeDTO1).isEqualTo(livestockTypeDTO2);
        livestockTypeDTO2.setId(2L);
        assertThat(livestockTypeDTO1).isNotEqualTo(livestockTypeDTO2);
        livestockTypeDTO1.setId(null);
        assertThat(livestockTypeDTO1).isNotEqualTo(livestockTypeDTO2);
    }
}
