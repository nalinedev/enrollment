package com.naline.coopfull.service.criteria;

import com.naline.coopfull.domain.enumeration.CooperativeRoleStatus;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.naline.coopfull.domain.CooperativeRole} entity. This class is used
 * in {@link com.naline.coopfull.web.rest.CooperativeRoleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cooperative-roles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CooperativeRoleCriteria implements Serializable, Criteria {

    /**
     * Class for filtering CooperativeRoleStatus
     */
    public static class CooperativeRoleStatusFilter extends Filter<CooperativeRoleStatus> {

        public CooperativeRoleStatusFilter() {}

        public CooperativeRoleStatusFilter(CooperativeRoleStatusFilter filter) {
            super(filter);
        }

        @Override
        public CooperativeRoleStatusFilter copy() {
            return new CooperativeRoleStatusFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private StringFilter name;

    private CooperativeRoleStatusFilter status;

    private LongFilter permissionsId;

    private Boolean distinct;

    public CooperativeRoleCriteria() {}

    public CooperativeRoleCriteria(CooperativeRoleCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(CooperativeRoleStatusFilter::copy).orElse(null);
        this.permissionsId = other.optionalPermissionsId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CooperativeRoleCriteria copy() {
        return new CooperativeRoleCriteria(this);
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

    public CooperativeRoleStatusFilter getStatus() {
        return status;
    }

    public Optional<CooperativeRoleStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public CooperativeRoleStatusFilter status() {
        if (status == null) {
            setStatus(new CooperativeRoleStatusFilter());
        }
        return status;
    }

    public void setStatus(CooperativeRoleStatusFilter status) {
        this.status = status;
    }

    public LongFilter getPermissionsId() {
        return permissionsId;
    }

    public Optional<LongFilter> optionalPermissionsId() {
        return Optional.ofNullable(permissionsId);
    }

    public LongFilter permissionsId() {
        if (permissionsId == null) {
            setPermissionsId(new LongFilter());
        }
        return permissionsId;
    }

    public void setPermissionsId(LongFilter permissionsId) {
        this.permissionsId = permissionsId;
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
        final CooperativeRoleCriteria that = (CooperativeRoleCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(name, that.name) &&
            Objects.equals(status, that.status) &&
            Objects.equals(permissionsId, that.permissionsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name, status, permissionsId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CooperativeRoleCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCode().map(f -> "code=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalPermissionsId().map(f -> "permissionsId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
