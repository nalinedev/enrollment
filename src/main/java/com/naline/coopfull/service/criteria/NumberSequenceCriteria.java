package com.naline.coopfull.service.criteria;

import com.naline.coopfull.domain.enumeration.SequenceType;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.naline.coopfull.domain.NumberSequence} entity. This class is used
 * in {@link com.naline.coopfull.web.rest.NumberSequenceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /number-sequences?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NumberSequenceCriteria implements Serializable, Criteria {

    /**
     * Class for filtering SequenceType
     */
    public static class SequenceTypeFilter extends Filter<SequenceType> {

        public SequenceTypeFilter() {}

        public SequenceTypeFilter(SequenceTypeFilter filter) {
            super(filter);
        }

        @Override
        public SequenceTypeFilter copy() {
            return new SequenceTypeFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private SequenceTypeFilter sequenceType;

    private StringFilter prefix;

    private IntegerFilter year;

    private LongFilter currentValue;

    private IntegerFilter padding;

    private LongFilter cooperativeId;

    private Boolean distinct;

    public NumberSequenceCriteria() {}

    public NumberSequenceCriteria(NumberSequenceCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.sequenceType = other.optionalSequenceType().map(SequenceTypeFilter::copy).orElse(null);
        this.prefix = other.optionalPrefix().map(StringFilter::copy).orElse(null);
        this.year = other.optionalYear().map(IntegerFilter::copy).orElse(null);
        this.currentValue = other.optionalCurrentValue().map(LongFilter::copy).orElse(null);
        this.padding = other.optionalPadding().map(IntegerFilter::copy).orElse(null);
        this.cooperativeId = other.optionalCooperativeId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public NumberSequenceCriteria copy() {
        return new NumberSequenceCriteria(this);
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

    public SequenceTypeFilter getSequenceType() {
        return sequenceType;
    }

    public Optional<SequenceTypeFilter> optionalSequenceType() {
        return Optional.ofNullable(sequenceType);
    }

    public SequenceTypeFilter sequenceType() {
        if (sequenceType == null) {
            setSequenceType(new SequenceTypeFilter());
        }
        return sequenceType;
    }

    public void setSequenceType(SequenceTypeFilter sequenceType) {
        this.sequenceType = sequenceType;
    }

    public StringFilter getPrefix() {
        return prefix;
    }

    public Optional<StringFilter> optionalPrefix() {
        return Optional.ofNullable(prefix);
    }

    public StringFilter prefix() {
        if (prefix == null) {
            setPrefix(new StringFilter());
        }
        return prefix;
    }

    public void setPrefix(StringFilter prefix) {
        this.prefix = prefix;
    }

    public IntegerFilter getYear() {
        return year;
    }

    public Optional<IntegerFilter> optionalYear() {
        return Optional.ofNullable(year);
    }

    public IntegerFilter year() {
        if (year == null) {
            setYear(new IntegerFilter());
        }
        return year;
    }

    public void setYear(IntegerFilter year) {
        this.year = year;
    }

    public LongFilter getCurrentValue() {
        return currentValue;
    }

    public Optional<LongFilter> optionalCurrentValue() {
        return Optional.ofNullable(currentValue);
    }

    public LongFilter currentValue() {
        if (currentValue == null) {
            setCurrentValue(new LongFilter());
        }
        return currentValue;
    }

    public void setCurrentValue(LongFilter currentValue) {
        this.currentValue = currentValue;
    }

    public IntegerFilter getPadding() {
        return padding;
    }

    public Optional<IntegerFilter> optionalPadding() {
        return Optional.ofNullable(padding);
    }

    public IntegerFilter padding() {
        if (padding == null) {
            setPadding(new IntegerFilter());
        }
        return padding;
    }

    public void setPadding(IntegerFilter padding) {
        this.padding = padding;
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
        final NumberSequenceCriteria that = (NumberSequenceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(sequenceType, that.sequenceType) &&
            Objects.equals(prefix, that.prefix) &&
            Objects.equals(year, that.year) &&
            Objects.equals(currentValue, that.currentValue) &&
            Objects.equals(padding, that.padding) &&
            Objects.equals(cooperativeId, that.cooperativeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sequenceType, prefix, year, currentValue, padding, cooperativeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NumberSequenceCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalSequenceType().map(f -> "sequenceType=" + f + ", ").orElse("") +
            optionalPrefix().map(f -> "prefix=" + f + ", ").orElse("") +
            optionalYear().map(f -> "year=" + f + ", ").orElse("") +
            optionalCurrentValue().map(f -> "currentValue=" + f + ", ").orElse("") +
            optionalPadding().map(f -> "padding=" + f + ", ").orElse("") +
            optionalCooperativeId().map(f -> "cooperativeId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
