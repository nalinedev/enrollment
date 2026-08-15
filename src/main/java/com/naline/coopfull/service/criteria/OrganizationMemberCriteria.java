package com.naline.coopfull.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.naline.coopfull.domain.OrganizationMember} entity. This class is used
 * in {@link com.naline.coopfull.web.rest.OrganizationMemberResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /organization-members?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrganizationMemberCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter legalName;

    private StringFilter tradeName;

    private StringFilter registrationNumber;

    private StringFilter taxNumber;

    private StringFilter legalForm;

    private LocalDateFilter registrationDate;

    private StringFilter email;

    private StringFilter phoneNumber;

    private StringFilter website;

    private LongFilter memberId;

    private Boolean distinct;

    public OrganizationMemberCriteria() {}

    public OrganizationMemberCriteria(OrganizationMemberCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.legalName = other.optionalLegalName().map(StringFilter::copy).orElse(null);
        this.tradeName = other.optionalTradeName().map(StringFilter::copy).orElse(null);
        this.registrationNumber = other.optionalRegistrationNumber().map(StringFilter::copy).orElse(null);
        this.taxNumber = other.optionalTaxNumber().map(StringFilter::copy).orElse(null);
        this.legalForm = other.optionalLegalForm().map(StringFilter::copy).orElse(null);
        this.registrationDate = other.optionalRegistrationDate().map(LocalDateFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.phoneNumber = other.optionalPhoneNumber().map(StringFilter::copy).orElse(null);
        this.website = other.optionalWebsite().map(StringFilter::copy).orElse(null);
        this.memberId = other.optionalMemberId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public OrganizationMemberCriteria copy() {
        return new OrganizationMemberCriteria(this);
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

    public StringFilter getLegalName() {
        return legalName;
    }

    public Optional<StringFilter> optionalLegalName() {
        return Optional.ofNullable(legalName);
    }

    public StringFilter legalName() {
        if (legalName == null) {
            setLegalName(new StringFilter());
        }
        return legalName;
    }

    public void setLegalName(StringFilter legalName) {
        this.legalName = legalName;
    }

    public StringFilter getTradeName() {
        return tradeName;
    }

    public Optional<StringFilter> optionalTradeName() {
        return Optional.ofNullable(tradeName);
    }

    public StringFilter tradeName() {
        if (tradeName == null) {
            setTradeName(new StringFilter());
        }
        return tradeName;
    }

    public void setTradeName(StringFilter tradeName) {
        this.tradeName = tradeName;
    }

    public StringFilter getRegistrationNumber() {
        return registrationNumber;
    }

    public Optional<StringFilter> optionalRegistrationNumber() {
        return Optional.ofNullable(registrationNumber);
    }

    public StringFilter registrationNumber() {
        if (registrationNumber == null) {
            setRegistrationNumber(new StringFilter());
        }
        return registrationNumber;
    }

    public void setRegistrationNumber(StringFilter registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public StringFilter getTaxNumber() {
        return taxNumber;
    }

    public Optional<StringFilter> optionalTaxNumber() {
        return Optional.ofNullable(taxNumber);
    }

    public StringFilter taxNumber() {
        if (taxNumber == null) {
            setTaxNumber(new StringFilter());
        }
        return taxNumber;
    }

    public void setTaxNumber(StringFilter taxNumber) {
        this.taxNumber = taxNumber;
    }

    public StringFilter getLegalForm() {
        return legalForm;
    }

    public Optional<StringFilter> optionalLegalForm() {
        return Optional.ofNullable(legalForm);
    }

    public StringFilter legalForm() {
        if (legalForm == null) {
            setLegalForm(new StringFilter());
        }
        return legalForm;
    }

    public void setLegalForm(StringFilter legalForm) {
        this.legalForm = legalForm;
    }

    public LocalDateFilter getRegistrationDate() {
        return registrationDate;
    }

    public Optional<LocalDateFilter> optionalRegistrationDate() {
        return Optional.ofNullable(registrationDate);
    }

    public LocalDateFilter registrationDate() {
        if (registrationDate == null) {
            setRegistrationDate(new LocalDateFilter());
        }
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateFilter registrationDate) {
        this.registrationDate = registrationDate;
    }

    public StringFilter getEmail() {
        return email;
    }

    public Optional<StringFilter> optionalEmail() {
        return Optional.ofNullable(email);
    }

    public StringFilter email() {
        if (email == null) {
            setEmail(new StringFilter());
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
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

    public StringFilter getWebsite() {
        return website;
    }

    public Optional<StringFilter> optionalWebsite() {
        return Optional.ofNullable(website);
    }

    public StringFilter website() {
        if (website == null) {
            setWebsite(new StringFilter());
        }
        return website;
    }

    public void setWebsite(StringFilter website) {
        this.website = website;
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
        final OrganizationMemberCriteria that = (OrganizationMemberCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(legalName, that.legalName) &&
            Objects.equals(tradeName, that.tradeName) &&
            Objects.equals(registrationNumber, that.registrationNumber) &&
            Objects.equals(taxNumber, that.taxNumber) &&
            Objects.equals(legalForm, that.legalForm) &&
            Objects.equals(registrationDate, that.registrationDate) &&
            Objects.equals(email, that.email) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(website, that.website) &&
            Objects.equals(memberId, that.memberId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            legalName,
            tradeName,
            registrationNumber,
            taxNumber,
            legalForm,
            registrationDate,
            email,
            phoneNumber,
            website,
            memberId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrganizationMemberCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalLegalName().map(f -> "legalName=" + f + ", ").orElse("") +
            optionalTradeName().map(f -> "tradeName=" + f + ", ").orElse("") +
            optionalRegistrationNumber().map(f -> "registrationNumber=" + f + ", ").orElse("") +
            optionalTaxNumber().map(f -> "taxNumber=" + f + ", ").orElse("") +
            optionalLegalForm().map(f -> "legalForm=" + f + ", ").orElse("") +
            optionalRegistrationDate().map(f -> "registrationDate=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalPhoneNumber().map(f -> "phoneNumber=" + f + ", ").orElse("") +
            optionalWebsite().map(f -> "website=" + f + ", ").orElse("") +
            optionalMemberId().map(f -> "memberId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
