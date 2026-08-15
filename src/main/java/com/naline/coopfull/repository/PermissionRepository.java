package com.naline.coopfull.repository;

import com.naline.coopfull.domain.Permission;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Permission entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long>, JpaSpecificationExecutor<Permission> {}
