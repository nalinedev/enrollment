package com.naline.coopfull.service.criteria;

import com.naline.coopfull.domain.enumeration.AquacultureProductionStatus;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.naline.coopfull.domain.AquacultureProduction} entity. This class is used
 * in {@link com.naline.coopfull.web.rest.AquacultureProductionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /aquaculture-productions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AquacultureProductionCriteria implements Serializable, Criteria {

    /**
     * Class for filtering AquacultureProductionStatus
     */
    public static class AquacultureProductionStatusFilter extends Filter<AquacultureProductionStatus> {

        public AquacultureProductionStatusFilter() {}

        public AquacultureProductionStatusFilter(AquacultureProductionStatusFilter filter) {
            super(filter);
        }

        @Override
        public AquacultureProductionStatusFilter copy() {
            return new AquacultureProductionStatusFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter productionDate;

    private IntegerFilter numberOfAnimals;

    private BigDecimalFilter stockingDensity;

    private BigDecimalFilter productionQuantity;

    private StringFilter productionUnit;

    private BigDecimalFilter averageWeightGrams;

    private IntegerFilter mortalityCount;

    private IntegerFilter stockingCount;

    private IntegerFilter harvestedCount;

    private BigDecimalFilter expectedProduction;

    private LocalDateFilter expectedHarvestDate;

    private LocalDateFilter actualHarvestDate;

    private AquacultureProductionStatusFilter status;

    private LongFilter aquacultureActivityId;

    private Boolean distinct;

    public AquacultureProductionCriteria() {}

    public AquacultureProductionCriteria(AquacultureProductionCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.productionDate = other.optionalProductionDate().map(LocalDateFilter::copy).orElse(null);
        this.numberOfAnimals = other.optionalNumberOfAnimals().map(IntegerFilter::copy).orElse(null);
        this.stockingDensity = other.optionalStockingDensity().map(BigDecimalFilter::copy).orElse(null);
        this.productionQuantity = other.optionalProductionQuantity().map(BigDecimalFilter::copy).orElse(null);
        this.productionUnit = other.optionalProductionUnit().map(StringFilter::copy).orElse(null);
        this.averageWeightGrams = other.optionalAverageWeightGrams().map(BigDecimalFilter::copy).orElse(null);
        this.mortalityCount = other.optionalMortalityCount().map(IntegerFilter::copy).orElse(null);
        this.stockingCount = other.optionalStockingCount().map(IntegerFilter::copy).orElse(null);
        this.harvestedCount = other.optionalHarvestedCount().map(IntegerFilter::copy).orElse(null);
        this.expectedProduction = other.optionalExpectedProduction().map(BigDecimalFilter::copy).orElse(null);
        this.expectedHarvestDate = other.optionalExpectedHarvestDate().map(LocalDateFilter::copy).orElse(null);
        this.actualHarvestDate = other.optionalActualHarvestDate().map(LocalDateFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(AquacultureProductionStatusFilter::copy).orElse(null);
        this.aquacultureActivityId = other.optionalAquacultureActivityId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AquacultureProductionCriteria copy() {
        return new AquacultureProductionCriteria(this);
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

    public LocalDateFilter getProductionDate() {
        return productionDate;
    }

    public Optional<LocalDateFilter> optionalProductionDate() {
        return Optional.ofNullable(productionDate);
    }

    public LocalDateFilter productionDate() {
        if (productionDate == null) {
            setProductionDate(new LocalDateFilter());
        }
        return productionDate;
    }

    public void setProductionDate(LocalDateFilter productionDate) {
        this.productionDate = productionDate;
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

    public BigDecimalFilter getStockingDensity() {
        return stockingDensity;
    }

    public Optional<BigDecimalFilter> optionalStockingDensity() {
        return Optional.ofNullable(stockingDensity);
    }

    public BigDecimalFilter stockingDensity() {
        if (stockingDensity == null) {
            setStockingDensity(new BigDecimalFilter());
        }
        return stockingDensity;
    }

    public void setStockingDensity(BigDecimalFilter stockingDensity) {
        this.stockingDensity = stockingDensity;
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

    public BigDecimalFilter getAverageWeightGrams() {
        return averageWeightGrams;
    }

    public Optional<BigDecimalFilter> optionalAverageWeightGrams() {
        return Optional.ofNullable(averageWeightGrams);
    }

    public BigDecimalFilter averageWeightGrams() {
        if (averageWeightGrams == null) {
            setAverageWeightGrams(new BigDecimalFilter());
        }
        return averageWeightGrams;
    }

    public void setAverageWeightGrams(BigDecimalFilter averageWeightGrams) {
        this.averageWeightGrams = averageWeightGrams;
    }

    public IntegerFilter getMortalityCount() {
        return mortalityCount;
    }

    public Optional<IntegerFilter> optionalMortalityCount() {
        return Optional.ofNullable(mortalityCount);
    }

    public IntegerFilter mortalityCount() {
        if (mortalityCount == null) {
            setMortalityCount(new IntegerFilter());
        }
        return mortalityCount;
    }

    public void setMortalityCount(IntegerFilter mortalityCount) {
        this.mortalityCount = mortalityCount;
    }

    public IntegerFilter getStockingCount() {
        return stockingCount;
    }

    public Optional<IntegerFilter> optionalStockingCount() {
        return Optional.ofNullable(stockingCount);
    }

    public IntegerFilter stockingCount() {
        if (stockingCount == null) {
            setStockingCount(new IntegerFilter());
        }
        return stockingCount;
    }

    public void setStockingCount(IntegerFilter stockingCount) {
        this.stockingCount = stockingCount;
    }

    public IntegerFilter getHarvestedCount() {
        return harvestedCount;
    }

    public Optional<IntegerFilter> optionalHarvestedCount() {
        return Optional.ofNullable(harvestedCount);
    }

    public IntegerFilter harvestedCount() {
        if (harvestedCount == null) {
            setHarvestedCount(new IntegerFilter());
        }
        return harvestedCount;
    }

    public void setHarvestedCount(IntegerFilter harvestedCount) {
        this.harvestedCount = harvestedCount;
    }

    public BigDecimalFilter getExpectedProduction() {
        return expectedProduction;
    }

    public Optional<BigDecimalFilter> optionalExpectedProduction() {
        return Optional.ofNullable(expectedProduction);
    }

    public BigDecimalFilter expectedProduction() {
        if (expectedProduction == null) {
            setExpectedProduction(new BigDecimalFilter());
        }
        return expectedProduction;
    }

    public void setExpectedProduction(BigDecimalFilter expectedProduction) {
        this.expectedProduction = expectedProduction;
    }

    public LocalDateFilter getExpectedHarvestDate() {
        return expectedHarvestDate;
    }

    public Optional<LocalDateFilter> optionalExpectedHarvestDate() {
        return Optional.ofNullable(expectedHarvestDate);
    }

    public LocalDateFilter expectedHarvestDate() {
        if (expectedHarvestDate == null) {
            setExpectedHarvestDate(new LocalDateFilter());
        }
        return expectedHarvestDate;
    }

    public void setExpectedHarvestDate(LocalDateFilter expectedHarvestDate) {
        this.expectedHarvestDate = expectedHarvestDate;
    }

    public LocalDateFilter getActualHarvestDate() {
        return actualHarvestDate;
    }

    public Optional<LocalDateFilter> optionalActualHarvestDate() {
        return Optional.ofNullable(actualHarvestDate);
    }

    public LocalDateFilter actualHarvestDate() {
        if (actualHarvestDate == null) {
            setActualHarvestDate(new LocalDateFilter());
        }
        return actualHarvestDate;
    }

    public void setActualHarvestDate(LocalDateFilter actualHarvestDate) {
        this.actualHarvestDate = actualHarvestDate;
    }

    public AquacultureProductionStatusFilter getStatus() {
        return status;
    }

    public Optional<AquacultureProductionStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public AquacultureProductionStatusFilter status() {
        if (status == null) {
            setStatus(new AquacultureProductionStatusFilter());
        }
        return status;
    }

    public void setStatus(AquacultureProductionStatusFilter status) {
        this.status = status;
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
        final AquacultureProductionCriteria that = (AquacultureProductionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(productionDate, that.productionDate) &&
            Objects.equals(numberOfAnimals, that.numberOfAnimals) &&
            Objects.equals(stockingDensity, that.stockingDensity) &&
            Objects.equals(productionQuantity, that.productionQuantity) &&
            Objects.equals(productionUnit, that.productionUnit) &&
            Objects.equals(averageWeightGrams, that.averageWeightGrams) &&
            Objects.equals(mortalityCount, that.mortalityCount) &&
            Objects.equals(stockingCount, that.stockingCount) &&
            Objects.equals(harvestedCount, that.harvestedCount) &&
            Objects.equals(expectedProduction, that.expectedProduction) &&
            Objects.equals(expectedHarvestDate, that.expectedHarvestDate) &&
            Objects.equals(actualHarvestDate, that.actualHarvestDate) &&
            Objects.equals(status, that.status) &&
            Objects.equals(aquacultureActivityId, that.aquacultureActivityId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            productionDate,
            numberOfAnimals,
            stockingDensity,
            productionQuantity,
            productionUnit,
            averageWeightGrams,
            mortalityCount,
            stockingCount,
            harvestedCount,
            expectedProduction,
            expectedHarvestDate,
            actualHarvestDate,
            status,
            aquacultureActivityId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AquacultureProductionCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalProductionDate().map(f -> "productionDate=" + f + ", ").orElse("") +
            optionalNumberOfAnimals().map(f -> "numberOfAnimals=" + f + ", ").orElse("") +
            optionalStockingDensity().map(f -> "stockingDensity=" + f + ", ").orElse("") +
            optionalProductionQuantity().map(f -> "productionQuantity=" + f + ", ").orElse("") +
            optionalProductionUnit().map(f -> "productionUnit=" + f + ", ").orElse("") +
            optionalAverageWeightGrams().map(f -> "averageWeightGrams=" + f + ", ").orElse("") +
            optionalMortalityCount().map(f -> "mortalityCount=" + f + ", ").orElse("") +
            optionalStockingCount().map(f -> "stockingCount=" + f + ", ").orElse("") +
            optionalHarvestedCount().map(f -> "harvestedCount=" + f + ", ").orElse("") +
            optionalExpectedProduction().map(f -> "expectedProduction=" + f + ", ").orElse("") +
            optionalExpectedHarvestDate().map(f -> "expectedHarvestDate=" + f + ", ").orElse("") +
            optionalActualHarvestDate().map(f -> "actualHarvestDate=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalAquacultureActivityId().map(f -> "aquacultureActivityId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
