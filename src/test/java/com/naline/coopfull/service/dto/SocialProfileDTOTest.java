package com.naline.coopfull.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SocialProfileDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SocialProfileDTO.class);
        SocialProfileDTO socialProfileDTO1 = new SocialProfileDTO();
        socialProfileDTO1.setId(1L);
        SocialProfileDTO socialProfileDTO2 = new SocialProfileDTO();
        assertThat(socialProfileDTO1).isNotEqualTo(socialProfileDTO2);
        socialProfileDTO2.setId(socialProfileDTO1.getId());
        assertThat(socialProfileDTO1).isEqualTo(socialProfileDTO2);
        socialProfileDTO2.setId(2L);
        assertThat(socialProfileDTO1).isNotEqualTo(socialProfileDTO2);
        socialProfileDTO1.setId(null);
        assertThat(socialProfileDTO1).isNotEqualTo(socialProfileDTO2);
    }
}
