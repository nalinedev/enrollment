package com.naline.coopfull.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.naline.coopfull.domain.enumeration.MembershipApplicationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MembershipApplication.
 */
@Entity
@Table(name = "membership_application")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MembershipApplication implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "application_number", nullable = false, unique = true)
    private String applicationNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MembershipApplicationStatus status;

    @NotNull
    @Column(name = "application_date", nullable = false)
    private LocalDate applicationDate;

    @Column(name = "submitted_at")
    private Instant submittedAt;

    @Column(name = "reviewed_at")
    private Instant reviewedAt;

    @Column(name = "approved_at")
    private Instant approvedAt;

    @Column(name = "rejected_at")
    private Instant rejectedAt;

    @Lob
    @Column(name = "rejection_reason")
    private String rejectionReason;

    @Lob
    @Column(name = "review_comments")
    private String reviewComments;

    @NotNull
    @Column(name = "confirmation", nullable = false)
    private Boolean confirmation;

    @Lob
    @Column(name = "notes")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "individualMember", "organizationMember", "socialProfile", "professionalProfile", "cooperative", "branch" },
        allowSetters = true
    )
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Cooperative cooperative;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "cooperative", "location" }, allowSetters = true)
    private CooperativeBranch branch;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MembershipApplication id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApplicationNumber() {
        return this.applicationNumber;
    }

    public MembershipApplication applicationNumber(String applicationNumber) {
        this.setApplicationNumber(applicationNumber);
        return this;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public MembershipApplicationStatus getStatus() {
        return this.status;
    }

    public MembershipApplication status(MembershipApplicationStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(MembershipApplicationStatus status) {
        this.status = status;
    }

    public LocalDate getApplicationDate() {
        return this.applicationDate;
    }

    public MembershipApplication applicationDate(LocalDate applicationDate) {
        this.setApplicationDate(applicationDate);
        return this;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    public Instant getSubmittedAt() {
        return this.submittedAt;
    }

    public MembershipApplication submittedAt(Instant submittedAt) {
        this.setSubmittedAt(submittedAt);
        return this;
    }

    public void setSubmittedAt(Instant submittedAt) {
        this.submittedAt = submittedAt;
    }

    public Instant getReviewedAt() {
        return this.reviewedAt;
    }

    public MembershipApplication reviewedAt(Instant reviewedAt) {
        this.setReviewedAt(reviewedAt);
        return this;
    }

    public void setReviewedAt(Instant reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public Instant getApprovedAt() {
        return this.approvedAt;
    }

    public MembershipApplication approvedAt(Instant approvedAt) {
        this.setApprovedAt(approvedAt);
        return this;
    }

    public void setApprovedAt(Instant approvedAt) {
        this.approvedAt = approvedAt;
    }

    public Instant getRejectedAt() {
        return this.rejectedAt;
    }

    public MembershipApplication rejectedAt(Instant rejectedAt) {
        this.setRejectedAt(rejectedAt);
        return this;
    }

    public void setRejectedAt(Instant rejectedAt) {
        this.rejectedAt = rejectedAt;
    }

    public String getRejectionReason() {
        return this.rejectionReason;
    }

    public MembershipApplication rejectionReason(String rejectionReason) {
        this.setRejectionReason(rejectionReason);
        return this;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public String getReviewComments() {
        return this.reviewComments;
    }

    public MembershipApplication reviewComments(String reviewComments) {
        this.setReviewComments(reviewComments);
        return this;
    }

    public void setReviewComments(String reviewComments) {
        this.reviewComments = reviewComments;
    }

    public Boolean getConfirmation() {
        return this.confirmation;
    }

    public MembershipApplication confirmation(Boolean confirmation) {
        this.setConfirmation(confirmation);
        return this;
    }

    public void setConfirmation(Boolean confirmation) {
        this.confirmation = confirmation;
    }

    public String getNotes() {
        return this.notes;
    }

    public MembershipApplication notes(String notes) {
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
        this.member = member;
    }

    public MembershipApplication member(Member member) {
        this.setMember(member);
        return this;
    }

    public Cooperative getCooperative() {
        return this.cooperative;
    }

    public void setCooperative(Cooperative cooperative) {
        this.cooperative = cooperative;
    }

    public MembershipApplication cooperative(Cooperative cooperative) {
        this.setCooperative(cooperative);
        return this;
    }

    public CooperativeBranch getBranch() {
        return this.branch;
    }

    public void setBranch(CooperativeBranch cooperativeBranch) {
        this.branch = cooperativeBranch;
    }

    public MembershipApplication branch(CooperativeBranch cooperativeBranch) {
        this.setBranch(cooperativeBranch);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MembershipApplication)) {
            return false;
        }
        return getId() != null && getId().equals(((MembershipApplication) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MembershipApplication{" +
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
            "}";
    }
}
