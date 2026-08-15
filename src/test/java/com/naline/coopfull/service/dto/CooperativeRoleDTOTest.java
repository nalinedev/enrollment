package com.naline.coopfull.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CooperativeRoleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CooperativeRoleDTO.class);
        CooperativeRoleDTO cooperativeRoleDTO1 = new CooperativeRoleDTO();
        cooperativeRoleDTO1.setId(1L);
        CooperativeRoleDTO cooperativeRoleDTO2 = new CooperativeRoleDTO();
        assertThat(cooperativeRoleDTO1).isNotEqualTo(cooperativeRoleDTO2);
        cooperativeRoleDTO2.setId(cooperativeRoleDTO1.getId());
        assertThat(cooperativeRoleDTO1).isEqualTo(cooperativeRoleDTO2);
        cooperativeRoleDTO2.setId(2L);
        assertThat(cooperativeRoleDTO1).isNotEqualTo(cooperativeRoleDTO2);
        cooperativeRoleDTO1.setId(null);
        assertThat(cooperativeRoleDTO1).isNotEqualTo(cooperativeRoleDTO2);
    }
}
