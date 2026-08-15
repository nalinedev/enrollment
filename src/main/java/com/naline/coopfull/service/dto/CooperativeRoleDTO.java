package com.naline.coopfull.service.dto;

import com.naline.coopfull.domain.enumeration.CooperativeRoleStatus;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.naline.coopfull.domain.CooperativeRole} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CooperativeRoleDTO implements Serializable {

    private Long id;

    @NotNull
    private String code;

    @NotNull
    private String name;

    @Lob
    private String description;

    @NotNull
    private CooperativeRoleStatus status;

    private Set<PermissionDTO> permissionses = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CooperativeRoleStatus getStatus() {
        return status;
    }

    public void setStatus(CooperativeRoleStatus status) {
        this.status = status;
    }

    public Set<PermissionDTO> getPermissionses() {
        return permissionses;
    }

    public void setPermissionses(Set<PermissionDTO> permissionses) {
        this.permissionses = permissionses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CooperativeRoleDTO)) {
            return false;
        }

        CooperativeRoleDTO cooperativeRoleDTO = (CooperativeRoleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cooperativeRoleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CooperativeRoleDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", status='" + getStatus() + "'" +
            ", permissionses=" + getPermissionses() +
            "}";
    }
}
