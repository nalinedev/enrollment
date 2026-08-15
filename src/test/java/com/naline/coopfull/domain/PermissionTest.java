package com.naline.coopfull.domain;

import static com.naline.coopfull.domain.CooperativeRoleTestSamples.*;
import static com.naline.coopfull.domain.PermissionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PermissionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Permission.class);
        Permission permission1 = getPermissionSample1();
        Permission permission2 = new Permission();
        assertThat(permission1).isNotEqualTo(permission2);

        permission2.setId(permission1.getId());
        assertThat(permission1).isEqualTo(permission2);

        permission2 = getPermissionSample2();
        assertThat(permission1).isNotEqualTo(permission2);
    }

    @Test
    void rolesTest() {
        Permission permission = getPermissionRandomSampleGenerator();
        CooperativeRole cooperativeRoleBack = getCooperativeRoleRandomSampleGenerator();

        permission.addRoles(cooperativeRoleBack);
        assertThat(permission.getRoleses()).containsOnly(cooperativeRoleBack);
        assertThat(cooperativeRoleBack.getPermissionses()).containsOnly(permission);

        permission.removeRoles(cooperativeRoleBack);
        assertThat(permission.getRoleses()).doesNotContain(cooperativeRoleBack);
        assertThat(cooperativeRoleBack.getPermissionses()).doesNotContain(permission);

        permission.roleses(new HashSet<>(Set.of(cooperativeRoleBack)));
        assertThat(permission.getRoleses()).containsOnly(cooperativeRoleBack);
        assertThat(cooperativeRoleBack.getPermissionses()).containsOnly(permission);

        permission.setRoleses(new HashSet<>());
        assertThat(permission.getRoleses()).doesNotContain(cooperativeRoleBack);
        assertThat(cooperativeRoleBack.getPermissionses()).doesNotContain(permission);
    }
}
