package com.naline.coopfull.service.criteria;

import com.naline.coopfull.domain.enumeration.MaritalStatus;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.naline.coopfull.domain.SocialProfile} entity. This class is used
 * in {@link com.naline.coopfull.web.rest.SocialProfileResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /social-profiles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SocialProfileCriteria implements Serializable, Criteria {

    /**
     * Class for filtering MaritalStatus
     */
    public static class MaritalStatusFilter extends Filter<MaritalStatus> {

        public MaritalStatusFilter() {}

        public MaritalStatusFilter(MaritalStatusFilter filter) {
            super(filter);
        }

        @Override
        public MaritalStatusFilter copy() {
            return new MaritalStatusFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private MaritalStatusFilter maritalStatus;

    private IntegerFilter numberOfChildren;

    private IntegerFilter numberOfDependents;

    private StringFilter educationLevel;

    private StringFilter housingStatus;

    private LocalDateFilter residenceSince;

    private BooleanFilter disabilityStatus;

    private StringFilter socialCategory;

    private LongFilter memberId;

    private Boolean distinct;

    public SocialProfileCriteria() {}

    public SocialProfileCriteria(SocialProfileCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.maritalStatus = other.optionalMaritalStatus().map(MaritalStatusFilter::copy).orElse(null);
        this.numberOfChildren = other.optionalNumberOfChildren().map(IntegerFilter::copy).orElse(null);
        this.numberOfDependents = other.optionalNumberOfDependents().map(IntegerFilter::copy).orElse(null);
        this.educationLevel = other.optionalEducationLevel().map(StringFilter::copy).orElse(null);
        this.housingStatus = other.optionalHousingStatus().map(StringFilter::copy).orElse(null);
        this.residenceSince = other.optionalResidenceSince().map(LocalDateFilter::copy).orElse(null);
        this.disabilityStatus = other.optionalDisabilityStatus().map(BooleanFilter::copy).orElse(null);
        this.socialCategory = other.optionalSocialCategory().map(StringFilter::copy).orElse(null);
        this.memberId = other.optionalMemberId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SocialProfileCriteria copy() {
        return new SocialProfileCriteria(this);
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

    public MaritalStatusFilter getMaritalStatus() {
        return maritalStatus;
    }

    public Optional<MaritalStatusFilter> optionalMaritalStatus() {
        return Optional.ofNullable(maritalStatus);
    }

    public MaritalStatusFilter maritalStatus() {
        if (maritalStatus == null) {
            setMaritalStatus(new MaritalStatusFilter());
        }
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatusFilter maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public IntegerFilter getNumberOfChildren() {
        return numberOfChildren;
    }

    public Optional<IntegerFilter> optionalNumberOfChildren() {
        return Optional.ofNullable(numberOfChildren);
    }

    public IntegerFilter numberOfChildren() {
        if (numberOfChildren == null) {
            setNumberOfChildren(new IntegerFilter());
        }
        return numberOfChildren;
    }

    public void setNumberOfChildren(IntegerFilter numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }

    public IntegerFilter getNumberOfDependents() {
        return numberOfDependents;
    }

    public Optional<IntegerFilter> optionalNumberOfDependents() {
        return Optional.ofNullable(numberOfDependents);
    }

    public IntegerFilter numberOfDependents() {
        if (numberOfDependents == null) {
            setNumberOfDependents(new IntegerFilter());
        }
        return numberOfDependents;
    }

    public void setNumberOfDependents(IntegerFilter numberOfDependents) {
        this.numberOfDependents = numberOfDependents;
    }

    public StringFilter getEducationLevel() {
        return educationLevel;
    }

    public Optional<StringFilter> optionalEducationLevel() {
        return Optional.ofNullable(educationLevel);
    }

    public StringFilter educationLevel() {
        if (educationLevel == null) {
            setEducationLevel(new StringFilter());
        }
        return educationLevel;
    }

    public void setEducationLevel(StringFilter educationLevel) {
        this.educationLevel = educationLevel;
    }

    public StringFilter getHousingStatus() {
        return housingStatus;
    }

    public Optional<StringFilter> optionalHousingStatus() {
        return Optional.ofNullable(housingStatus);
    }

    public StringFilter housingStatus() {
        if (housingStatus == null) {
            setHousingStatus(new StringFilter());
        }
        return housingStatus;
    }

    public void setHousingStatus(StringFilter housingStatus) {
        this.housingStatus = housingStatus;
    }

    public LocalDateFilter getResidenceSince() {
        return residenceSince;
    }

    public Optional<LocalDateFilter> optionalResidenceSince() {
        return Optional.ofNullable(residenceSince);
    }

    public LocalDateFilter residenceSince() {
        if (residenceSince == null) {
            setResidenceSince(new LocalDateFilter());
        }
        return residenceSince;
    }

    public void setResidenceSince(LocalDateFilter residenceSince) {
        this.residenceSince = residenceSince;
    }

    public BooleanFilter getDisabilityStatus() {
        return disabilityStatus;
    }

    public Optional<BooleanFilter> optionalDisabilityStatus() {
        return Optional.ofNullable(disabilityStatus);
    }

    public BooleanFilter disabilityStatus() {
        if (disabilityStatus == null) {
            setDisabilityStatus(new BooleanFilter());
        }
        return disabilityStatus;
    }

    public void setDisabilityStatus(BooleanFilter disabilityStatus) {
        this.disabilityStatus = disabilityStatus;
    }

    public StringFilter getSocialCategory() {
        return socialCategory;
    }

    public Optional<StringFilter> optionalSocialCategory() {
        return Optional.ofNullable(socialCategory);
    }

    public StringFilter socialCategory() {
        if (socialCategory == null) {
            setSocialCategory(new StringFilter());
        }
        return socialCategory;
    }

    public void setSocialCategory(StringFilter socialCategory) {
        this.socialCategory = socialCategory;
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
        final SocialProfileCriteria that = (SocialProfileCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(maritalStatus, that.maritalStatus) &&
            Objects.equals(numberOfChildren, that.numberOfChildren) &&
            Objects.equals(numberOfDependents, that.numberOfDependents) &&
            Objects.equals(educationLevel, that.educationLevel) &&
            Objects.equals(housingStatus, that.housingStatus) &&
            Objects.equals(residenceSince, that.residenceSince) &&
            Objects.equals(disabilityStatus, that.disabilityStatus) &&
            Objects.equals(socialCategory, that.socialCategory) &&
            Objects.equals(memberId, that.memberId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            maritalStatus,
            numberOfChildren,
            numberOfDependents,
            educationLevel,
            housingStatus,
            residenceSince,
            disabilityStatus,
            socialCategory,
            memberId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SocialProfileCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalMaritalStatus().map(f -> "maritalStatus=" + f + ", ").orElse("") +
            optionalNumberOfChildren().map(f -> "numberOfChildren=" + f + ", ").orElse("") +
            optionalNumberOfDependents().map(f -> "numberOfDependents=" + f + ", ").orElse("") +
            optionalEducationLevel().map(f -> "educationLevel=" + f + ", ").orElse("") +
            optionalHousingStatus().map(f -> "housingStatus=" + f + ", ").orElse("") +
            optionalResidenceSince().map(f -> "residenceSince=" + f + ", ").orElse("") +
            optionalDisabilityStatus().map(f -> "disabilityStatus=" + f + ", ").orElse("") +
            optionalSocialCategory().map(f -> "socialCategory=" + f + ", ").orElse("") +
            optionalMemberId().map(f -> "memberId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
