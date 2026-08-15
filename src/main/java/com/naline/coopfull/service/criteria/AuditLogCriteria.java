package com.naline.coopfull.service.criteria;

import com.naline.coopfull.domain.enumeration.AuditAction;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.naline.coopfull.domain.AuditLog} entity. This class is used
 * in {@link com.naline.coopfull.web.rest.AuditLogResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /audit-logs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AuditLogCriteria implements Serializable, Criteria {

    /**
     * Class for filtering AuditAction
     */
    public static class AuditActionFilter extends Filter<AuditAction> {

        public AuditActionFilter() {}

        public AuditActionFilter(AuditActionFilter filter) {
            super(filter);
        }

        @Override
        public AuditActionFilter copy() {
            return new AuditActionFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private AuditActionFilter action;

    private StringFilter entityName;

    private StringFilter entityId;

    private StringFilter username;

    private LongFilter cooperativeId;

    private LongFilter branchId;

    private InstantFilter timestamp;

    private StringFilter ipAddress;

    private StringFilter userAgent;

    private LongFilter appUserId;

    private Boolean distinct;

    public AuditLogCriteria() {}

    public AuditLogCriteria(AuditLogCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.action = other.optionalAction().map(AuditActionFilter::copy).orElse(null);
        this.entityName = other.optionalEntityName().map(StringFilter::copy).orElse(null);
        this.entityId = other.optionalEntityId().map(StringFilter::copy).orElse(null);
        this.username = other.optionalUsername().map(StringFilter::copy).orElse(null);
        this.cooperativeId = other.optionalCooperativeId().map(LongFilter::copy).orElse(null);
        this.branchId = other.optionalBranchId().map(LongFilter::copy).orElse(null);
        this.timestamp = other.optionalTimestamp().map(InstantFilter::copy).orElse(null);
        this.ipAddress = other.optionalIpAddress().map(StringFilter::copy).orElse(null);
        this.userAgent = other.optionalUserAgent().map(StringFilter::copy).orElse(null);
        this.appUserId = other.optionalAppUserId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AuditLogCriteria copy() {
        return new AuditLogCriteria(this);
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

    public AuditActionFilter getAction() {
        return action;
    }

    public Optional<AuditActionFilter> optionalAction() {
        return Optional.ofNullable(action);
    }

    public AuditActionFilter action() {
        if (action == null) {
            setAction(new AuditActionFilter());
        }
        return action;
    }

    public void setAction(AuditActionFilter action) {
        this.action = action;
    }

    public StringFilter getEntityName() {
        return entityName;
    }

    public Optional<StringFilter> optionalEntityName() {
        return Optional.ofNullable(entityName);
    }

    public StringFilter entityName() {
        if (entityName == null) {
            setEntityName(new StringFilter());
        }
        return entityName;
    }

    public void setEntityName(StringFilter entityName) {
        this.entityName = entityName;
    }

    public StringFilter getEntityId() {
        return entityId;
    }

    public Optional<StringFilter> optionalEntityId() {
        return Optional.ofNullable(entityId);
    }

    public StringFilter entityId() {
        if (entityId == null) {
            setEntityId(new StringFilter());
        }
        return entityId;
    }

    public void setEntityId(StringFilter entityId) {
        this.entityId = entityId;
    }

    public StringFilter getUsername() {
        return username;
    }

    public Optional<StringFilter> optionalUsername() {
        return Optional.ofNullable(username);
    }

    public StringFilter username() {
        if (username == null) {
            setUsername(new StringFilter());
        }
        return username;
    }

    public void setUsername(StringFilter username) {
        this.username = username;
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

    public InstantFilter getTimestamp() {
        return timestamp;
    }

    public Optional<InstantFilter> optionalTimestamp() {
        return Optional.ofNullable(timestamp);
    }

    public InstantFilter timestamp() {
        if (timestamp == null) {
            setTimestamp(new InstantFilter());
        }
        return timestamp;
    }

    public void setTimestamp(InstantFilter timestamp) {
        this.timestamp = timestamp;
    }

    public StringFilter getIpAddress() {
        return ipAddress;
    }

    public Optional<StringFilter> optionalIpAddress() {
        return Optional.ofNullable(ipAddress);
    }

    public StringFilter ipAddress() {
        if (ipAddress == null) {
            setIpAddress(new StringFilter());
        }
        return ipAddress;
    }

    public void setIpAddress(StringFilter ipAddress) {
        this.ipAddress = ipAddress;
    }

    public StringFilter getUserAgent() {
        return userAgent;
    }

    public Optional<StringFilter> optionalUserAgent() {
        return Optional.ofNullable(userAgent);
    }

    public StringFilter userAgent() {
        if (userAgent == null) {
            setUserAgent(new StringFilter());
        }
        return userAgent;
    }

    public void setUserAgent(StringFilter userAgent) {
        this.userAgent = userAgent;
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
        final AuditLogCriteria that = (AuditLogCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(action, that.action) &&
            Objects.equals(entityName, that.entityName) &&
            Objects.equals(entityId, that.entityId) &&
            Objects.equals(username, that.username) &&
            Objects.equals(cooperativeId, that.cooperativeId) &&
            Objects.equals(branchId, that.branchId) &&
            Objects.equals(timestamp, that.timestamp) &&
            Objects.equals(ipAddress, that.ipAddress) &&
            Objects.equals(userAgent, that.userAgent) &&
            Objects.equals(appUserId, that.appUserId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            action,
            entityName,
            entityId,
            username,
            cooperativeId,
            branchId,
            timestamp,
            ipAddress,
            userAgent,
            appUserId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AuditLogCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalAction().map(f -> "action=" + f + ", ").orElse("") +
            optionalEntityName().map(f -> "entityName=" + f + ", ").orElse("") +
            optionalEntityId().map(f -> "entityId=" + f + ", ").orElse("") +
            optionalUsername().map(f -> "username=" + f + ", ").orElse("") +
            optionalCooperativeId().map(f -> "cooperativeId=" + f + ", ").orElse("") +
            optionalBranchId().map(f -> "branchId=" + f + ", ").orElse("") +
            optionalTimestamp().map(f -> "timestamp=" + f + ", ").orElse("") +
            optionalIpAddress().map(f -> "ipAddress=" + f + ", ").orElse("") +
            optionalUserAgent().map(f -> "userAgent=" + f + ", ").orElse("") +
            optionalAppUserId().map(f -> "appUserId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
