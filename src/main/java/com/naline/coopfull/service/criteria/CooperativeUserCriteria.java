package com.naline.coopfull.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.naline.coopfull.domain.CooperativeUser} entity. This class is used
 * in {@link com.naline.coopfull.web.rest.CooperativeUserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cooperative-users?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CooperativeUserCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter startDate;

    private LocalDateFilter endDate;

    private BooleanFilter active;

    private LongFilter appUserId;

    private LongFilter cooperativeId;

    private LongFilter roleId;

    private Boolean distinct;

    public CooperativeUserCriteria() {}

    public CooperativeUserCriteria(CooperativeUserCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.startDate = other.optionalStartDate().map(LocalDateFilter::copy).orElse(null);
        this.endDate = other.optionalEndDate().map(LocalDateFilter::copy).orElse(null);
        this.active = other.optionalActive().map(BooleanFilter::copy).orElse(null);
        this.appUserId = other.optionalAppUserId().map(LongFilter::copy).orElse(null);
        this.cooperativeId = other.optionalCooperativeId().map(LongFilter::copy).orElse(null);
        this.roleId = other.optionalRoleId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CooperativeUserCriteria copy() {
        return new CooperativeUserCriteria(this);
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

    public LocalDateFilter getStartDate() {
        return startDate;
    }

    public Optional<LocalDateFilter> optionalStartDate() {
        return Optional.ofNullable(startDate);
    }

    public LocalDateFilter startDate() {
        if (startDate == null) {
            setStartDate(new LocalDateFilter());
        }
        return startDate;
    }

    public void setStartDate(LocalDateFilter startDate) {
        this.startDate = startDate;
    }

    public LocalDateFilter getEndDate() {
        return endDate;
    }

    public Optional<LocalDateFilter> optionalEndDate() {
        return Optional.ofNullable(endDate);
    }

    public LocalDateFilter endDate() {
        if (endDate == null) {
            setEndDate(new LocalDateFilter());
        }
        return endDate;
    }

    public void setEndDate(LocalDateFilter endDate) {
        this.endDate = endDate;
    }

    public BooleanFilter getActive() {
        return active;
    }

    public Optional<BooleanFilter> optionalActive() {
        return Optional.ofNullable(active);
    }

    public BooleanFilter active() {
        if (active == null) {
            setActive(new BooleanFilter());
        }
        return active;
    }

    public void setActive(BooleanFilter active) {
        this.active = active;
    }

    public LongFilter getAppUserId() {
        return appUserId;
    }

    public Optional<LongFilter> optionalAppUserId() {
        return Optional.ofNullable(appUserId);
    }

    public LongFilter appUserId() {
        if (appUserId == null) {
            setAppUserId(new LongFilter());
        }
        return appUserId;
    }

    public void setAppUserId(LongFilter appUserId) {
        this.appUserId = appUserId;
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

    public LongFilter getRoleId() {
        return roleId;
    }

    public Optional<LongFilter> optionalRoleId() {
        return Optional.ofNullable(roleId);
    }

    public LongFilter roleId() {
        if (roleId == null) {
            setRoleId(new LongFilter());
        }
        return roleId;
    }

    public void setRoleId(LongFilter roleId) {
        this.roleId = roleId;
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
        final CooperativeUserCriteria that = (CooperativeUserCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(active, that.active) &&
            Objects.equals(appUserId, that.appUserId) &&
            Objects.equals(cooperativeId, that.cooperativeId) &&
            Objects.equals(roleId, that.roleId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startDate, endDate, active, appUserId, cooperativeId, roleId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CooperativeUserCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalStartDate().map(f -> "startDate=" + f + ", ").orElse("") +
            optionalEndDate().map(f -> "endDate=" + f + ", ").orElse("") +
            optionalActive().map(f -> "active=" + f + ", ").orElse("") +
            optionalAppUserId().map(f -> "appUserId=" + f + ", ").orElse("") +
            optionalCooperativeId().map(f -> "cooperativeId=" + f + ", ").orElse("") +
            optionalRoleId().map(f -> "roleId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
