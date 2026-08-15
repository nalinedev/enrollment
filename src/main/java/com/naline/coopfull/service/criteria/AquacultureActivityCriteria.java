package com.naline.coopfull.service.criteria;

import com.naline.coopfull.domain.enumeration.AquacultureOwnershipType;
import com.naline.coopfull.domain.enumeration.AquacultureProductionMode;
import com.naline.coopfull.domain.enumeration.AquacultureProductionType;
import com.naline.coopfull.domain.enumeration.AquacultureStatus;
import com.naline.coopfull.domain.enumeration.AquacultureSystemType;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.naline.coopfull.domain.AquacultureActivity} entity. This class is used
 * in {@link com.naline.coopfull.web.rest.AquacultureActivityResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /aquaculture-activities?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AquacultureActivityCriteria implements Serializable, Criteria {

    /**
     * Class for filtering AquacultureProductionMode
     */
    public static class AquacultureProductionModeFilter extends Filter<AquacultureProductionMode> {

        public AquacultureProductionModeFilter() {}

        public AquacultureProductionModeFilter(AquacultureProductionModeFilter filter) {
            super(filter);
        }

        @Override
        public AquacultureProductionModeFilter copy() {
            return new AquacultureProductionModeFilter(this);
        }
    }

    /**
     * Class for filtering AquacultureOwnershipType
     */
    public static class AquacultureOwnershipTypeFilter extends Filter<AquacultureOwnershipType> {

        public AquacultureOwnershipTypeFilter() {}

        public AquacultureOwnershipTypeFilter(AquacultureOwnershipTypeFilter filter) {
            super(filter);
        }

        @Override
        public AquacultureOwnershipTypeFilter copy() {
            return new AquacultureOwnershipTypeFilter(this);
        }
    }

    /**
     * Class for filtering AquacultureProductionType
     */
    public static class AquacultureProductionTypeFilter extends Filter<AquacultureProductionType> {

        public AquacultureProductionTypeFilter() {}

        public AquacultureProductionTypeFilter(AquacultureProductionTypeFilter filter) {
            super(filter);
        }

        @Override
        public AquacultureProductionTypeFilter copy() {
            return new AquacultureProductionTypeFilter(this);
        }
    }

    /**
     * Class for filtering AquacultureSystemType
     */
    public static class AquacultureSystemTypeFilter extends Filter<AquacultureSystemType> {

        public AquacultureSystemTypeFilter() {}

        public AquacultureSystemTypeFilter(AquacultureSystemTypeFilter filter) {
            super(filter);
        }

        @Override
        public AquacultureSystemTypeFilter copy() {
            return new AquacultureSystemTypeFilter(this);
        }
    }

    /**
     * Class for filtering AquacultureStatus
     */
    public static class AquacultureStatusFilter extends Filter<AquacultureStatus> {

        public AquacultureStatusFilter() {}

        public AquacultureStatusFilter(AquacultureStatusFilter filter) {
            super(filter);
        }

        @Override
        public AquacultureStatusFilter copy() {
            return new AquacultureStatusFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private AquacultureProductionModeFilter productionMode;

    private AquacultureOwnershipTypeFilter ownershipType;

    private AquacultureProductionTypeFilter productionType;

    private AquacultureSystemTypeFilter systemType;

    private LocalDateFilter startDate;

    private BigDecimalFilter totalArea;

    private StringFilter areaUnit;

    private StringFilter waterSource;

    private IntegerFilter numberOfProductionUnits;

    private StringFilter productionUnitDescription;

    private AquacultureStatusFilter status;

    private BigDecimalFilter annualRevenue;

    private BigDecimalFilter monthlyRevenue;

    private IntegerFilter employees;

    private StringFilter certification;

    private LongFilter locationId;

    private LongFilter aquaticSpeciesId;

    private LongFilter economicActivityId;

    private LongFilter productionsId;

    private Boolean distinct;

    public AquacultureActivityCriteria() {}

    public AquacultureActivityCriteria(AquacultureActivityCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.productionMode = other.optionalProductionMode().map(AquacultureProductionModeFilter::copy).orElse(null);
        this.ownershipType = other.optionalOwnershipType().map(AquacultureOwnershipTypeFilter::copy).orElse(null);
        this.productionType = other.optionalProductionType().map(AquacultureProductionTypeFilter::copy).orElse(null);
        this.systemType = other.optionalSystemType().map(AquacultureSystemTypeFilter::copy).orElse(null);
        this.startDate = other.optionalStartDate().map(LocalDateFilter::copy).orElse(null);
        this.totalArea = other.optionalTotalArea().map(BigDecimalFilter::copy).orElse(null);
        this.areaUnit = other.optionalAreaUnit().map(StringFilter::copy).orElse(null);
        this.waterSource = other.optionalWaterSource().map(StringFilter::copy).orElse(null);
        this.numberOfProductionUnits = other.optionalNumberOfProductionUnits().map(IntegerFilter::copy).orElse(null);
        this.productionUnitDescription = other.optionalProductionUnitDescription().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(AquacultureStatusFilter::copy).orElse(null);
        this.annualRevenue = other.optionalAnnualRevenue().map(BigDecimalFilter::copy).orElse(null);
        this.monthlyRevenue = other.optionalMonthlyRevenue().map(BigDecimalFilter::copy).orElse(null);
        this.employees = other.optionalEmployees().map(IntegerFilter::copy).orElse(null);
        this.certification = other.optionalCertification().map(StringFilter::copy).orElse(null);
        this.locationId = other.optionalLocationId().map(LongFilter::copy).orElse(null);
        this.aquaticSpeciesId = other.optionalAquaticSpeciesId().map(LongFilter::copy).orElse(null);
        this.economicActivityId = other.optionalEconomicActivityId().map(LongFilter::copy).orElse(null);
        this.productionsId = other.optionalProductionsId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AquacultureActivityCriteria copy() {
        return new AquacultureActivityCriteria(this);
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

    public AquacultureProductionModeFilter getProductionMode() {
        return productionMode;
    }

    public Optional<AquacultureProductionModeFilter> optionalProductionMode() {
        return Optional.ofNullable(productionMode);
    }

    public AquacultureProductionModeFilter productionMode() {
        if (productionMode == null) {
            setProductionMode(new AquacultureProductionModeFilter());
        }
        return productionMode;
    }

    public void setProductionMode(AquacultureProductionModeFilter productionMode) {
        this.productionMode = productionMode;
    }

    public AquacultureOwnershipTypeFilter getOwnershipType() {
        return ownershipType;
    }

    public Optional<AquacultureOwnershipTypeFilter> optionalOwnershipType() {
        return Optional.ofNullable(ownershipType);
    }

    public AquacultureOwnershipTypeFilter ownershipType() {
        if (ownershipType == null) {
            setOwnershipType(new AquacultureOwnershipTypeFilter());
        }
        return ownershipType;
    }

    public void setOwnershipType(AquacultureOwnershipTypeFilter ownershipType) {
        this.ownershipType = ownershipType;
    }

    public AquacultureProductionTypeFilter getProductionType() {
        return productionType;
    }

    public Optional<AquacultureProductionTypeFilter> optionalProductionType() {
        return Optional.ofNullable(productionType);
    }

    public AquacultureProductionTypeFilter productionType() {
        if (productionType == null) {
            setProductionType(new AquacultureProductionTypeFilter());
        }
        return productionType;
    }

    public void setProductionType(AquacultureProductionTypeFilter productionType) {
        this.productionType = productionType;
    }

    public AquacultureSystemTypeFilter getSystemType() {
        return systemType;
    }

    public Optional<AquacultureSystemTypeFilter> optionalSystemType() {
        return Optional.ofNullable(systemType);
    }

    public AquacultureSystemTypeFilter systemType() {
        if (systemType == null) {
            setSystemType(new AquacultureSystemTypeFilter());
        }
        return systemType;
    }

    public void setSystemType(AquacultureSystemTypeFilter systemType) {
        this.systemType = systemType;
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

    public IntegerFilter getNumberOfProductionUnits() {
        return numberOfProductionUnits;
    }

    public Optional<IntegerFilter> optionalNumberOfProductionUnits() {
        return Optional.ofNullable(numberOfProductionUnits);
    }

    public IntegerFilter numberOfProductionUnits() {
        if (numberOfProductionUnits == null) {
            setNumberOfProductionUnits(new IntegerFilter());
        }
        return numberOfProductionUnits;
    }

    public void setNumberOfProductionUnits(IntegerFilter numberOfProductionUnits) {
        this.numberOfProductionUnits = numberOfProductionUnits;
    }

    public StringFilter getProductionUnitDescription() {
        return productionUnitDescription;
    }

    public Optional<StringFilter> optionalProductionUnitDescription() {
        return Optional.ofNullable(productionUnitDescription);
    }

    public StringFilter productionUnitDescription() {
        if (productionUnitDescription == null) {
            setProductionUnitDescription(new StringFilter());
        }
        return productionUnitDescription;
    }

    public void setProductionUnitDescription(StringFilter productionUnitDescription) {
        this.productionUnitDescription = productionUnitDescription;
    }

    public AquacultureStatusFilter getStatus() {
        return status;
    }

    public Optional<AquacultureStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public AquacultureStatusFilter status() {
        if (status == null) {
            setStatus(new AquacultureStatusFilter());
        }
        return status;
    }

    public void setStatus(AquacultureStatusFilter status) {
        this.status = status;
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

    public LongFilter getAquaticSpeciesId() {
        return aquaticSpeciesId;
    }

    public Optional<LongFilter> optionalAquaticSpeciesId() {
        return Optional.ofNullable(aquaticSpeciesId);
    }

    public LongFilter aquaticSpeciesId() {
        if (aquaticSpeciesId == null) {
            setAquaticSpeciesId(new LongFilter());
        }
        return aquaticSpeciesId;
    }

    public void setAquaticSpeciesId(LongFilter aquaticSpeciesId) {
        this.aquaticSpeciesId = aquaticSpeciesId;
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
        final AquacultureActivityCriteria that = (AquacultureActivityCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(productionMode, that.productionMode) &&
            Objects.equals(ownershipType, that.ownershipType) &&
            Objects.equals(productionType, that.productionType) &&
            Objects.equals(systemType, that.systemType) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(totalArea, that.totalArea) &&
            Objects.equals(areaUnit, that.areaUnit) &&
            Objects.equals(waterSource, that.waterSource) &&
            Objects.equals(numberOfProductionUnits, that.numberOfProductionUnits) &&
            Objects.equals(productionUnitDescription, that.productionUnitDescription) &&
            Objects.equals(status, that.status) &&
            Objects.equals(annualRevenue, that.annualRevenue) &&
            Objects.equals(monthlyRevenue, that.monthlyRevenue) &&
            Objects.equals(employees, that.employees) &&
            Objects.equals(certification, that.certification) &&
            Objects.equals(locationId, that.locationId) &&
            Objects.equals(aquaticSpeciesId, that.aquaticSpeciesId) &&
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
            systemType,
            startDate,
            totalArea,
            areaUnit,
            waterSource,
            numberOfProductionUnits,
            productionUnitDescription,
            status,
            annualRevenue,
            monthlyRevenue,
            employees,
            certification,
            locationId,
            aquaticSpeciesId,
            economicActivityId,
            productionsId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AquacultureActivityCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalProductionMode().map(f -> "productionMode=" + f + ", ").orElse("") +
            optionalOwnershipType().map(f -> "ownershipType=" + f + ", ").orElse("") +
            optionalProductionType().map(f -> "productionType=" + f + ", ").orElse("") +
            optionalSystemType().map(f -> "systemType=" + f + ", ").orElse("") +
            optionalStartDate().map(f -> "startDate=" + f + ", ").orElse("") +
            optionalTotalArea().map(f -> "totalArea=" + f + ", ").orElse("") +
            optionalAreaUnit().map(f -> "areaUnit=" + f + ", ").orElse("") +
            optionalWaterSource().map(f -> "waterSource=" + f + ", ").orElse("") +
            optionalNumberOfProductionUnits().map(f -> "numberOfProductionUnits=" + f + ", ").orElse("") +
            optionalProductionUnitDescription().map(f -> "productionUnitDescription=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalAnnualRevenue().map(f -> "annualRevenue=" + f + ", ").orElse("") +
            optionalMonthlyRevenue().map(f -> "monthlyRevenue=" + f + ", ").orElse("") +
            optionalEmployees().map(f -> "employees=" + f + ", ").orElse("") +
            optionalCertification().map(f -> "certification=" + f + ", ").orElse("") +
            optionalLocationId().map(f -> "locationId=" + f + ", ").orElse("") +
            optionalAquaticSpeciesId().map(f -> "aquaticSpeciesId=" + f + ", ").orElse("") +
            optionalEconomicActivityId().map(f -> "economicActivityId=" + f + ", ").orElse("") +
            optionalProductionsId().map(f -> "productionsId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
