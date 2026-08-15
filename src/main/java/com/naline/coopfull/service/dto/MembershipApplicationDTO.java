package com.naline.coopfull.service.dto;

import com.naline.coopfull.domain.enumeration.MembershipApplicationStatus;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.naline.coopfull.domain.MembershipApplication} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MembershipApplicationDTO implements Serializable {

    private Long id;

    @NotNull
    private String applicationNumber;

    @NotNull
    private MembershipApplicationStatus status;

    @NotNull
    private LocalDate applicationDate;

    private Instant submittedAt;

    private Instant reviewedAt;

    private Instant approvedAt;

    private Instant rejectedAt;

    @Lob
    private String rejectionReason;

    @Lob
    private String reviewComments;

    @NotNull
    private Boolean confirmation;

    @Lob
    private String notes;

    private MemberDTO member;

    private CooperativeDTO cooperative;

    private CooperativeBranchDTO branch;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public MembershipApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(MembershipApplicationStatus status) {
        this.status = status;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    public Instant getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Instant submittedAt) {
        this.submittedAt = submittedAt;
    }

    public Instant getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(Instant reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public Instant getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(Instant approvedAt) {
        this.approvedAt = approvedAt;
    }

    public Instant getRejectedAt() {
        return rejectedAt;
    }

    public void setRejectedAt(Instant rejectedAt) {
        this.rejectedAt = rejectedAt;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public String getReviewComments() {
        return reviewComments;
    }

    public void setReviewComments(String reviewComments) {
        this.reviewComments = reviewComments;
    }

    public Boolean getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(Boolean confirmation) {
        this.confirmation = confirmation;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public MemberDTO getMember() {
        return member;
    }

    public void setMember(MemberDTO member) {
        this.member = member;
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
        if (!(o instanceof MembershipApplicationDTO)) {
            return false;
        }

        MembershipApplicationDTO membershipApplicationDTO = (MembershipApplicationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, membershipApplicationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MembershipApplicationDTO{" +
            "id=" + getId() +
            ", applicationNumber='" + getApplicationNumber() + "'" +
            ", status='" + getStatus() + "'" +
            ", applicationDate='" + getApplicationDate() + "'" +
            ", submittedAt='" + getSubmittedAt() + "'" +
            ", reviewedAt='" + getReviewedAt() + "'" +
            ", approvedAt='" + getApprovedAt() + "'" +
            ", rejectedAt='" + getRejectedAt() + "'" +
            ", rejectionReason='" + getRejectionReason() + "'" +
            ", reviewComments='" + getReviewComments() + "'" +
            ", confirmation='" + getConfirmation() + "'" +
            ", notes='" + getNotes() + "'" +
            ", member=" + getMember() +
            ", cooperative=" + getCooperative() +
            ", branch=" + getBranch() +
            "}";
    }
}
