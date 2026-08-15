package com.naline.coopfull.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MemberDocumentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MemberDocumentDTO.class);
        MemberDocumentDTO memberDocumentDTO1 = new MemberDocumentDTO();
        memberDocumentDTO1.setId(1L);
        MemberDocumentDTO memberDocumentDTO2 = new MemberDocumentDTO();
        assertThat(memberDocumentDTO1).isNotEqualTo(memberDocumentDTO2);
        memberDocumentDTO2.setId(memberDocumentDTO1.getId());
        assertThat(memberDocumentDTO1).isEqualTo(memberDocumentDTO2);
        memberDocumentDTO2.setId(2L);
        assertThat(memberDocumentDTO1).isNotEqualTo(memberDocumentDTO2);
        memberDocumentDTO1.setId(null);
        assertThat(memberDocumentDTO1).isNotEqualTo(memberDocumentDTO2);
    }
}
