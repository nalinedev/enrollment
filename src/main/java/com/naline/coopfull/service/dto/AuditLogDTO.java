package com.naline.coopfull.service.dto;

import com.naline.coopfull.domain.enumeration.AuditAction;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.naline.coopfull.domain.AuditLog} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AuditLogDTO implements Serializable {

    private Long id;

    @NotNull
    private AuditAction action;

    @NotNull
    private String entityName;

    private String entityId;

    private String username;

    private Long cooperativeId;

    private Long branchId;

    @NotNull
    private Instant timestamp;

    private String ipAddress;

    private String userAgent;

    @Lob
    private String oldValue;

    @Lob
    private String newValue;

    @Lob
    private String description;

    private AppUserDTO appUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AuditAction getAction() {
        return action;
    }

    public void setAction(AuditAction action) {
        this.action = action;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getCooperativeId() {
        return cooperativeId;
    }

    public void setCooperativeId(Long cooperativeId) {
        this.cooperativeId = cooperativeId;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AppUserDTO getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUserDTO appUser) {
        this.appUser = appUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AuditLogDTO)) {
            return false;
        }

        AuditLogDTO auditLogDTO = (AuditLogDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, auditLogDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AuditLogDTO{" +
            "id=" + getId() +
            ", action='" + getAction() + "'" +
            ", entityName='" + getEntityName() + "'" +
            ", entityId='" + getEntityId() + "'" +
            ", username='" + getUsername() + "'" +
            ", cooperativeId=" + getCooperativeId() +
            ", branchId=" + getBranchId() +
            ", timestamp='" + getTimestamp() + "'" +
            ", ipAddress='" + getIpAddress() + "'" +
            ", userAgent='" + getUserAgent() + "'" +
            ", oldValue='" + getOldValue() + "'" +
            ", newValue='" + getNewValue() + "'" +
            ", description='" + getDescription() + "'" +
            ", appUser=" + getAppUser() +
            "}";
    }
}
