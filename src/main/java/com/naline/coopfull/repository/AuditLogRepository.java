package com.naline.coopfull.repository;

import com.naline.coopfull.domain.AuditLog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AuditLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long>, JpaSpecificationExecutor<AuditLog> {}
