package com.naline.coopfull.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.naline.coopfull.domain.enumeration.MaritalStatus;
import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SocialProfile.
 */
@Entity
@Table(name = "social_profile")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SocialProfile implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "marital_status")
    private MaritalStatus maritalStatus;

    @Column(name = "number_of_children")
    private Integer numberOfChildren;

    @Column(name = "number_of_dependents")
    private Integer numberOfDependents;

    @Column(name = "education_level")
    private String educationLevel;

    @Column(name = "housing_status")
    private String housingStatus;

    @Column(name = "residence_since")
    private LocalDate residenceSince;

    @Column(name = "disability_status")
    private Boolean disabilityStatus;

    @Lob
    @Column(name = "disability_description")
    private String disabilityDescription;

    @Column(name = "social_category")
    private String socialCategory;

    @Lob
    @Column(name = "notes")
    private String notes;

    @JsonIgnoreProperties(
        value = { "individualMember", "organizationMember", "socialProfile", "professionalProfile", "cooperative", "branch" },
        allowSetters = true
    )
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "socialProfile")
    private Member member;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SocialProfile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MaritalStatus getMaritalStatus() {
        return this.maritalStatus;
    }

    public SocialProfile maritalStatus(MaritalStatus maritalStatus) {
        this.setMaritalStatus(maritalStatus);
        return this;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public Integer getNumberOfChildren() {
        return this.numberOfChildren;
    }

    public SocialProfile numberOfChildren(Integer numberOfChildren) {
        this.setNumberOfChildren(numberOfChildren);
        return this;
    }

    public void setNumberOfChildren(Integer numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }

    public Integer getNumberOfDependents() {
        return this.numberOfDependents;
    }

    public SocialProfile numberOfDependents(Integer numberOfDependents) {
        this.setNumberOfDependents(numberOfDependents);
        return this;
    }

    public void setNumberOfDependents(Integer numberOfDependents) {
        this.numberOfDependents = numberOfDependents;
    }

    public String getEducationLevel() {
        return this.educationLevel;
    }

    public SocialProfile educationLevel(String educationLevel) {
        this.setEducationLevel(educationLevel);
        return this;
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

    public String getHousingStatus() {
        return this.housingStatus;
    }

    public SocialProfile housingStatus(String housingStatus) {
        this.setHousingStatus(housingStatus);
        return this;
    }

    public void setHousingStatus(String housingStatus) {
        this.housingStatus = housingStatus;
    }

    public LocalDate getResidenceSince() {
        return this.residenceSince;
    }

    public SocialProfile residenceSince(LocalDate residenceSince) {
        this.setResidenceSince(residenceSince);
        return this;
    }

    public void setResidenceSince(LocalDate residenceSince) {
        this.residenceSince = residenceSince;
    }

    public Boolean getDisabilityStatus() {
        return this.disabilityStatus;
    }

    public SocialProfile disabilityStatus(Boolean disabilityStatus) {
        this.setDisabilityStatus(disabilityStatus);
        return this;
    }

    public void setDisabilityStatus(Boolean disabilityStatus) {
        this.disabilityStatus = disabilityStatus;
    }

    public String getDisabilityDescription() {
        return this.disabilityDescription;
    }

    public SocialProfile disabilityDescription(String disabilityDescription) {
        this.setDisabilityDescription(disabilityDescription);
        return this;
    }

    public void setDisabilityDescription(String disabilityDescription) {
        this.disabilityDescription = disabilityDescription;
    }

    public String getSocialCategory() {
        return this.socialCategory;
    }

    public SocialProfile socialCategory(String socialCategory) {
        this.setSocialCategory(socialCategory);
        return this;
    }

    public void setSocialCategory(String socialCategory) {
        this.socialCategory = socialCategory;
    }

    public String getNotes() {
        return this.notes;
    }

    public SocialProfile notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Member getMember() {
        return this.member;
    }

    public void setMember(Member member) {
        if (this.member != null) {
            this.member.setSocialProfile(null);
        }
        if (member != null) {
            member.setSocialProfile(this);
        }
        this.member = member;
    }

    public SocialProfile member(Member member) {
        this.setMember(member);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SocialProfile)) {
            return false;
        }
        return getId() != null && getId().equals(((SocialProfile) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SocialProfile{" +
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
