package com.naline.coopfull.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProfessionalProfileDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfessionalProfileDTO.class);
        ProfessionalProfileDTO professionalProfileDTO1 = new ProfessionalProfileDTO();
        professionalProfileDTO1.setId(1L);
        ProfessionalProfileDTO professionalProfileDTO2 = new ProfessionalProfileDTO();
        assertThat(professionalProfileDTO1).isNotEqualTo(professionalProfileDTO2);
        professionalProfileDTO2.setId(professionalProfileDTO1.getId());
        assertThat(professionalProfileDTO1).isEqualTo(professionalProfileDTO2);
        professionalProfileDTO2.setId(2L);
        assertThat(professionalProfileDTO1).isNotEqualTo(professionalProfileDTO2);
        professionalProfileDTO1.setId(null);
        assertThat(professionalProfileDTO1).isNotEqualTo(professionalProfileDTO2);
    }
}
