package com.naline.coopfull.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.naline.coopfull.domain.CropVariety} entity. This class is used
 * in {@link com.naline.coopfull.web.rest.CropVarietyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /crop-varieties?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CropVarietyCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private StringFilter name;

    private StringFilter origin;

    private IntegerFilter maturityDays;

    private BigDecimalFilter yieldPotential;

    private StringFilter diseaseResistance;

    private BooleanFilter active;

    private LongFilter cropId;

    private Boolean distinct;

    public CropVarietyCriteria() {}

    public CropVarietyCriteria(CropVarietyCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.origin = other.optionalOrigin().map(StringFilter::copy).orElse(null);
        this.maturityDays = other.optionalMaturityDays().map(IntegerFilter::copy).orElse(null);
        this.yieldPotential = other.optionalYieldPotential().map(BigDecimalFilter::copy).orElse(null);
        this.diseaseResistance = other.optionalDiseaseResistance().map(StringFilter::copy).orElse(null);
        this.active = other.optionalActive().map(BooleanFilter::copy).orElse(null);
        this.cropId = other.optionalCropId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CropVarietyCriteria copy() {
        return new CropVarietyCriteria(this);
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

    public StringFilter getOrigin() {
        return origin;
    }

    public Optional<StringFilter> optionalOrigin() {
        return Optional.ofNullable(origin);
    }

    public StringFilter origin() {
        if (origin == null) {
            setOrigin(new StringFilter());
        }
        return origin;
    }

    public void setOrigin(StringFilter origin) {
        this.origin = origin;
    }

    public IntegerFilter getMaturityDays() {
        return maturityDays;
    }

    public Optional<IntegerFilter> optionalMaturityDays() {
        return Optional.ofNullable(maturityDays);
    }

    public IntegerFilter maturityDays() {
        if (maturityDays == null) {
            setMaturityDays(new IntegerFilter());
        }
        return maturityDays;
    }

    public void setMaturityDays(IntegerFilter maturityDays) {
        this.maturityDays = maturityDays;
    }

    public BigDecimalFilter getYieldPotential() {
        return yieldPotential;
    }

    public Optional<BigDecimalFilter> optionalYieldPotential() {
        return Optional.ofNullable(yieldPotential);
    }

    public BigDecimalFilter yieldPotential() {
        if (yieldPotential == null) {
            setYieldPotential(new BigDecimalFilter());
        }
        return yieldPotential;
    }

    public void setYieldPotential(BigDecimalFilter yieldPotential) {
        this.yieldPotential = yieldPotential;
    }

    public StringFilter getDiseaseResistance() {
        return diseaseResistance;
    }

    public Optional<StringFilter> optionalDiseaseResistance() {
        return Optional.ofNullable(diseaseResistance);
    }

    public StringFilter diseaseResistance() {
        if (diseaseResistance == null) {
            setDiseaseResistance(new StringFilter());
        }
        return diseaseResistance;
    }

    public void setDiseaseResistance(StringFilter diseaseResistance) {
        this.diseaseResistance = diseaseResistance;
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
        final CropVarietyCriteria that = (CropVarietyCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(name, that.name) &&
            Objects.equals(origin, that.origin) &&
            Objects.equals(maturityDays, that.maturityDays) &&
            Objects.equals(yieldPotential, that.yieldPotential) &&
            Objects.equals(diseaseResistance, that.diseaseResistance) &&
            Objects.equals(active, that.active) &&
            Objects.equals(cropId, that.cropId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name, origin, maturityDays, yieldPotential, diseaseResistance, active, cropId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CropVarietyCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCode().map(f -> "code=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalOrigin().map(f -> "origin=" + f + ", ").orElse("") +
            optionalMaturityDays().map(f -> "maturityDays=" + f + ", ").orElse("") +
            optionalYieldPotential().map(f -> "yieldPotential=" + f + ", ").orElse("") +
            optionalDiseaseResistance().map(f -> "diseaseResistance=" + f + ", ").orElse("") +
            optionalActive().map(f -> "active=" + f + ", ").orElse("") +
            optionalCropId().map(f -> "cropId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
