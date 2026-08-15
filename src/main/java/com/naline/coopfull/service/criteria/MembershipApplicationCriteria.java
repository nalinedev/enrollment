package com.naline.coopfull.service.criteria;

import com.naline.coopfull.domain.enumeration.MembershipApplicationStatus;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.naline.coopfull.domain.MembershipApplication} entity. This class is used
 * in {@link com.naline.coopfull.web.rest.MembershipApplicationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /membership-applications?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MembershipApplicationCriteria implements Serializable, Criteria {

    /**
     * Class for filtering MembershipApplicationStatus
     */
    public static class MembershipApplicationStatusFilter extends Filter<MembershipApplicationStatus> {

        public MembershipApplicationStatusFilter() {}

        public MembershipApplicationStatusFilter(MembershipApplicationStatusFilter filter) {
            super(filter);
        }

        @Override
        public MembershipApplicationStatusFilter copy() {
            return new MembershipApplicationStatusFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter applicationNumber;

    private MembershipApplicationStatusFilter status;

    private LocalDateFilter applicationDate;

    private InstantFilter submittedAt;

    private InstantFilter reviewedAt;

    private InstantFilter approvedAt;

    private InstantFilter rejectedAt;

    private BooleanFilter confirmation;

    private LongFilter memberId;

    private LongFilter cooperativeId;

    private LongFilter branchId;

    private Boolean distinct;

    public MembershipApplicationCriteria() {}

    public MembershipApplicationCriteria(MembershipApplicationCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.applicationNumber = other.optionalApplicationNumber().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(MembershipApplicationStatusFilter::copy).orElse(null);
        this.applicationDate = other.optionalApplicationDate().map(LocalDateFilter::copy).orElse(null);
        this.submittedAt = other.optionalSubmittedAt().map(InstantFilter::copy).orElse(null);
        this.reviewedAt = other.optionalReviewedAt().map(InstantFilter::copy).orElse(null);
        this.approvedAt = other.optionalApprovedAt().map(InstantFilter::copy).orElse(null);
        this.rejectedAt = other.optionalRejectedAt().map(InstantFilter::copy).orElse(null);
        this.confirmation = other.optionalConfirmation().map(BooleanFilter::copy).orElse(null);
        this.memberId = other.optionalMemberId().map(LongFilter::copy).orElse(null);
        this.cooperativeId = other.optionalCooperativeId().map(LongFilter::copy).orElse(null);
        this.branchId = other.optionalBranchId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public MembershipApplicationCriteria copy() {
        return new MembershipApplicationCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getApplicationNumber() {
        return applicationNumber;
    }

    public Optional<StringFilter> optionalApplicationNumber() {
        return Optional.ofNullable(applicationNumber);
    }

    public StringFilter applicationNumber() {
        if (applicationNumber == null) {
            setApplicationNumber(new StringFilter());
        }
        return applicationNumber;
    }

    public void setApplicationNumber(StringFilter applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public MembershipApplicationStatusFilter getStatus() {
        return status;
    }

    public Optional<MembershipApplicationStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public MembershipApplicationStatusFilter status() {
        if (status == null) {
            setStatus(new MembershipApplicationStatusFilter());
        }
        return status;
    }

    public void setStatus(MembershipApplicationStatusFilter status) {
        this.status = status;
    }

    public LocalDateFilter getApplicationDate() {
        return applicationDate;
    }

    public Optional<LocalDateFilter> optionalApplicationDate() {
        return Optional.ofNullable(applicationDate);
    }

    public LocalDateFilter applicationDate() {
        if (applicationDate == null) {
            setApplicationDate(new LocalDateFilter());
        }
        return applicationDate;
    }

    public void setApplicationDate(LocalDateFilter applicationDate) {
        this.applicationDate = applicationDate;
    }

    public InstantFilter getSubmittedAt() {
        return submittedAt;
    }

    public Optional<InstantFilter> optionalSubmittedAt() {
        return Optional.ofNullable(submittedAt);
    }

    public InstantFilter submittedAt() {
        if (submittedAt == null) {
            setSubmittedAt(new InstantFilter());
        }
        return submittedAt;
    }

    public void setSubmittedAt(InstantFilter submittedAt) {
        this.submittedAt = submittedAt;
    }

    public InstantFilter getReviewedAt() {
        return reviewedAt;
    }

    public Optional<InstantFilter> optionalReviewedAt() {
        return Optional.ofNullable(reviewedAt);
    }

    public InstantFilter reviewedAt() {
        if (reviewedAt == null) {
            setReviewedAt(new InstantFilter());
        }
        return reviewedAt;
    }

    public void setReviewedAt(InstantFilter reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public InstantFilter getApprovedAt() {
        return approvedAt;
    }

    public Optional<InstantFilter> optionalApprovedAt() {
        return Optional.ofNullable(approvedAt);
    }

    public InstantFilter approvedAt() {
        if (approvedAt == null) {
            setApprovedAt(new InstantFilter());
        }
        return approvedAt;
    }

    public void setApprovedAt(InstantFilter approvedAt) {
        this.approvedAt = approvedAt;
    }

    public InstantFilter getRejectedAt() {
        return rejectedAt;
    }

    public Optional<InstantFilter> optionalRejectedAt() {
        return Optional.ofNullable(rejectedAt);
    }

    public InstantFilter rejectedAt() {
        if (rejectedAt == null) {
            setRejectedAt(new InstantFilter());
        }
        return rejectedAt;
    }

    public void setRejectedAt(InstantFilter rejectedAt) {
        this.rejectedAt = rejectedAt;
    }

    public BooleanFilter getConfirmation() {
        return confirmation;
    }

    public Optional<BooleanFilter> optionalConfirmation() {
        return Optional.ofNullable(confirmation);
    }

    public BooleanFilter confirmation() {
        if (confirmation == null) {
            setConfirmation(new BooleanFilter());
        }
        return confirmation;
    }

    public void setConfirmation(BooleanFilter confirmation) {
        this.confirmation = confirmation;
    }

    public LongFilter getMemberId() {
        return memberId;
    }

    public Optional<LongFilter> optionalMemberId() {
        return Optional.ofNullable(memberId);
    }

    public LongFilter memberId() {
        if (memberId == null) {
            setMemberId(new LongFilter());
        }
        return memberId;
    }

    public void setMemberId(LongFilter memberId) {
        this.memberId = memberId;
    }

    public LongFilter getCooperativeId() {
        return cooperativeId;
    }

    public Optional<LongFilter> optionalCooperativeId() {
        return Optional.ofNullable(cooperativeId);
    }

    public LongFilter cooperativeId() {
        if (cooperativeId == null) {
            setCooperativeId(new LongFilter());
        }
        return cooperativeId;
    }

    public void setCooperativeId(LongFilter cooperativeId) {
        this.cooperativeId = cooperativeId;
    }

    public LongFilter getBranchId() {
        return branchId;
    }

    public Optional<LongFilter> optionalBranchId() {
        return Optional.ofNullable(branchId);
    }

    public LongFilter branchId() {
        if (branchId == null) {
            setBranchId(new LongFilter());
        }
        return branchId;
    }

    public void setBranchId(LongFilter branchId) {
        this.branchId = branchId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MembershipApplicationCriteria that = (MembershipApplicationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(applicationNumber, that.applicationNumber) &&
            Objects.equals(status, that.status) &&
            Objects.equals(applicationDate, that.applicationDate) &&
            Objects.equals(submittedAt, that.submittedAt) &&
            Objects.equals(reviewedAt, that.reviewedAt) &&
            Objects.equals(approvedAt, that.approvedAt) &&
            Objects.equals(rejectedAt, that.rejectedAt) &&
            Objects.equals(confirmation, that.confirmation) &&
            Objects.equals(memberId, that.memberId) &&
            Objects.equals(cooperativeId, that.cooperativeId) &&
            Objects.equals(branchId, that.branchId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            applicationNumber,
            status,
            applicationDate,
            submittedAt,
            reviewedAt,
            approvedAt,
            rejectedAt,
            confirmation,
            memberId,
            cooperativeId,
            branchId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MembershipApplicationCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalApplicationNumber().map(f -> "applicationNumber=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalApplicationDate().map(f -> "applicationDate=" + f + ", ").orElse("") +
            optionalSubmittedAt().map(f -> "submittedAt=" + f + ", ").orElse("") +
            optionalReviewedAt().map(f -> "reviewedAt=" + f + ", ").orElse("") +
            optionalApprovedAt().map(f -> "approvedAt=" + f + ", ").orElse("") +
            optionalRejectedAt().map(f -> "rejectedAt=" + f + ", ").orElse("") +
            optionalConfirmation().map(f -> "confirmation=" + f + ", ").orElse("") +
            optionalMemberId().map(f -> "memberId=" + f + ", ").orElse("") +
            optionalCooperativeId().map(f -> "cooperativeId=" + f + ", ").orElse("") +
            optionalBranchId().map(f -> "branchId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
