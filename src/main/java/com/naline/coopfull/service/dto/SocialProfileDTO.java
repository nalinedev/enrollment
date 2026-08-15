package com.naline.coopfull.service.dto;

import com.naline.coopfull.domain.enumeration.MaritalStatus;
import jakarta.persistence.Lob;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.naline.coopfull.domain.SocialProfile} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SocialProfileDTO implements Serializable {

    private Long id;

    private MaritalStatus maritalStatus;

    private Integer numberOfChildren;

    private Integer numberOfDependents;

    private String educationLevel;

    private String housingStatus;

    private LocalDate residenceSince;

    private Boolean disabilityStatus;

    @Lob
    private String disabilityDescription;

    private String socialCategory;

    @Lob
    private String notes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public Integer getNumberOfChildren() {
        return numberOfChildren;
    }

    public void setNumberOfChildren(Integer numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }

    public Integer getNumberOfDependents() {
        return numberOfDependents;
    }

    public void setNumberOfDependents(Integer numberOfDependents) {
        this.numberOfDependents = numberOfDependents;
    }

    public String getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

    public String getHousingStatus() {
        return housingStatus;
    }

    public void setHousingStatus(String housingStatus) {
        this.housingStatus = housingStatus;
    }

    public LocalDate getResidenceSince() {
        return residenceSince;
    }

    public void setResidenceSince(LocalDate residenceSince) {
        this.residenceSince = residenceSince;
    }

    public Boolean getDisabilityStatus() {
        return disabilityStatus;
    }

    public void setDisabilityStatus(Boolean disabilityStatus) {
        this.disabilityStatus = disabilityStatus;
    }

    public String getDisabilityDescription() {
        return disabilityDescription;
    }

    public void setDisabilityDescription(String disabilityDescription) {
        this.disabilityDescription = disabilityDescription;
    }

    public String getSocialCategory() {
        return socialCategory;
    }

    public void setSocialCategory(String socialCategory) {
        this.socialCategory = socialCategory;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SocialProfileDTO)) {
            return false;
        }

        SocialProfileDTO socialProfileDTO = (SocialProfileDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, socialProfileDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SocialProfileDTO{" +
            "id=" + getId() +
            ", maritalStatus='" + getMaritalStatus() + "'" +
            ", numberOfChildren=" + getNumberOfChildren() +
            ", numberOfDependents=" + getNumberOfDependents() +
            ", educationLevel='" + getEducationLevel() + "'" +
            ", housingStatus='" + getHousingStatus() + "'" +
            ", residenceSince='" + getResidenceSince() + "'" +
            ", disabilityStatus='" + getDisabilityStatus() + "'" +
            ", disabilityDescription='" + getDisabilityDescription() + "'" +
            ", socialCategory='" + getSocialCategory() + "'" +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
