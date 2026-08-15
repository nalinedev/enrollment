package com.naline.coopfull.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MembershipApplicationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MembershipApplicationDTO.class);
        MembershipApplicationDTO membershipApplicationDTO1 = new MembershipApplicationDTO();
        membershipApplicationDTO1.setId(1L);
        MembershipApplicationDTO membershipApplicationDTO2 = new MembershipApplicationDTO();
        assertThat(membershipApplicationDTO1).isNotEqualTo(membershipApplicationDTO2);
        membershipApplicationDTO2.setId(membershipApplicationDTO1.getId());
        assertThat(membershipApplicationDTO1).isEqualTo(membershipApplicationDTO2);
        membershipApplicationDTO2.setId(2L);
        assertThat(membershipApplicationDTO1).isNotEqualTo(membershipApplicationDTO2);
        membershipApplicationDTO1.setId(null);
        assertThat(membershipApplicationDTO1).isNotEqualTo(membershipApplicationDTO2);
    }
}
