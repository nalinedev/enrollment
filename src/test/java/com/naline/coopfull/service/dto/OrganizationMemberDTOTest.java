package com.naline.coopfull.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrganizationMemberDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganizationMemberDTO.class);
        OrganizationMemberDTO organizationMemberDTO1 = new OrganizationMemberDTO();
        organizationMemberDTO1.setId(1L);
        OrganizationMemberDTO organizationMemberDTO2 = new OrganizationMemberDTO();
        assertThat(organizationMemberDTO1).isNotEqualTo(organizationMemberDTO2);
        organizationMemberDTO2.setId(organizationMemberDTO1.getId());
        assertThat(organizationMemberDTO1).isEqualTo(organizationMemberDTO2);
        organizationMemberDTO2.setId(2L);
        assertThat(organizationMemberDTO1).isNotEqualTo(organizationMemberDTO2);
        organizationMemberDTO1.setId(null);
        assertThat(organizationMemberDTO1).isNotEqualTo(organizationMemberDTO2);
    }
}
