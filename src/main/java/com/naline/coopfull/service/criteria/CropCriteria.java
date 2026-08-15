package com.naline.coopfull.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.naline.coopfull.domain.Crop} entity. This class is used
 * in {@link com.naline.coopfull.web.rest.CropResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /crops?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CropCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private StringFilter name;

    private StringFilter scientificName;

    private StringFilter category;

    private BooleanFilter perennial;

    private BooleanFilter active;

    private Boolean distinct;

    public CropCriteria() {}

    public CropCriteria(CropCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.scientificName = other.optionalScientificName().map(StringFilter::copy).orElse(null);
        this.category = other.optionalCategory().map(StringFilter::copy).orElse(null);
        this.perennial = other.optionalPerennial().map(BooleanFilter::copy).orElse(null);
        this.active = other.optionalActive().map(BooleanFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CropCriteria copy() {
        return new CropCriteria(this);
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

    public StringFilter getScientificName() {
        return scientificName;
    }

    public Optional<StringFilter> optionalScientificName() {
        return Optional.ofNullable(scientificName);
    }

    public StringFilter scientificName() {
        if (scientificName == null) {
            setScientificName(new StringFilter());
        }
        return scientificName;
    }

    public void setScientificName(StringFilter scientificName) {
        this.scientificName = scientificName;
    }

    public StringFilter getCategory() {
        return category;
    }

    public Optional<StringFilter> optionalCategory() {
        return Optional.ofNullable(category);
    }

    public StringFilter category() {
        if (category == null) {
            setCategory(new StringFilter());
        }
        return category;
    }

    public void setCategory(StringFilter category) {
        this.category = category;
    }

    public BooleanFilter getPerennial() {
        return perennial;
    }

    public Optional<BooleanFilter> optionalPerennial() {
        return Optional.ofNullable(perennial);
    }

    public BooleanFilter perennial() {
        if (perennial == null) {
            setPerennial(new BooleanFilter());
        }
        return perennial;
    }

    public void setPerennial(BooleanFilter perennial) {
        this.perennial = perennial;
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
        final CropCriteria that = (CropCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(name, that.name) &&
            Objects.equals(scientificName, that.scientificName) &&
            Objects.equals(category, that.category) &&
            Objects.equals(perennial, that.perennial) &&
            Objects.equals(active, that.active) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name, scientificName, category, perennial, active, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CropCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCode().map(f -> "code=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalScientificName().map(f -> "scientificName=" + f + ", ").orElse("") +
            optionalCategory().map(f -> "category=" + f + ", ").orElse("") +
            optionalPerennial().map(f -> "perennial=" + f + ", ").orElse("") +
            optionalActive().map(f -> "active=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
