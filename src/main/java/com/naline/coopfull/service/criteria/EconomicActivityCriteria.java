package com.naline.coopfull.service.criteria;

import com.naline.coopfull.domain.enumeration.EconomicActivityStatus;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.naline.coopfull.domain.EconomicActivity} entity. This class is used
 * in {@link com.naline.coopfull.web.rest.EconomicActivityResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /economic-activities?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EconomicActivityCriteria implements Serializable, Criteria {

    /**
     * Class for filtering EconomicActivityStatus
     */
    public static class EconomicActivityStatusFilter extends Filter<EconomicActivityStatus> {

        public EconomicActivityStatusFilter() {}

        public EconomicActivityStatusFilter(EconomicActivityStatusFilter filter) {
            super(filter);
        }

        @Override
        public EconomicActivityStatusFilter copy() {
            return new EconomicActivityStatusFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private BooleanFilter mainActivity;

    private LocalDateFilter startDate;

    private LocalDateFilter endDate;

    private BigDecimalFilter annualRevenue;

    private BigDecimalFilter monthlyRevenue;

    private IntegerFilter numberOfEmployees;

    private EconomicActivityStatusFilter status;

    private LongFilter agriculturalActivityId;

    private LongFilter livestockActivityId;

    private LongFilter aquacultureActivityId;

    private LongFilter memberId;

    private LongFilter activityTypeId;

    private LongFilter locationId;

    private Boolean distinct;

    public EconomicActivityCriteria() {}

    public EconomicActivityCriteria(EconomicActivityCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.mainActivity = other.optionalMainActivity().map(BooleanFilter::copy).orElse(null);
        this.startDate = other.optionalStartDate().map(LocalDateFilter::copy).orElse(null);
        this.endDate = other.optionalEndDate().map(LocalDateFilter::copy).orElse(null);
        this.annualRevenue = other.optionalAnnualRevenue().map(BigDecimalFilter::copy).orElse(null);
        this.monthlyRevenue = other.optionalMonthlyRevenue().map(BigDecimalFilter::copy).orElse(null);
        this.numberOfEmployees = other.optionalNumberOfEmployees().map(IntegerFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(EconomicActivityStatusFilter::copy).orElse(null);
        this.agriculturalActivityId = other.optionalAgriculturalActivityId().map(LongFilter::copy).orElse(null);
        this.livestockActivityId = other.optionalLivestockActivityId().map(LongFilter::copy).orElse(null);
        this.aquacultureActivityId = other.optionalAquacultureActivityId().map(LongFilter::copy).orElse(null);
        this.memberId = other.optionalMemberId().map(LongFilter::copy).orElse(null);
        this.activityTypeId = other.optionalActivityTypeId().map(LongFilter::copy).orElse(null);
        this.locationId = other.optionalLocationId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public EconomicActivityCriteria copy() {
        return new EconomicActivityCriteria(this);
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

    public BooleanFilter getMainActivity() {
        return mainActivity;
    }

    public Optional<BooleanFilter> optionalMainActivity() {
        return Optional.ofNullable(mainActivity);
    }

    public BooleanFilter mainActivity() {
        if (mainActivity == null) {
            setMainActivity(new BooleanFilter());
        }
        return mainActivity;
    }

    public void setMainActivity(BooleanFilter mainActivity) {
        this.mainActivity = mainActivity;
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

    public BigDecimalFilter getAnnualRevenue() {
        return annualRevenue;
    }

    public Optional<BigDecimalFilter> optionalAnnualRevenue() {
        return Optional.ofNullable(annualRevenue);
    }

    public BigDecimalFilter annualRevenue() {
        if (annualRevenue == null) {
            setAnnualRevenue(new BigDecimalFilter());
        }
        return annualRevenue;
    }

    public void setAnnualRevenue(BigDecimalFilter annualRevenue) {
        this.annualRevenue = annualRevenue;
    }

    public BigDecimalFilter getMonthlyRevenue() {
        return monthlyRevenue;
    }

    public Optional<BigDecimalFilter> optionalMonthlyRevenue() {
        return Optional.ofNullable(monthlyRevenue);
    }

    public BigDecimalFilter monthlyRevenue() {
        if (monthlyRevenue == null) {
            setMonthlyRevenue(new BigDecimalFilter());
        }
        return monthlyRevenue;
    }

    public void setMonthlyRevenue(BigDecimalFilter monthlyRevenue) {
        this.monthlyRevenue = monthlyRevenue;
    }

    public IntegerFilter getNumberOfEmployees() {
        return numberOfEmployees;
    }

    public Optional<IntegerFilter> optionalNumberOfEmployees() {
        return Optional.ofNullable(numberOfEmployees);
    }

    public IntegerFilter numberOfEmployees() {
        if (numberOfEmployees == null) {
            setNumberOfEmployees(new IntegerFilter());
        }
        return numberOfEmployees;
    }

    public void setNumberOfEmployees(IntegerFilter numberOfEmployees) {
        this.numberOfEmployees = numberOfEmployees;
    }

    public EconomicActivityStatusFilter getStatus() {
        return status;
    }

    public Optional<EconomicActivityStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public EconomicActivityStatusFilter status() {
        if (status == null) {
            setStatus(new EconomicActivityStatusFilter());
        }
        return status;
    }

    public void setStatus(EconomicActivityStatusFilter status) {
        this.status = status;
    }

    public LongFilter getAgriculturalActivityId() {
        return agriculturalActivityId;
    }

    public Optional<LongFilter> optionalAgriculturalActivityId() {
        return Optional.ofNullable(agriculturalActivityId);
    }

    public LongFilter agriculturalActivityId() {
        if (agriculturalActivityId == null) {
            setAgriculturalActivityId(new LongFilter());
        }
        return agriculturalActivityId;
    }

    public void setAgriculturalActivityId(LongFilter agriculturalActivityId) {
        this.agriculturalActivityId = agriculturalActivityId;
    }

    public LongFilter getLivestockActivityId() {
        return livestockActivityId;
    }

    public Optional<LongFilter> optionalLivestockActivityId() {
        return Optional.ofNullable(livestockActivityId);
    }

    public LongFilter livestockActivityId() {
        if (livestockActivityId == null) {
            setLivestockActivityId(new LongFilter());
        }
        return livestockActivityId;
    }

    public void setLivestockActivityId(LongFilter livestockActivityId) {
        this.livestockActivityId = livestockActivityId;
    }

    public LongFilter getAquacultureActivityId() {
        return aquacultureActivityId;
    }

    public Optional<LongFilter> optionalAquacultureActivityId() {
        return Optional.ofNullable(aquacultureActivityId);
    }

    public LongFilter aquacultureActivityId() {
        if (aquacultureActivityId == null) {
            setAquacultureActivityId(new LongFilter());
        }
        return aquacultureActivityId;
    }

    public void setAquacultureActivityId(LongFilter aquacultureActivityId) {
        this.aquacultureActivityId = aquacultureActivityId;
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

    public LongFilter getActivityTypeId() {
        return activityTypeId;
    }

    public Optional<LongFilter> optionalActivityTypeId() {
        return Optional.ofNullable(activityTypeId);
    }

    public LongFilter activityTypeId() {
        if (activityTypeId == null) {
            setActivityTypeId(new LongFilter());
        }
        return activityTypeId;
    }

    public void setActivityTypeId(LongFilter activityTypeId) {
        this.activityTypeId = activityTypeId;
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
        final EconomicActivityCriteria that = (EconomicActivityCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(mainActivity, that.mainActivity) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(annualRevenue, that.annualRevenue) &&
            Objects.equals(monthlyRevenue, that.monthlyRevenue) &&
            Objects.equals(numberOfEmployees, that.numberOfEmployees) &&
            Objects.equals(status, that.status) &&
            Objects.equals(agriculturalActivityId, that.agriculturalActivityId) &&
            Objects.equals(livestockActivityId, that.livestockActivityId) &&
            Objects.equals(aquacultureActivityId, that.aquacultureActivityId) &&
            Objects.equals(memberId, that.memberId) &&
            Objects.equals(activityTypeId, that.activityTypeId) &&
            Objects.equals(locationId, that.locationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            mainActivity,
            startDate,
            endDate,
            annualRevenue,
            monthlyRevenue,
            numberOfEmployees,
            status,
            agriculturalActivityId,
            livestockActivityId,
            aquacultureActivityId,
            memberId,
            activityTypeId,
            locationId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EconomicActivityCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalMainActivity().map(f -> "mainActivity=" + f + ", ").orElse("") +
            optionalStartDate().map(f -> "startDate=" + f + ", ").orElse("") +
            optionalEndDate().map(f -> "endDate=" + f + ", ").orElse("") +
            optionalAnnualRevenue().map(f -> "annualRevenue=" + f + ", ").orElse("") +
            optionalMonthlyRevenue().map(f -> "monthlyRevenue=" + f + ", ").orElse("") +
            optionalNumberOfEmployees().map(f -> "numberOfEmployees=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalAgriculturalActivityId().map(f -> "agriculturalActivityId=" + f + ", ").orElse("") +
            optionalLivestockActivityId().map(f -> "livestockActivityId=" + f + ", ").orElse("") +
            optionalAquacultureActivityId().map(f -> "aquacultureActivityId=" + f + ", ").orElse("") +
            optionalMemberId().map(f -> "memberId=" + f + ", ").orElse("") +
            optionalActivityTypeId().map(f -> "activityTypeId=" + f + ", ").orElse("") +
            optionalLocationId().map(f -> "locationId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
