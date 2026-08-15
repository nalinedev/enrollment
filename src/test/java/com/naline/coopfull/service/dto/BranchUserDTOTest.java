package com.naline.coopfull.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BranchUserDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BranchUserDTO.class);
        BranchUserDTO branchUserDTO1 = new BranchUserDTO();
        branchUserDTO1.setId(1L);
        BranchUserDTO branchUserDTO2 = new BranchUserDTO();
        assertThat(branchUserDTO1).isNotEqualTo(branchUserDTO2);
        branchUserDTO2.setId(branchUserDTO1.getId());
        assertThat(branchUserDTO1).isEqualTo(branchUserDTO2);
        branchUserDTO2.setId(2L);
        assertThat(branchUserDTO1).isNotEqualTo(branchUserDTO2);
        branchUserDTO1.setId(null);
        assertThat(branchUserDTO1).isNotEqualTo(branchUserDTO2);
    }
}
