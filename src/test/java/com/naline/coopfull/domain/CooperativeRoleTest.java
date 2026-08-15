package com.naline.coopfull.domain;

import static com.naline.coopfull.domain.CooperativeRoleTestSamples.*;
import static com.naline.coopfull.domain.PermissionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CooperativeRoleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CooperativeRole.class);
        CooperativeRole cooperativeRole1 = getCooperativeRoleSample1();
        CooperativeRole cooperativeRole2 = new CooperativeRole();
        assertThat(cooperativeRole1).isNotEqualTo(cooperativeRole2);

        cooperativeRole2.setId(cooperativeRole1.getId());
        assertThat(cooperativeRole1).isEqualTo(cooperativeRole2);

        cooperativeRole2 = getCooperativeRoleSample2();
        assertThat(cooperativeRole1).isNotEqualTo(cooperativeRole2);
    }

    @Test
    void permissionsTest() {
        CooperativeRole cooperativeRole = getCooperativeRoleRandomSampleGenerator();
        Permission permissionBack = getPermissionRandomSampleGenerator();

        cooperativeRole.addPermissions(permissionBack);
        assertThat(cooperativeRole.getPermissionses()).containsOnly(permissionBack);

        cooperativeRole.removePermissions(permissionBack);
        assertThat(cooperativeRole.getPermissionses()).doesNotContain(permissionBack);

        cooperativeRole.permissionses(new HashSet<>(Set.of(permissionBack)));
        assertThat(cooperativeRole.getPermissionses()).containsOnly(permissionBack);

        cooperativeRole.setPermissionses(new HashSet<>());
        assertThat(cooperativeRole.getPermissionses()).doesNotContain(permissionBack);
    }
}
