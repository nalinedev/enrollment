package com.naline.coopfull.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LivestockActivityDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LivestockActivityDTO.class);
        LivestockActivityDTO livestockActivityDTO1 = new LivestockActivityDTO();
        livestockActivityDTO1.setId(1L);
        LivestockActivityDTO livestockActivityDTO2 = new LivestockActivityDTO();
        assertThat(livestockActivityDTO1).isNotEqualTo(livestockActivityDTO2);
        livestockActivityDTO2.setId(livestockActivityDTO1.getId());
        assertThat(livestockActivityDTO1).isEqualTo(livestockActivityDTO2);
        livestockActivityDTO2.setId(2L);
        assertThat(livestockActivityDTO1).isNotEqualTo(livestockActivityDTO2);
        livestockActivityDTO1.setId(null);
        assertThat(livestockActivityDTO1).isNotEqualTo(livestockActivityDTO2);
    }
}
