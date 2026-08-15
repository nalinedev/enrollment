package com.naline.coopfull.service.criteria;

import com.naline.coopfull.domain.enumeration.ProductionStatus;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.naline.coopfull.domain.AgriculturalProduction} entity. This class is used
 * in {@link com.naline.coopfull.web.rest.AgriculturalProductionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /agricultural-productions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AgriculturalProductionCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ProductionStatus
     */
    public static class ProductionStatusFilter extends Filter<ProductionStatus> {

        public ProductionStatusFilter() {}

        public ProductionStatusFilter(ProductionStatusFilter filter) {
            super(filter);
        }

        @Override
        public ProductionStatusFilter copy() {
            return new ProductionStatusFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter area;

    private StringFilter areaUnit;

    private LocalDateFilter plantingDate;

    private LocalDateFilter harvestStartDate;

    private LocalDateFilter harvestEndDate;

    private BigDecimalFilter productionQuantity;

    private StringFilter productionUnit;

    private BigDecimalFilter expectedAnnualProduction;

    private IntegerFilter numberOfPlants;

    private BigDecimalFilter plantingDensity;

    private IntegerFilter productionYear;

    private ProductionStatusFilter status;

    private LongFilter agriculturalActivityId;

    private LongFilter cropId;

    private LongFilter cropVarietyId;

    private Boolean distinct;

    public AgriculturalProductionCriteria() {}

    public AgriculturalProductionCriteria(AgriculturalProductionCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.area = other.optionalArea().map(BigDecimalFilter::copy).orElse(null);
        this.areaUnit = other.optionalAreaUnit().map(StringFilter::copy).orElse(null);
        this.plantingDate = other.optionalPlantingDate().map(LocalDateFilter::copy).orElse(null);
        this.harvestStartDate = other.optionalHarvestStartDate().map(LocalDateFilter::copy).orElse(null);
        this.harvestEndDate = other.optionalHarvestEndDate().map(LocalDateFilter::copy).orElse(null);
        this.productionQuantity = other.optionalProductionQuantity().map(BigDecimalFilter::copy).orElse(null);
        this.productionUnit = other.optionalProductionUnit().map(StringFilter::copy).orElse(null);
        this.expectedAnnualProduction = other.optionalExpectedAnnualProduction().map(BigDecimalFilter::copy).orElse(null);
        this.numberOfPlants = other.optionalNumberOfPlants().map(IntegerFilter::copy).orElse(null);
        this.plantingDensity = other.optionalPlantingDensity().map(BigDecimalFilter::copy).orElse(null);
        this.productionYear = other.optionalProductionYear().map(IntegerFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(ProductionStatusFilter::copy).orElse(null);
        this.agriculturalActivityId = other.optionalAgriculturalActivityId().map(LongFilter::copy).orElse(null);
        this.cropId = other.optionalCropId().map(LongFilter::copy).orElse(null);
        this.cropVarietyId = other.optionalCropVarietyId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AgriculturalProductionCriteria copy() {
        return new AgriculturalProductionCriteria(this);
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

    public BigDecimalFilter getArea() {
        return area;
    }

    public Optional<BigDecimalFilter> optionalArea() {
        return Optional.ofNullable(area);
    }

    public BigDecimalFilter area() {
        if (area == null) {
            setArea(new BigDecimalFilter());
        }
        return area;
    }

    public void setArea(BigDecimalFilter area) {
        this.area = area;
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

    public LocalDateFilter getPlantingDate() {
        return plantingDate;
    }

    public Optional<LocalDateFilter> optionalPlantingDate() {
        return Optional.ofNullable(plantingDate);
    }

    public LocalDateFilter plantingDate() {
        if (plantingDate == null) {
            setPlantingDate(new LocalDateFilter());
        }
        return plantingDate;
    }

    public void setPlantingDate(LocalDateFilter plantingDate) {
        this.plantingDate = plantingDate;
    }

    public LocalDateFilter getHarvestStartDate() {
        return harvestStartDate;
    }

    public Optional<LocalDateFilter> optionalHarvestStartDate() {
        return Optional.ofNullable(harvestStartDate);
    }

    public LocalDateFilter harvestStartDate() {
        if (harvestStartDate == null) {
            setHarvestStartDate(new LocalDateFilter());
        }
        return harvestStartDate;
    }

    public void setHarvestStartDate(LocalDateFilter harvestStartDate) {
        this.harvestStartDate = harvestStartDate;
    }

    public LocalDateFilter getHarvestEndDate() {
        return harvestEndDate;
    }

    public Optional<LocalDateFilter> optionalHarvestEndDate() {
        return Optional.ofNullable(harvestEndDate);
    }

    public LocalDateFilter harvestEndDate() {
        if (harvestEndDate == null) {
            setHarvestEndDate(new LocalDateFilter());
        }
        return harvestEndDate;
    }

    public void setHarvestEndDate(LocalDateFilter harvestEndDate) {
        this.harvestEndDate = harvestEndDate;
    }

    public BigDecimalFilter getProductionQuantity() {
        return productionQuantity;
    }

    public Optional<BigDecimalFilter> optionalProductionQuantity() {
        return Optional.ofNullable(productionQuantity);
    }

    public BigDecimalFilter productionQuantity() {
        if (productionQuantity == null) {
            setProductionQuantity(new BigDecimalFilter());
        }
        return productionQuantity;
    }

    public void setProductionQuantity(BigDecimalFilter productionQuantity) {
        this.productionQuantity = productionQuantity;
    }

    public StringFilter getProductionUnit() {
        return productionUnit;
    }

    public Optional<StringFilter> optionalProductionUnit() {
        return Optional.ofNullable(productionUnit);
    }

    public StringFilter productionUnit() {
        if (productionUnit == null) {
            setProductionUnit(new StringFilter());
        }
        return productionUnit;
    }

    public void setProductionUnit(StringFilter productionUnit) {
        this.productionUnit = productionUnit;
    }

    public BigDecimalFilter getExpectedAnnualProduction() {
        return expectedAnnualProduction;
    }

    public Optional<BigDecimalFilter> optionalExpectedAnnualProduction() {
        return Optional.ofNullable(expectedAnnualProduction);
    }

    public BigDecimalFilter expectedAnnualProduction() {
        if (expectedAnnualProduction == null) {
            setExpectedAnnualProduction(new BigDecimalFilter());
        }
        return expectedAnnualProduction;
    }

    public void setExpectedAnnualProduction(BigDecimalFilter expectedAnnualProduction) {
        this.expectedAnnualProduction = expectedAnnualProduction;
    }

    public IntegerFilter getNumberOfPlants() {
        return numberOfPlants;
    }

    public Optional<IntegerFilter> optionalNumberOfPlants() {
        return Optional.ofNullable(numberOfPlants);
    }

    public IntegerFilter numberOfPlants() {
        if (numberOfPlants == null) {
            setNumberOfPlants(new IntegerFilter());
        }
        return numberOfPlants;
    }

    public void setNumberOfPlants(IntegerFilter numberOfPlants) {
        this.numberOfPlants = numberOfPlants;
    }

    public BigDecimalFilter getPlantingDensity() {
        return plantingDensity;
    }

    public Optional<BigDecimalFilter> optionalPlantingDensity() {
        return Optional.ofNullable(plantingDensity);
    }

    public BigDecimalFilter plantingDensity() {
        if (plantingDensity == null) {
            setPlantingDensity(new BigDecimalFilter());
        }
        return plantingDensity;
    }

    public void setPlantingDensity(BigDecimalFilter plantingDensity) {
        this.plantingDensity = plantingDensity;
    }

    public IntegerFilter getProductionYear() {
        return productionYear;
    }

    public Optional<IntegerFilter> optionalProductionYear() {
        return Optional.ofNullable(productionYear);
    }

    public IntegerFilter productionYear() {
        if (productionYear == null) {
            setProductionYear(new IntegerFilter());
        }
        return productionYear;
    }

    public void setProductionYear(IntegerFilter productionYear) {
        this.productionYear = productionYear;
    }

    public ProductionStatusFilter getStatus() {
        return status;
    }

    public Optional<ProductionStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public ProductionStatusFilter status() {
        if (status == null) {
            setStatus(new ProductionStatusFilter());
        }
        return status;
    }

    public void setStatus(ProductionStatusFilter status) {
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

    public LongFilter getCropId() {
        return cropId;
    }

    public Optional<LongFilter> optionalCropId() {
        return Optional.ofNullable(cropId);
    }

    public LongFilter cropId() {
        if (cropId == null) {
            setCropId(new LongFilter());
        }
        return cropId;
    }

    public void setCropId(LongFilter cropId) {
        this.cropId = cropId;
    }

    public LongFilter getCropVarietyId() {
        return cropVarietyId;
    }

    public Optional<LongFilter> optionalCropVarietyId() {
        return Optional.ofNullable(cropVarietyId);
    }

    public LongFilter cropVarietyId() {
        if (cropVarietyId == null) {
            setCropVarietyId(new LongFilter());
        }
        return cropVarietyId;
    }

    public void setCropVarietyId(LongFilter cropVarietyId) {
        this.cropVarietyId = cropVarietyId;
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
        final AgriculturalProductionCriteria that = (AgriculturalProductionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(area, that.area) &&
            Objects.equals(areaUnit, that.areaUnit) &&
            Objects.equals(plantingDate, that.plantingDate) &&
            Objects.equals(harvestStartDate, that.harvestStartDate) &&
            Objects.equals(harvestEndDate, that.harvestEndDate) &&
            Objects.equals(productionQuantity, that.productionQuantity) &&
            Objects.equals(productionUnit, that.productionUnit) &&
            Objects.equals(expectedAnnualProduction, that.expectedAnnualProduction) &&
            Objects.equals(numberOfPlants, that.numberOfPlants) &&
            Objects.equals(plantingDensity, that.plantingDensity) &&
            Objects.equals(productionYear, that.productionYear) &&
            Objects.equals(status, that.status) &&
            Objects.equals(agriculturalActivityId, that.agriculturalActivityId) &&
            Objects.equals(cropId, that.cropId) &&
            Objects.equals(cropVarietyId, that.cropVarietyId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            area,
            areaUnit,
            plantingDate,
            harvestStartDate,
            harvestEndDate,
            productionQuantity,
            productionUnit,
            expectedAnnualProduction,
            numberOfPlants,
            plantingDensity,
            productionYear,
            status,
            agriculturalActivityId,
            cropId,
            cropVarietyId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AgriculturalProductionCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalArea().map(f -> "area=" + f + ", ").orElse("") +
            optionalAreaUnit().map(f -> "areaUnit=" + f + ", ").orElse("") +
            optionalPlantingDate().map(f -> "plantingDate=" + f + ", ").orElse("") +
            optionalHarvestStartDate().map(f -> "harvestStartDate=" + f + ", ").orElse("") +
            optionalHarvestEndDate().map(f -> "harvestEndDate=" + f + ", ").orElse("") +
            optionalProductionQuantity().map(f -> "productionQuantity=" + f + ", ").orElse("") +
            optionalProductionUnit().map(f -> "productionUnit=" + f + ", ").orElse("") +
            optionalExpectedAnnualProduction().map(f -> "expectedAnnualProduction=" + f + ", ").orElse("") +
            optionalNumberOfPlants().map(f -> "numberOfPlants=" + f + ", ").orElse("") +
            optionalPlantingDensity().map(f -> "plantingDensity=" + f + ", ").orElse("") +
            optionalProductionYear().map(f -> "productionYear=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalAgriculturalActivityId().map(f -> "agriculturalActivityId=" + f + ", ").orElse("") +
            optionalCropId().map(f -> "cropId=" + f + ", ").orElse("") +
            optionalCropVarietyId().map(f -> "cropVarietyId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
