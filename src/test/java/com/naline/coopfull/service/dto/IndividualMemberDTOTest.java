package com.naline.coopfull.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IndividualMemberDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IndividualMemberDTO.class);
        IndividualMemberDTO individualMemberDTO1 = new IndividualMemberDTO();
        individualMemberDTO1.setId(1L);
        IndividualMemberDTO individualMemberDTO2 = new IndividualMemberDTO();
        assertThat(individualMemberDTO1).isNotEqualTo(individualMemberDTO2);
        individualMemberDTO2.setId(individualMemberDTO1.getId());
        assertThat(individualMemberDTO1).isEqualTo(individualMemberDTO2);
        individualMemberDTO2.setId(2L);
        assertThat(individualMemberDTO1).isNotEqualTo(individualMemberDTO2);
        individualMemberDTO1.setId(null);
        assertThat(individualMemberDTO1).isNotEqualTo(individualMemberDTO2);
    }
}
