package com.naline.coopfull.service.dto;

import com.naline.coopfull.domain.enumeration.MemberStatus;
import com.naline.coopfull.domain.enumeration.MemberType;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.naline.coopfull.domain.Member} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MemberDTO implements Serializable {

    private Long id;

    @NotNull
    private String memberNumber;

    @NotNull
    private MemberType memberType;

    @NotNull
    private MemberStatus status;

    private LocalDate admissionDate;

    private LocalDate exitDate;

    private String exitReason;

    @Lob
    private String notes;

    @NotNull
    private Instant createdDate;

    private Instant lastModifiedDate;

    private IndividualMemberDTO individualMember;

    private OrganizationMemberDTO organizationMember;

    private SocialProfileDTO socialProfile;

    private ProfessionalProfileDTO professionalProfile;

    private CooperativeDTO cooperative;

    private CooperativeBranchDTO branch;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMemberNumber() {
        return memberNumber;
    }

    public void setMemberNumber(String memberNumber) {
        this.memberNumber = memberNumber;
    }

    public MemberType getMemberType() {
        return memberType;
    }

    public void setMemberType(MemberType memberType) {
        this.memberType = memberType;
    }

    public MemberStatus getStatus() {
        return status;
    }

    public void setStatus(MemberStatus status) {
        this.status = status;
    }

    public LocalDate getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(LocalDate admissionDate) {
        this.admissionDate = admissionDate;
    }

    public LocalDate getExitDate() {
        return exitDate;
    }

    public void setExitDate(LocalDate exitDate) {
        this.exitDate = exitDate;
    }

    public String getExitReason() {
        return exitReason;
    }

    public void setExitReason(String exitReason) {
        this.exitReason = exitReason;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public IndividualMemberDTO getIndividualMember() {
        return individualMember;
    }

    public void setIndividualMember(IndividualMemberDTO individualMember) {
        this.individualMember = individualMember;
    }

    public OrganizationMemberDTO getOrganizationMember() {
        return organizationMember;
    }

    public void setOrganizationMember(OrganizationMemberDTO organizationMember) {
        this.organizationMember = organizationMember;
    }

    public SocialProfileDTO getSocialProfile() {
        return socialProfile;
    }

    public void setSocialProfile(SocialProfileDTO socialProfile) {
        this.socialProfile = socialProfile;
    }

    public ProfessionalProfileDTO getProfessionalProfile() {
        return professionalProfile;
    }

    public void setProfessionalProfile(ProfessionalProfileDTO professionalProfile) {
        this.professionalProfile = professionalProfile;
    }

    public CooperativeDTO getCooperative() {
        return cooperative;
    }

    public void setCooperative(CooperativeDTO cooperative) {
        this.cooperative = cooperative;
    }

    public CooperativeBranchDTO getBranch() {
        return branch;
    }

    public void setBranch(CooperativeBranchDTO branch) {
        this.branch = branch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MemberDTO)) {
            return false;
        }

        MemberDTO memberDTO = (MemberDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, memberDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MemberDTO{" +
            "id=" + getId() +
            ", memberNumber='" + getMemberNumber() + "'" +
            ", memberType='" + getMemberType() + "'" +
            ", status='" + getStatus() + "'" +
            ", admissionDate='" + getAdmissionDate() + "'" +
            ", exitDate='" + getExitDate() + "'" +
            ", exitReason='" + getExitReason() + "'" +
            ", notes='" + getNotes() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", individualMember=" + getIndividualMember() +
            ", organizationMember=" + getOrganizationMember() +
            ", socialProfile=" + getSocialProfile() +
            ", professionalProfile=" + getProfessionalProfile() +
            ", cooperative=" + getCooperative() +
            ", branch=" + getBranch() +
            "}";
    }
}
