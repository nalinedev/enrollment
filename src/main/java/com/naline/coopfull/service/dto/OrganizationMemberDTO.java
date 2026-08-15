package com.naline.coopfull.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.naline.coopfull.domain.OrganizationMember} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrganizationMemberDTO implements Serializable {

    private Long id;

    @NotNull
    private String legalName;

    private String tradeName;

    private String registrationNumber;

    private String taxNumber;

    private String legalForm;

    private LocalDate registrationDate;

    private String email;

    private String phoneNumber;

    private String website;

    @Lob
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    public String getLegalForm() {
        return legalForm;
    }

    public void setLegalForm(String legalForm) {
        this.legalForm = legalForm;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrganizationMemberDTO)) {
            return false;
        }

        OrganizationMemberDTO organizationMemberDTO = (OrganizationMemberDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, organizationMemberDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrganizationMemberDTO{" +
            "id=" + getId() +
            ", legalName='" + getLegalName() + "'" +
            ", tradeName='" + getTradeName() + "'" +
            ", registrationNumber='" + getRegistrationNumber() + "'" +
            ", taxNumber='" + getTaxNumber() + "'" +
            ", legalForm='" + getLegalForm() + "'" +
            ", registrationDate='" + getRegistrationDate() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", website='" + getWebsite() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
