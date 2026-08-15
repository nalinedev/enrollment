package com.naline.coopfull.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.naline.coopfull.domain.Permission} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PermissionDTO implements Serializable {

    private Long id;

    @NotNull
    private String code;

    @NotNull
    private String name;

    @Lob
    private String description;

    private String resource;

    private String action;

    @NotNull
    private Boolean active;

    private Set<CooperativeRoleDTO> roleses = new HashSet<>();

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

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<CooperativeRoleDTO> getRoleses() {
        return roleses;
    }

    public void setRoleses(Set<CooperativeRoleDTO> roleses) {
        this.roleses = roleses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PermissionDTO)) {
            return false;
        }

        PermissionDTO permissionDTO = (PermissionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, permissionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PermissionDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", resource='" + getResource() + "'" +
            ", action='" + getAction() + "'" +
            ", active='" + getActive() + "'" +
            ", roleses=" + getRoleses() +
            "}";
    }
}
