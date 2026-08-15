package com.naline.coopfull.service.criteria;

import com.naline.coopfull.domain.enumeration.MemberStatus;
import com.naline.coopfull.domain.enumeration.MemberType;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.naline.coopfull.domain.Member} entity. This class is used
 * in {@link com.naline.coopfull.web.rest.MemberResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /members?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MemberCriteria implements Serializable, Criteria {

    /**
     * Class for filtering MemberType
     */
    public static class MemberTypeFilter extends Filter<MemberType> {

        public MemberTypeFilter() {}

        public MemberTypeFilter(MemberTypeFilter filter) {
            super(filter);
        }

        @Override
        public MemberTypeFilter copy() {
            return new MemberTypeFilter(this);
        }
    }

    /**
     * Class for filtering MemberStatus
     */
    public static class MemberStatusFilter extends Filter<MemberStatus> {

        public MemberStatusFilter() {}

        public MemberStatusFilter(MemberStatusFilter filter) {
            super(filter);
        }

        @Override
        public MemberStatusFilter copy() {
            return new MemberStatusFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter memberNumber;

    private MemberTypeFilter memberType;

    private MemberStatusFilter status;

    private LocalDateFilter admissionDate;

    private LocalDateFilter exitDate;

    private StringFilter exitReason;

    private InstantFilter createdDate;

    private InstantFilter lastModifiedDate;

    private LongFilter individualMemberId;

    private LongFilter organizationMemberId;

    private LongFilter socialProfileId;

    private LongFilter professionalProfileId;

    private LongFilter cooperativeId;

    private LongFilter branchId;

    private Boolean distinct;

    public MemberCriteria() {}

    public MemberCriteria(MemberCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.memberNumber = other.optionalMemberNumber().map(StringFilter::copy).orElse(null);
        this.memberType = other.optionalMemberType().map(MemberTypeFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(MemberStatusFilter::copy).orElse(null);
        this.admissionDate = other.optionalAdmissionDate().map(LocalDateFilter::copy).orElse(null);
        this.exitDate = other.optionalExitDate().map(LocalDateFilter::copy).orElse(null);
        this.exitReason = other.optionalExitReason().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.individualMemberId = other.optionalIndividualMemberId().map(LongFilter::copy).orElse(null);
        this.organizationMemberId = other.optionalOrganizationMemberId().map(LongFilter::copy).orElse(null);
        this.socialProfileId = other.optionalSocialProfileId().map(LongFilter::copy).orElse(null);
        this.professionalProfileId = other.optionalProfessionalProfileId().map(LongFilter::copy).orElse(null);
        this.cooperativeId = other.optionalCooperativeId().map(LongFilter::copy).orElse(null);
        this.branchId = other.optionalBranchId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public MemberCriteria copy() {
        return new MemberCriteria(this);
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

    public StringFilter getMemberNumber() {
        return memberNumber;
    }

    public Optional<StringFilter> optionalMemberNumber() {
        return Optional.ofNullable(memberNumber);
    }

    public StringFilter memberNumber() {
        if (memberNumber == null) {
            setMemberNumber(new StringFilter());
        }
        return memberNumber;
    }

    public void setMemberNumber(StringFilter memberNumber) {
        this.memberNumber = memberNumber;
    }

    public MemberTypeFilter getMemberType() {
        return memberType;
    }

    public Optional<MemberTypeFilter> optionalMemberType() {
        return Optional.ofNullable(memberType);
    }

    public MemberTypeFilter memberType() {
        if (memberType == null) {
            setMemberType(new MemberTypeFilter());
        }
        return memberType;
    }

    public void setMemberType(MemberTypeFilter memberType) {
        this.memberType = memberType;
    }

    public MemberStatusFilter getStatus() {
        return status;
    }

    public Optional<MemberStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public MemberStatusFilter status() {
        if (status == null) {
            setStatus(new MemberStatusFilter());
        }
        return status;
    }

    public void setStatus(MemberStatusFilter status) {
        this.status = status;
    }

    public LocalDateFilter getAdmissionDate() {
        return admissionDate;
    }

    public Optional<LocalDateFilter> optionalAdmissionDate() {
        return Optional.ofNullable(admissionDate);
    }

    public LocalDateFilter admissionDate() {
        if (admissionDate == null) {
            setAdmissionDate(new LocalDateFilter());
        }
        return admissionDate;
    }

    public void setAdmissionDate(LocalDateFilter admissionDate) {
        this.admissionDate = admissionDate;
    }

    public LocalDateFilter getExitDate() {
        return exitDate;
    }

    public Optional<LocalDateFilter> optionalExitDate() {
        return Optional.ofNullable(exitDate);
    }

    public LocalDateFilter exitDate() {
        if (exitDate == null) {
            setExitDate(new LocalDateFilter());
        }
        return exitDate;
    }

    public void setExitDate(LocalDateFilter exitDate) {
        this.exitDate = exitDate;
    }

    public StringFilter getExitReason() {
        return exitReason;
    }

    public Optional<StringFilter> optionalExitReason() {
        return Optional.ofNullable(exitReason);
    }

    public StringFilter exitReason() {
        if (exitReason == null) {
            setExitReason(new StringFilter());
        }
        return exitReason;
    }

    public void setExitReason(StringFilter exitReason) {
        this.exitReason = exitReason;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public Optional<InstantFilter> optionalCreatedDate() {
        return Optional.ofNullable(createdDate);
    }

    public InstantFilter createdDate() {
        if (createdDate == null) {
            setCreatedDate(new InstantFilter());
        }
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public InstantFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Optional<InstantFilter> optionalLastModifiedDate() {
        return Optional.ofNullable(lastModifiedDate);
    }

    public InstantFilter lastModifiedDate() {
        if (lastModifiedDate == null) {
            setLastModifiedDate(new InstantFilter());
        }
        return lastModifiedDate;
    }

    public void setLastModifiedDate(InstantFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public LongFilter getIndividualMemberId() {
        return individualMemberId;
    }

    public Optional<LongFilter> optionalIndividualMemberId() {
        return Optional.ofNullable(individualMemberId);
    }

    public LongFilter individualMemberId() {
        if (individualMemberId == null) {
            setIndividualMemberId(new LongFilter());
        }
        return individualMemberId;
    }

    public void setIndividualMemberId(LongFilter individualMemberId) {
        this.individualMemberId = individualMemberId;
    }

    public LongFilter getOrganizationMemberId() {
        return organizationMemberId;
    }

    public Optional<LongFilter> optionalOrganizationMemberId() {
        return Optional.ofNullable(organizationMemberId);
    }

    public LongFilter organizationMemberId() {
        if (organizationMemberId == null) {
            setOrganizationMemberId(new LongFilter());
        }
        return organizationMemberId;
    }

    public void setOrganizationMemberId(LongFilter organizationMemberId) {
        this.organizationMemberId = organizationMemberId;
    }

    public LongFilter getSocialProfileId() {
        return socialProfileId;
    }

    public Optional<LongFilter> optionalSocialProfileId() {
        return Optional.ofNullable(socialProfileId);
    }

    public LongFilter socialProfileId() {
        if (socialProfileId == null) {
            setSocialProfileId(new LongFilter());
        }
        return socialProfileId;
    }

    public void setSocialProfileId(LongFilter socialProfileId) {
        this.socialProfileId = socialProfileId;
    }

    public LongFilter getProfessionalProfileId() {
        return professionalProfileId;
    }

    public Optional<LongFilter> optionalProfessionalProfileId() {
        return Optional.ofNullable(professionalProfileId);
    }

    public LongFilter professionalProfileId() {
        if (professionalProfileId == null) {
            setProfessionalProfileId(new LongFilter());
        }
        return professionalProfileId;
    }

    public void setProfessionalProfileId(LongFilter professionalProfileId) {
        this.professionalProfileId = professionalProfileId;
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
        final MemberCriteria that = (MemberCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(memberNumber, that.memberNumber) &&
            Objects.equals(memberType, that.memberType) &&
            Objects.equals(status, that.status) &&
            Objects.equals(admissionDate, that.admissionDate) &&
            Objects.equals(exitDate, that.exitDate) &&
            Objects.equals(exitReason, that.exitReason) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(individualMemberId, that.individualMemberId) &&
            Objects.equals(organizationMemberId, that.organizationMemberId) &&
            Objects.equals(socialProfileId, that.socialProfileId) &&
            Objects.equals(professionalProfileId, that.professionalProfileId) &&
            Objects.equals(cooperativeId, that.cooperativeId) &&
            Objects.equals(branchId, that.branchId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            memberNumber,
            memberType,
            status,
            admissionDate,
            exitDate,
            exitReason,
            createdDate,
            lastModifiedDate,
            individualMemberId,
            organizationMemberId,
            socialProfileId,
            professionalProfileId,
            cooperativeId,
            branchId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MemberCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalMemberNumber().map(f -> "memberNumber=" + f + ", ").orElse("") +
            optionalMemberType().map(f -> "memberType=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalAdmissionDate().map(f -> "admissionDate=" + f + ", ").orElse("") +
            optionalExitDate().map(f -> "exitDate=" + f + ", ").orElse("") +
            optionalExitReason().map(f -> "exitReason=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalIndividualMemberId().map(f -> "individualMemberId=" + f + ", ").orElse("") +
            optionalOrganizationMemberId().map(f -> "organizationMemberId=" + f + ", ").orElse("") +
            optionalSocialProfileId().map(f -> "socialProfileId=" + f + ", ").orElse("") +
            optionalProfessionalProfileId().map(f -> "professionalProfileId=" + f + ", ").orElse("") +
            optionalCooperativeId().map(f -> "cooperativeId=" + f + ", ").orElse("") +
            optionalBranchId().map(f -> "branchId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
