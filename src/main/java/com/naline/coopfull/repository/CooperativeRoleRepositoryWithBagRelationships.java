package com.naline.coopfull.repository;

import com.naline.coopfull.domain.CooperativeRole;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface CooperativeRoleRepositoryWithBagRelationships {
    Optional<CooperativeRole> fetchBagRelationships(Optional<CooperativeRole> cooperativeRole);

    List<CooperativeRole> fetchBagRelationships(List<CooperativeRole> cooperativeRoles);

    Page<CooperativeRole> fetchBagRelationships(Page<CooperativeRole> cooperativeRoles);
}
