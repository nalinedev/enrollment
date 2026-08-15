package com.naline.coopfull.service.criteria;

import com.naline.coopfull.domain.enumeration.LivestockOwnershipType;
import com.naline.coopfull.domain.enumeration.LivestockProductionMode;
import com.naline.coopfull.domain.enumeration.LivestockProductionType;
import com.naline.coopfull.domain.enumeration.LivestockStatus;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.naline.coopfull.domain.LivestockActivity} entity. This class is used
 * in {@link com.naline.coopfull.web.rest.LivestockActivityResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /livestock-activities?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LivestockActivityCriteria implements Serializable, Criteria {

    /**
     * Class for filtering LivestockProductionMode
     */
    public static class LivestockProductionModeFilter extends Filter<LivestockProductionMode> {

        public LivestockProductionModeFilter() {}

        public LivestockProductionModeFilter(LivestockProductionModeFilter filter) {
            super(filter);
        }

        @Override
        public LivestockProductionModeFilter copy() {
            return new LivestockProductionModeFilter(this);
        }
    }

    /**
     * Class for filtering LivestockOwnershipType
     */
    public static class LivestockOwnershipTypeFilter extends Filter<LivestockOwnershipType> {

        public LivestockOwnershipTypeFilter() {}

        public LivestockOwnershipTypeFilter(LivestockOwnershipTypeFilter filter) {
            super(filter);
        }

        @Override
        public LivestockOwnershipTypeFilter copy() {
            return new LivestockOwnershipTypeFilter(this);
        }
    }

    /**
     * Class for filtering LivestockProductionType
     */
    public static class LivestockProductionTypeFilter extends Filter<LivestockProductionType> {

        public LivestockProductionTypeFilter() {}

        public LivestockProductionTypeFilter(LivestockProductionTypeFilter filter) {
            super(filter);
        }

        @Override
        public LivestockProductionTypeFilter copy() {
            return new LivestockProductionTypeFilter(this);
        }
    }

    /**
     * Class for filtering LivestockStatus
     */
    public static class LivestockStatusFilter extends Filter<LivestockStatus> {

        public LivestockStatusFilter() {}

        public LivestockStatusFilter(LivestockStatusFilter filter) {
            super(filter);
        }

        @Override
        public LivestockStatusFilter copy() {
            return new LivestockStatusFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LivestockProductionModeFilter productionMode;

    private LivestockOwnershipTypeFilter ownershipType;

    private LivestockProductionTypeFilter productionType;

    private LocalDateFilter startDate;

    private BigDecimalFilter totalArea;

    private StringFilter areaUnit;

    private LivestockStatusFilter status;

    private IntegerFilter numberOfAnimals;

    private BigDecimalFilter annualRevenue;

    private BigDecimalFilter monthlyRevenue;

    private IntegerFilter employees;

    private BooleanFilter veterinaryServiceAvailable;

    private StringFilter feedSource;

    private StringFilter waterSource;

    private StringFilter certification;

    private LongFilter locationId;

    private LongFilter livestockTypeId;

    private LongFilter economicActivityId;

    private LongFilter productionsId;

    private Boolean distinct;

    public LivestockActivityCriteria() {}

    public LivestockActivityCriteria(LivestockActivityCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.productionMode = other.optionalProductionMode().map(LivestockProductionModeFilter::copy).orElse(null);
        this.ownershipType = other.optionalOwnershipType().map(LivestockOwnershipTypeFilter::copy).orElse(null);
        this.productionType = other.optionalProductionType().map(LivestockProductionTypeFilter::copy).orElse(null);
        this.startDate = other.optionalStartDate().map(LocalDateFilter::copy).orElse(null);
        this.totalArea = other.optionalTotalArea().map(BigDecimalFilter::copy).orElse(null);
        this.areaUnit = other.optionalAreaUnit().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(LivestockStatusFilter::copy).orElse(null);
        this.numberOfAnimals = other.optionalNumberOfAnimals().map(IntegerFilter::copy).orElse(null);
        this.annualRevenue = other.optionalAnnualRevenue().map(BigDecimalFilter::copy).orElse(null);
        this.monthlyRevenue = other.optionalMonthlyRevenue().map(BigDecimalFilter::copy).orElse(null);
        this.employees = other.optionalEmployees().map(IntegerFilter::copy).orElse(null);
        this.veterinaryServiceAvailable = other.optionalVeterinaryServiceAvailable().map(BooleanFilter::copy).orElse(null);
        this.feedSource = other.optionalFeedSource().map(StringFilter::copy).orElse(null);
        this.waterSource = other.optionalWaterSource().map(StringFilter::copy).orElse(null);
        this.certification = other.optionalCertification().map(StringFilter::copy).orElse(null);
        this.locationId = other.optionalLocationId().map(LongFilter::copy).orElse(null);
        this.livestockTypeId = other.optionalLivestockTypeId().map(LongFilter::copy).orElse(null);
        this.economicActivityId = other.optionalEconomicActivityId().map(LongFilter::copy).orElse(null);
        this.productionsId = other.optionalProductionsId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public LivestockActivityCriteria copy() {
        return new LivestockActivityCriteria(this);
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

    public LivestockProductionModeFilter getProductionMode() {
        return productionMode;
    }

    public Optional<LivestockProductionModeFilter> optionalProductionMode() {
        return Optional.ofNullable(productionMode);
    }

    public LivestockProductionModeFilter productionMode() {
        if (productionMode == null) {
            setProductionMode(new LivestockProductionModeFilter());
        }
        return productionMode;
    }

    public void setProductionMode(LivestockProductionModeFilter productionMode) {
        this.productionMode = productionMode;
    }

    public LivestockOwnershipTypeFilter getOwnershipType() {
        return ownershipType;
    }

    public Optional<LivestockOwnershipTypeFilter> optionalOwnershipType() {
        return Optional.ofNullable(ownershipType);
    }

    public LivestockOwnershipTypeFilter ownershipType() {
        if (ownershipType == null) {
            setOwnershipType(new LivestockOwnershipTypeFilter());
        }
        return ownershipType;
    }

    public void setOwnershipType(LivestockOwnershipTypeFilter ownershipType) {
        this.ownershipType = ownershipType;
    }

    public LivestockProductionTypeFilter getProductionType() {
        return productionType;
    }

    public Optional<LivestockProductionTypeFilter> optionalProductionType() {
        return Optional.ofNullable(productionType);
    }

    public LivestockProductionTypeFilter productionType() {
        if (productionType == null) {
            setProductionType(new LivestockProductionTypeFilter());
        }
        return productionType;
    }

    public void setProductionType(LivestockProductionTypeFilter productionType) {
        this.productionType = productionType;
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

    public BigDecimalFilter getTotalArea() {
        return totalArea;
    }

    public Optional<BigDecimalFilter> optionalTotalArea() {
        return Optional.ofNullable(totalArea);
    }

    public BigDecimalFilter totalArea() {
        if (totalArea == null) {
            setTotalArea(new BigDecimalFilter());
        }
        return totalArea;
    }

    public void setTotalArea(BigDecimalFilter totalArea) {
        this.totalArea = totalArea;
    }

    public StringFilter getAreaUnit() {
        return areaUnit;
    }

    public Optional<StringFilter> optionalAreaUnit() {
        return Optional.ofNullable(areaUnit);
    }

    public StringFilter areaUnit() {
        if (areaUnit == null) {
            setAreaUnit(new StringFilter());
        }
        return areaUnit;
    }

    public void setAreaUnit(StringFilter areaUnit) {
        this.areaUnit = areaUnit;
    }

    public LivestockStatusFilter getStatus() {
        return status;
    }

    public Optional<LivestockStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public LivestockStatusFilter status() {
        if (status == null) {
            setStatus(new LivestockStatusFilter());
        }
        return status;
    }

    public void setStatus(LivestockStatusFilter status) {
        this.status = status;
    }

    public IntegerFilter getNumberOfAnimals() {
        return numberOfAnimals;
    }

    public Optional<IntegerFilter> optionalNumberOfAnimals() {
        return Optional.ofNullable(numberOfAnimals);
    }

    public IntegerFilter numberOfAnimals() {
        if (numberOfAnimals == null) {
            setNumberOfAnimals(new IntegerFilter());
        }
        return numberOfAnimals;
    }

    public void setNumberOfAnimals(IntegerFilter numberOfAnimals) {
        this.numberOfAnimals = numberOfAnimals;
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

    public IntegerFilter getEmployees() {
        return employees;
    }

    public Optional<IntegerFilter> optionalEmployees() {
        return Optional.ofNullable(employees);
    }

    public IntegerFilter employees() {
        if (employees == null) {
            setEmployees(new IntegerFilter());
        }
        return employees;
    }

    public void setEmployees(IntegerFilter employees) {
        this.employees = employees;
    }

    public BooleanFilter getVeterinaryServiceAvailable() {
        return veterinaryServiceAvailable;
    }

    public Optional<BooleanFilter> optionalVeterinaryServiceAvailable() {
        return Optional.ofNullable(veterinaryServiceAvailable);
    }

    public BooleanFilter veterinaryServiceAvailable() {
        if (veterinaryServiceAvailable == null) {
            setVeterinaryServiceAvailable(new BooleanFilter());
        }
        return veterinaryServiceAvailable;
    }

    public void setVeterinaryServiceAvailable(BooleanFilter veterinaryServiceAvailable) {
        this.veterinaryServiceAvailable = veterinaryServiceAvailable;
    }

    public StringFilter getFeedSource() {
        return feedSource;
    }

    public Optional<StringFilter> optionalFeedSource() {
        return Optional.ofNullable(feedSource);
    }

    public StringFilter feedSource() {
        if (feedSource == null) {
            setFeedSource(new StringFilter());
        }
        return feedSource;
    }

    public void setFeedSource(StringFilter feedSource) {
        this.feedSource = feedSource;
    }

    public StringFilter getWaterSource() {
        return waterSource;
    }

    public Optional<StringFilter> optionalWaterSource() {
        return Optional.ofNullable(waterSource);
    }

    public StringFilter waterSource() {
        if (waterSource == null) {
            setWaterSource(new StringFilter());
        }
        return waterSource;
    }

    public void setWaterSource(StringFilter waterSource) {
        this.waterSource = waterSource;
    }

    public StringFilter getCertification() {
        return certification;
    }

    public Optional<StringFilter> optionalCertification() {
        return Optional.ofNullable(certification);
    }

    public StringFilter certification() {
        if (certification == null) {
            setCertification(new StringFilter());
        }
        return certification;
    }

    public void setCertification(StringFilter certification) {
        this.certification = certification;
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

    public LongFilter getLivestockTypeId() {
        return livestockTypeId;
    }

    public Optional<LongFilter> optionalLivestockTypeId() {
        return Optional.ofNullable(livestockTypeId);
    }

    public LongFilter livestockTypeId() {
        if (livestockTypeId == null) {
            setLivestockTypeId(new LongFilter());
        }
        return livestockTypeId;
    }

    public void setLivestockTypeId(LongFilter livestockTypeId) {
        this.livestockTypeId = livestockTypeId;
    }

    public LongFilter getEconomicActivityId() {
        return economicActivityId;
    }

    public Optional<LongFilter> optionalEconomicActivityId() {
        return Optional.ofNullable(economicActivityId);
    }

    public LongFilter economicActivityId() {
        if (economicActivityId == null) {
            setEconomicActivityId(new LongFilter());
        }
        return economicActivityId;
    }

    public void setEconomicActivityId(LongFilter economicActivityId) {
        this.economicActivityId = economicActivityId;
    }

    public LongFilter getProductionsId() {
        return productionsId;
    }

    public Optional<LongFilter> optionalProductionsId() {
        return Optional.ofNullable(productionsId);
    }

    public LongFilter productionsId() {
        if (productionsId == null) {
            setProductionsId(new LongFilter());
        }
        return productionsId;
    }

    public void setProductionsId(LongFilter productionsId) {
        this.productionsId = productionsId;
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
        final LivestockActivityCriteria that = (LivestockActivityCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(productionMode, that.productionMode) &&
            Objects.equals(ownershipType, that.ownershipType) &&
            Objects.equals(productionType, that.productionType) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(totalArea, that.totalArea) &&
            Objects.equals(areaUnit, that.areaUnit) &&
            Objects.equals(status, that.status) &&
            Objects.equals(numberOfAnimals, that.numberOfAnimals) &&
            Objects.equals(annualRevenue, that.annualRevenue) &&
            Objects.equals(monthlyRevenue, that.monthlyRevenue) &&
            Objects.equals(employees, that.employees) &&
            Objects.equals(veterinaryServiceAvailable, that.veterinaryServiceAvailable) &&
            Objects.equals(feedSource, that.feedSource) &&
            Objects.equals(waterSource, that.waterSource) &&
            Objects.equals(certification, that.certification) &&
            Objects.equals(locationId, that.locationId) &&
            Objects.equals(livestockTypeId, that.livestockTypeId) &&
            Objects.equals(economicActivityId, that.economicActivityId) &&
            Objects.equals(productionsId, that.productionsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            productionMode,
            ownershipType,
            productionType,
            startDate,
            totalArea,
            areaUnit,
            status,
            numberOfAnimals,
            annualRevenue,
            monthlyRevenue,
            employees,
            veterinaryServiceAvailable,
            feedSource,
            waterSource,
            certification,
            locationId,
            livestockTypeId,
            economicActivityId,
            productionsId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LivestockActivityCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalProductionMode().map(f -> "productionMode=" + f + ", ").orElse("") +
            optionalOwnershipType().map(f -> "ownershipType=" + f + ", ").orElse("") +
            optionalProductionType().map(f -> "productionType=" + f + ", ").orElse("") +
            optionalStartDate().map(f -> "startDate=" + f + ", ").orElse("") +
            optionalTotalArea().map(f -> "totalArea=" + f + ", ").orElse("") +
            optionalAreaUnit().map(f -> "areaUnit=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalNumberOfAnimals().map(f -> "numberOfAnimals=" + f + ", ").orElse("") +
            optionalAnnualRevenue().map(f -> "annualRevenue=" + f + ", ").orElse("") +
            optionalMonthlyRevenue().map(f -> "monthlyRevenue=" + f + ", ").orElse("") +
            optionalEmployees().map(f -> "employees=" + f + ", ").orElse("") +
            optionalVeterinaryServiceAvailable().map(f -> "veterinaryServiceAvailable=" + f + ", ").orElse("") +
            optionalFeedSource().map(f -> "feedSource=" + f + ", ").orElse("") +
            optionalWaterSource().map(f -> "waterSource=" + f + ", ").orElse("") +
            optionalCertification().map(f -> "certification=" + f + ", ").orElse("") +
            optionalLocationId().map(f -> "locationId=" + f + ", ").orElse("") +
            optionalLivestockTypeId().map(f -> "livestockTypeId=" + f + ", ").orElse("") +
            optionalEconomicActivityId().map(f -> "economicActivityId=" + f + ", ").orElse("") +
            optionalProductionsId().map(f -> "productionsId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
