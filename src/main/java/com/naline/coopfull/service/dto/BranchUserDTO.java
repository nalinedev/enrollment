package com.naline.coopfull.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.naline.coopfull.domain.BranchUser} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BranchUserDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull
    private Boolean active;

    private AppUserDTO appUser;

    private CooperativeBranchDTO branch;

    private CooperativeRoleDTO role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public AppUserDTO getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUserDTO appUser) {
        this.appUser = appUser;
    }

    public CooperativeBranchDTO getBranch() {
        return branch;
    }

    public void setBranch(CooperativeBranchDTO branch) {
        this.branch = branch;
    }

    public CooperativeRoleDTO getRole() {
        return role;
    }

    public void setRole(CooperativeRoleDTO role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BranchUserDTO)) {
            return false;
        }

        BranchUserDTO branchUserDTO = (BranchUserDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, branchUserDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BranchUserDTO{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", active='" + getActive() + "'" +
            ", appUser=" + getAppUser() +
            ", branch=" + getBranch() +
            ", role=" + getRole() +
            "}";
    }
}
