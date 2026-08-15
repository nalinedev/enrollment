package com.naline.coopfull.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.naline.coopfull.domain.FamilyMember} entity. This class is used
 * in {@link com.naline.coopfull.web.rest.FamilyMemberResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /family-members?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FamilyMemberCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter firstName;

    private StringFilter middleName;

    private StringFilter lastName;

    private StringFilter relationship;

    private StringFilter gender;

    private LocalDateFilter birthDate;

    private StringFilter birthPlace;

    private StringFilter nationality;

    private StringFilter phoneNumber;

    private StringFilter occupation;

    private BooleanFilter dependent;

    private LongFilter memberId;

    private Boolean distinct;

    public FamilyMemberCriteria() {}

    public FamilyMemberCriteria(FamilyMemberCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.firstName = other.optionalFirstName().map(StringFilter::copy).orElse(null);
        this.middleName = other.optionalMiddleName().map(StringFilter::copy).orElse(null);
        this.lastName = other.optionalLastName().map(StringFilter::copy).orElse(null);
        this.relationship = other.optionalRelationship().map(StringFilter::copy).orElse(null);
        this.gender = other.optionalGender().map(StringFilter::copy).orElse(null);
        this.birthDate = other.optionalBirthDate().map(LocalDateFilter::copy).orElse(null);
        this.birthPlace = other.optionalBirthPlace().map(StringFilter::copy).orElse(null);
        this.nationality = other.optionalNationality().map(StringFilter::copy).orElse(null);
        this.phoneNumber = other.optionalPhoneNumber().map(StringFilter::copy).orElse(null);
        this.occupation = other.optionalOccupation().map(StringFilter::copy).orElse(null);
        this.dependent = other.optionalDependent().map(BooleanFilter::copy).orElse(null);
        this.memberId = other.optionalMemberId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public FamilyMemberCriteria copy() {
        return new FamilyMemberCriteria(this);
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

    public StringFilter getFirstName() {
        return firstName;
    }

    public Optional<StringFilter> optionalFirstName() {
        return Optional.ofNullable(firstName);
    }

    public StringFilter firstName() {
        if (firstName == null) {
            setFirstName(new StringFilter());
        }
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getMiddleName() {
        return middleName;
    }

    public Optional<StringFilter> optionalMiddleName() {
        return Optional.ofNullable(middleName);
    }

    public StringFilter middleName() {
        if (middleName == null) {
            setMiddleName(new StringFilter());
        }
        return middleName;
    }

    public void setMiddleName(StringFilter middleName) {
        this.middleName = middleName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public Optional<StringFilter> optionalLastName() {
        return Optional.ofNullable(lastName);
    }

    public StringFilter lastName() {
        if (lastName == null) {
            setLastName(new StringFilter());
        }
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getRelationship() {
        return relationship;
    }

    public Optional<StringFilter> optionalRelationship() {
        return Optional.ofNullable(relationship);
    }

    public StringFilter relationship() {
        if (relationship == null) {
            setRelationship(new StringFilter());
        }
        return relationship;
    }

    public void setRelationship(StringFilter relationship) {
        this.relationship = relationship;
    }

    public StringFilter getGender() {
        return gender;
    }

    public Optional<StringFilter> optionalGender() {
        return Optional.ofNullable(gender);
    }

    public StringFilter gender() {
        if (gender == null) {
            setGender(new StringFilter());
        }
        return gender;
    }

    public void setGender(StringFilter gender) {
        this.gender = gender;
    }

    public LocalDateFilter getBirthDate() {
        return birthDate;
    }

    public Optional<LocalDateFilter> optionalBirthDate() {
        return Optional.ofNullable(birthDate);
    }

    public LocalDateFilter birthDate() {
        if (birthDate == null) {
            setBirthDate(new LocalDateFilter());
        }
        return birthDate;
    }

    public void setBirthDate(LocalDateFilter birthDate) {
        this.birthDate = birthDate;
    }

    public StringFilter getBirthPlace() {
        return birthPlace;
    }

    public Optional<StringFilter> optionalBirthPlace() {
        return Optional.ofNullable(birthPlace);
    }

    public StringFilter birthPlace() {
        if (birthPlace == null) {
            setBirthPlace(new StringFilter());
        }
        return birthPlace;
    }

    public void setBirthPlace(StringFilter birthPlace) {
        this.birthPlace = birthPlace;
    }

    public StringFilter getNationality() {
        return nationality;
    }

    public Optional<StringFilter> optionalNationality() {
        return Optional.ofNullable(nationality);
    }

    public StringFilter nationality() {
        if (nationality == null) {
            setNationality(new StringFilter());
        }
        return nationality;
    }

    public void setNationality(StringFilter nationality) {
        this.nationality = nationality;
    }

    public StringFilter getPhoneNumber() {
        return phoneNumber;
    }

    public Optional<StringFilter> optionalPhoneNumber() {
        return Optional.ofNullable(phoneNumber);
    }

    public StringFilter phoneNumber() {
        if (phoneNumber == null) {
            setPhoneNumber(new StringFilter());
        }
        return phoneNumber;
    }

    public void setPhoneNumber(StringFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public StringFilter getOccupation() {
        return occupation;
    }

    public Optional<StringFilter> optionalOccupation() {
        return Optional.ofNullable(occupation);
    }

    public StringFilter occupation() {
        if (occupation == null) {
            setOccupation(new StringFilter());
        }
        return occupation;
    }

    public void setOccupation(StringFilter occupation) {
        this.occupation = occupation;
    }

    public BooleanFilter getDependent() {
        return dependent;
    }

    public Optional<BooleanFilter> optionalDependent() {
        return Optional.ofNullable(dependent);
    }

    public BooleanFilter dependent() {
        if (dependent == null) {
            setDependent(new BooleanFilter());
        }
        return dependent;
    }

    public void setDependent(BooleanFilter dependent) {
        this.dependent = dependent;
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
        final FamilyMemberCriteria that = (FamilyMemberCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(middleName, that.middleName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(relationship, that.relationship) &&
            Objects.equals(gender, that.gender) &&
            Objects.equals(birthDate, that.birthDate) &&
            Objects.equals(birthPlace, that.birthPlace) &&
            Objects.equals(nationality, that.nationality) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(occupation, that.occupation) &&
            Objects.equals(dependent, that.dependent) &&
            Objects.equals(memberId, that.memberId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            firstName,
            middleName,
            lastName,
            relationship,
            gender,
            birthDate,
            birthPlace,
            nationality,
            phoneNumber,
            occupation,
            dependent,
            memberId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FamilyMemberCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalFirstName().map(f -> "firstName=" + f + ", ").orElse("") +
            optionalMiddleName().map(f -> "middleName=" + f + ", ").orElse("") +
            optionalLastName().map(f -> "lastName=" + f + ", ").orElse("") +
            optionalRelationship().map(f -> "relationship=" + f + ", ").orElse("") +
            optionalGender().map(f -> "gender=" + f + ", ").orElse("") +
            optionalBirthDate().map(f -> "birthDate=" + f + ", ").orElse("") +
            optionalBirthPlace().map(f -> "birthPlace=" + f + ", ").orElse("") +
            optionalNationality().map(f -> "nationality=" + f + ", ").orElse("") +
            optionalPhoneNumber().map(f -> "phoneNumber=" + f + ", ").orElse("") +
            optionalOccupation().map(f -> "occupation=" + f + ", ").orElse("") +
            optionalDependent().map(f -> "dependent=" + f + ", ").orElse("") +
            optionalMemberId().map(f -> "memberId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
