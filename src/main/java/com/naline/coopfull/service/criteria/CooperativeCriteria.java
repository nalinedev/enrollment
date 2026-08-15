package com.naline.coopfull.service.criteria;

import com.naline.coopfull.domain.enumeration.CooperativeStatus;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.naline.coopfull.domain.Cooperative} entity. This class is used
 * in {@link com.naline.coopfull.web.rest.CooperativeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cooperatives?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CooperativeCriteria implements Serializable, Criteria {

    /**
     * Class for filtering CooperativeStatus
     */
    public static class CooperativeStatusFilter extends Filter<CooperativeStatus> {

        public CooperativeStatusFilter() {}

        public CooperativeStatusFilter(CooperativeStatusFilter filter) {
            super(filter);
        }

        @Override
        public CooperativeStatusFilter copy() {
            return new CooperativeStatusFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private StringFilter name;

    private StringFilter legalName;

    private StringFilter registrationNumber;

    private StringFilter taxNumber;

    private CooperativeStatusFilter status;

    private LocalDateFilter foundedDate;

    private StringFilter email;

    private StringFilter phone;

    private StringFilter website;

    private InstantFilter createdDate;

    private InstantFilter lastModifiedDate;

    private Boolean distinct;

    public CooperativeCriteria() {}

    public CooperativeCriteria(CooperativeCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.legalName = other.optionalLegalName().map(StringFilter::copy).orElse(null);
        this.registrationNumber = other.optionalRegistrationNumber().map(StringFilter::copy).orElse(null);
        this.taxNumber = other.optionalTaxNumber().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(CooperativeStatusFilter::copy).orElse(null);
        this.foundedDate = other.optionalFoundedDate().map(LocalDateFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.phone = other.optionalPhone().map(StringFilter::copy).orElse(null);
        this.website = other.optionalWebsite().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CooperativeCriteria copy() {
        return new CooperativeCriteria(this);
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

    public StringFilter getCode() {
        return code;
    }

    public Optional<StringFilter> optionalCode() {
        return Optional.ofNullable(code);
    }

    public StringFilter code() {
        if (code == null) {
            setCode(new StringFilter());
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
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

    public CooperativeStatusFilter getStatus() {
        return status;
    }

    public Optional<CooperativeStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public CooperativeStatusFilter status() {
        if (status == null) {
            setStatus(new CooperativeStatusFilter());
        }
        return status;
    }

    public void setStatus(CooperativeStatusFilter status) {
        this.status = status;
    }

    public LocalDateFilter getFoundedDate() {
        return foundedDate;
    }

    public Optional<LocalDateFilter> optionalFoundedDate() {
        return Optional.ofNullable(foundedDate);
    }

    public LocalDateFilter foundedDate() {
        if (foundedDate == null) {
            setFoundedDate(new LocalDateFilter());
        }
        return foundedDate;
    }

    public void setFoundedDate(LocalDateFilter foundedDate) {
        this.foundedDate = foundedDate;
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

    public StringFilter getPhone() {
        return phone;
    }

    public Optional<StringFilter> optionalPhone() {
        return Optional.ofNullable(phone);
    }

    public StringFilter phone() {
        if (phone == null) {
            setPhone(new StringFilter());
        }
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
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
        final CooperativeCriteria that = (CooperativeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(name, that.name) &&
            Objects.equals(legalName, that.legalName) &&
            Objects.equals(registrationNumber, that.registrationNumber) &&
            Objects.equals(taxNumber, that.taxNumber) &&
            Objects.equals(status, that.status) &&
            Objects.equals(foundedDate, that.foundedDate) &&
            Objects.equals(email, that.email) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(website, that.website) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            code,
            name,
            legalName,
            registrationNumber,
            taxNumber,
            status,
            foundedDate,
            email,
            phone,
            website,
            createdDate,
            lastModifiedDate,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CooperativeCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCode().map(f -> "code=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalLegalName().map(f -> "legalName=" + f + ", ").orElse("") +
            optionalRegistrationNumber().map(f -> "registrationNumber=" + f + ", ").orElse("") +
            optionalTaxNumber().map(f -> "taxNumber=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalFoundedDate().map(f -> "foundedDate=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalPhone().map(f -> "phone=" + f + ", ").orElse("") +
            optionalWebsite().map(f -> "website=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
