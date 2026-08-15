package com.naline.coopfull.service.criteria;

import com.naline.coopfull.domain.enumeration.AnimalProductionStatus;
import com.naline.coopfull.domain.enumeration.AnimalSex;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.naline.coopfull.domain.LivestockProduction} entity. This class is used
 * in {@link com.naline.coopfull.web.rest.LivestockProductionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /livestock-productions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LivestockProductionCriteria implements Serializable, Criteria {

    /**
     * Class for filtering AnimalSex
     */
    public static class AnimalSexFilter extends Filter<AnimalSex> {

        public AnimalSexFilter() {}

        public AnimalSexFilter(AnimalSexFilter filter) {
            super(filter);
        }

        @Override
        public AnimalSexFilter copy() {
            return new AnimalSexFilter(this);
        }
    }

    /**
     * Class for filtering AnimalProductionStatus
     */
    public static class AnimalProductionStatusFilter extends Filter<AnimalProductionStatus> {

        public AnimalProductionStatusFilter() {}

        public AnimalProductionStatusFilter(AnimalProductionStatusFilter filter) {
            super(filter);
        }

        @Override
        public AnimalProductionStatusFilter copy() {
            return new AnimalProductionStatusFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter productionDate;

    private AnimalSexFilter animalSex;

    private IntegerFilter numberOfAnimals;

    private IntegerFilter averageAgeMonths;

    private BigDecimalFilter averageWeightKg;

    private BigDecimalFilter productionQuantity;

    private StringFilter productionUnit;

    private IntegerFilter mortalityCount;

    private IntegerFilter birthCount;

    private IntegerFilter soldCount;

    private AnimalProductionStatusFilter status;

    private LongFilter livestockActivityId;

    private Boolean distinct;

    public LivestockProductionCriteria() {}

    public LivestockProductionCriteria(LivestockProductionCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.productionDate = other.optionalProductionDate().map(LocalDateFilter::copy).orElse(null);
        this.animalSex = other.optionalAnimalSex().map(AnimalSexFilter::copy).orElse(null);
        this.numberOfAnimals = other.optionalNumberOfAnimals().map(IntegerFilter::copy).orElse(null);
        this.averageAgeMonths = other.optionalAverageAgeMonths().map(IntegerFilter::copy).orElse(null);
        this.averageWeightKg = other.optionalAverageWeightKg().map(BigDecimalFilter::copy).orElse(null);
        this.productionQuantity = other.optionalProductionQuantity().map(BigDecimalFilter::copy).orElse(null);
        this.productionUnit = other.optionalProductionUnit().map(StringFilter::copy).orElse(null);
        this.mortalityCount = other.optionalMortalityCount().map(IntegerFilter::copy).orElse(null);
        this.birthCount = other.optionalBirthCount().map(IntegerFilter::copy).orElse(null);
        this.soldCount = other.optionalSoldCount().map(IntegerFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(AnimalProductionStatusFilter::copy).orElse(null);
        this.livestockActivityId = other.optionalLivestockActivityId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public LivestockProductionCriteria copy() {
        return new LivestockProductionCriteria(this);
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

    public AnimalSexFilter getAnimalSex() {
        return animalSex;
    }

    public Optional<AnimalSexFilter> optionalAnimalSex() {
        return Optional.ofNullable(animalSex);
    }

    public AnimalSexFilter animalSex() {
        if (animalSex == null) {
            setAnimalSex(new AnimalSexFilter());
        }
        return animalSex;
    }

    public void setAnimalSex(AnimalSexFilter animalSex) {
        this.animalSex = animalSex;
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

    public IntegerFilter getAverageAgeMonths() {
        return averageAgeMonths;
    }

    public Optional<IntegerFilter> optionalAverageAgeMonths() {
        return Optional.ofNullable(averageAgeMonths);
    }

    public IntegerFilter averageAgeMonths() {
        if (averageAgeMonths == null) {
            setAverageAgeMonths(new IntegerFilter());
        }
        return averageAgeMonths;
    }

    public void setAverageAgeMonths(IntegerFilter averageAgeMonths) {
        this.averageAgeMonths = averageAgeMonths;
    }

    public BigDecimalFilter getAverageWeightKg() {
        return averageWeightKg;
    }

    public Optional<BigDecimalFilter> optionalAverageWeightKg() {
        return Optional.ofNullable(averageWeightKg);
    }

    public BigDecimalFilter averageWeightKg() {
        if (averageWeightKg == null) {
            setAverageWeightKg(new BigDecimalFilter());
        }
        return averageWeightKg;
    }

    public void setAverageWeightKg(BigDecimalFilter averageWeightKg) {
        this.averageWeightKg = averageWeightKg;
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

    public IntegerFilter getBirthCount() {
        return birthCount;
    }

    public Optional<IntegerFilter> optionalBirthCount() {
        return Optional.ofNullable(birthCount);
    }

    public IntegerFilter birthCount() {
        if (birthCount == null) {
            setBirthCount(new IntegerFilter());
        }
        return birthCount;
    }

    public void setBirthCount(IntegerFilter birthCount) {
        this.birthCount = birthCount;
    }

    public IntegerFilter getSoldCount() {
        return soldCount;
    }

    public Optional<IntegerFilter> optionalSoldCount() {
        return Optional.ofNullable(soldCount);
    }

    public IntegerFilter soldCount() {
        if (soldCount == null) {
            setSoldCount(new IntegerFilter());
        }
        return soldCount;
    }

    public void setSoldCount(IntegerFilter soldCount) {
        this.soldCount = soldCount;
    }

    public AnimalProductionStatusFilter getStatus() {
        return status;
    }

    public Optional<AnimalProductionStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public AnimalProductionStatusFilter status() {
        if (status == null) {
            setStatus(new AnimalProductionStatusFilter());
        }
        return status;
    }

    public void setStatus(AnimalProductionStatusFilter status) {
        this.status = status;
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
        final LivestockProductionCriteria that = (LivestockProductionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(productionDate, that.productionDate) &&
            Objects.equals(animalSex, that.animalSex) &&
            Objects.equals(numberOfAnimals, that.numberOfAnimals) &&
            Objects.equals(averageAgeMonths, that.averageAgeMonths) &&
            Objects.equals(averageWeightKg, that.averageWeightKg) &&
            Objects.equals(productionQuantity, that.productionQuantity) &&
            Objects.equals(productionUnit, that.productionUnit) &&
            Objects.equals(mortalityCount, that.mortalityCount) &&
            Objects.equals(birthCount, that.birthCount) &&
            Objects.equals(soldCount, that.soldCount) &&
            Objects.equals(status, that.status) &&
            Objects.equals(livestockActivityId, that.livestockActivityId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            productionDate,
            animalSex,
            numberOfAnimals,
            averageAgeMonths,
            averageWeightKg,
            productionQuantity,
            productionUnit,
            mortalityCount,
            birthCount,
            soldCount,
            status,
            livestockActivityId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LivestockProductionCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalProductionDate().map(f -> "productionDate=" + f + ", ").orElse("") +
            optionalAnimalSex().map(f -> "animalSex=" + f + ", ").orElse("") +
            optionalNumberOfAnimals().map(f -> "numberOfAnimals=" + f + ", ").orElse("") +
            optionalAverageAgeMonths().map(f -> "averageAgeMonths=" + f + ", ").orElse("") +
            optionalAverageWeightKg().map(f -> "averageWeightKg=" + f + ", ").orElse("") +
            optionalProductionQuantity().map(f -> "productionQuantity=" + f + ", ").orElse("") +
            optionalProductionUnit().map(f -> "productionUnit=" + f + ", ").orElse("") +
            optionalMortalityCount().map(f -> "mortalityCount=" + f + ", ").orElse("") +
            optionalBirthCount().map(f -> "birthCount=" + f + ", ").orElse("") +
            optionalSoldCount().map(f -> "soldCount=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalLivestockActivityId().map(f -> "livestockActivityId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
