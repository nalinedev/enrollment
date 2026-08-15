package com.naline.coopfull.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CooperativeBranchDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CooperativeBranchDTO.class);
        CooperativeBranchDTO cooperativeBranchDTO1 = new CooperativeBranchDTO();
        cooperativeBranchDTO1.setId(1L);
        CooperativeBranchDTO cooperativeBranchDTO2 = new CooperativeBranchDTO();
        assertThat(cooperativeBranchDTO1).isNotEqualTo(cooperativeBranchDTO2);
        cooperativeBranchDTO2.setId(cooperativeBranchDTO1.getId());
        assertThat(cooperativeBranchDTO1).isEqualTo(cooperativeBranchDTO2);
        cooperativeBranchDTO2.setId(2L);
        assertThat(cooperativeBranchDTO1).isNotEqualTo(cooperativeBranchDTO2);
        cooperativeBranchDTO1.setId(null);
        assertThat(cooperativeBranchDTO1).isNotEqualTo(cooperativeBranchDTO2);
    }
}
