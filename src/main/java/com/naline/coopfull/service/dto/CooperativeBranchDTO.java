package com.naline.coopfull.service.dto;

import com.naline.coopfull.domain.enumeration.CooperativeStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.naline.coopfull.domain.CooperativeBranch} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CooperativeBranchDTO implements Serializable {

    private Long id;

    @NotNull
    private String code;

    @NotNull
    private String name;

    private String description;

    private String phone;

    private String email;

    @NotNull
    private CooperativeStatus status;

    private LocalDate openingDate;

    private LocalDate closingDate;

    private CooperativeDTO cooperative;

    private LocationDTO location;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CooperativeStatus getStatus() {
        return status;
    }

    public void setStatus(CooperativeStatus status) {
        this.status = status;
    }

    public LocalDate getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(LocalDate openingDate) {
        this.openingDate = openingDate;
    }

    public LocalDate getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(LocalDate closingDate) {
        this.closingDate = closingDate;
    }

    public CooperativeDTO getCooperative() {
        return cooperative;
    }

    public void setCooperative(CooperativeDTO cooperative) {
        this.cooperative = cooperative;
    }

    public LocationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationDTO location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CooperativeBranchDTO)) {
            return false;
        }

        CooperativeBranchDTO cooperativeBranchDTO = (CooperativeBranchDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cooperativeBranchDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CooperativeBranchDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", phone='" + getPhone() + "'" +
            ", email='" + getEmail() + "'" +
            ", status='" + getStatus() + "'" +
            ", openingDate='" + getOpeningDate() + "'" +
            ", closingDate='" + getClosingDate() + "'" +
            ", cooperative=" + getCooperative() +
            ", location=" + getLocation() +
            "}";
    }
}
