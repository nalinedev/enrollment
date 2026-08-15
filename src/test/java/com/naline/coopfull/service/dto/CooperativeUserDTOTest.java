package com.naline.coopfull.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CooperativeUserDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CooperativeUserDTO.class);
        CooperativeUserDTO cooperativeUserDTO1 = new CooperativeUserDTO();
        cooperativeUserDTO1.setId(1L);
        CooperativeUserDTO cooperativeUserDTO2 = new CooperativeUserDTO();
        assertThat(cooperativeUserDTO1).isNotEqualTo(cooperativeUserDTO2);
        cooperativeUserDTO2.setId(cooperativeUserDTO1.getId());
        assertThat(cooperativeUserDTO1).isEqualTo(cooperativeUserDTO2);
        cooperativeUserDTO2.setId(2L);
        assertThat(cooperativeUserDTO1).isNotEqualTo(cooperativeUserDTO2);
        cooperativeUserDTO1.setId(null);
        assertThat(cooperativeUserDTO1).isNotEqualTo(cooperativeUserDTO2);
    }
}
