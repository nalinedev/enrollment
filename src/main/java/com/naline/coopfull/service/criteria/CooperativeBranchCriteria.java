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
 * Criteria class for the {@link com.naline.coopfull.domain.CooperativeBranch} entity. This class is used
 * in {@link com.naline.coopfull.web.rest.CooperativeBranchResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cooperative-branches?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CooperativeBranchCriteria implements Serializable, Criteria {

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

    private StringFilter description;

    private StringFilter phone;

    private StringFilter email;

    private CooperativeStatusFilter status;

    private LocalDateFilter openingDate;

    private LocalDateFilter closingDate;

    private LongFilter cooperativeId;

    private LongFilter locationId;

    private Boolean distinct;

    public CooperativeBranchCriteria() {}

    public CooperativeBranchCriteria(CooperativeBranchCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.phone = other.optionalPhone().map(StringFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(CooperativeStatusFilter::copy).orElse(null);
        this.openingDate = other.optionalOpeningDate().map(LocalDateFilter::copy).orElse(null);
        this.closingDate = other.optionalClosingDate().map(LocalDateFilter::copy).orElse(null);
        this.cooperativeId = other.optionalCooperativeId().map(LongFilter::copy).orElse(null);
        this.locationId = other.optionalLocationId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CooperativeBranchCriteria copy() {
        return new CooperativeBranchCriteria(this);
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

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
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

    public LocalDateFilter getOpeningDate() {
        return openingDate;
    }

    public Optional<LocalDateFilter> optionalOpeningDate() {
        return Optional.ofNullable(openingDate);
    }

    public LocalDateFilter openingDate() {
        if (openingDate == null) {
            setOpeningDate(new LocalDateFilter());
        }
        return openingDate;
    }

    public void setOpeningDate(LocalDateFilter openingDate) {
        this.openingDate = openingDate;
    }

    public LocalDateFilter getClosingDate() {
        return closingDate;
    }

    public Optional<LocalDateFilter> optionalClosingDate() {
        return Optional.ofNullable(closingDate);
    }

    public LocalDateFilter closingDate() {
        if (closingDate == null) {
            setClosingDate(new LocalDateFilter());
        }
        return closingDate;
    }

    public void setClosingDate(LocalDateFilter closingDate) {
        this.closingDate = closingDate;
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

    public LongFilter getLocationId() {
        return locationId;
    }

    public Optional<LongFilter> optionalLocationId() {
        return Optional.ofNullable(locationId);
    }

    public LongFilter locationId() {
        if (locationId == null) {
            setLocationId(new LongFilter());
        }
        return locationId;
    }

    public void setLocationId(LongFilter locationId) {
        this.locationId = locationId;
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
        final CooperativeBranchCriteria that = (CooperativeBranchCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(email, that.email) &&
            Objects.equals(status, that.status) &&
            Objects.equals(openingDate, that.openingDate) &&
            Objects.equals(closingDate, that.closingDate) &&
            Objects.equals(cooperativeId, that.cooperativeId) &&
            Objects.equals(locationId, that.locationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            code,
            name,
            description,
            phone,
            email,
            status,
            openingDate,
            closingDate,
            cooperativeId,
            locationId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CooperativeBranchCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCode().map(f -> "code=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalPhone().map(f -> "phone=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalOpeningDate().map(f -> "openingDate=" + f + ", ").orElse("") +
            optionalClosingDate().map(f -> "closingDate=" + f + ", ").orElse("") +
            optionalCooperativeId().map(f -> "cooperativeId=" + f + ", ").orElse("") +
            optionalLocationId().map(f -> "locationId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
