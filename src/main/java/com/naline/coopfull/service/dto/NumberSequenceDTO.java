package com.naline.coopfull.service.dto;

import com.naline.coopfull.domain.enumeration.SequenceType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.naline.coopfull.domain.NumberSequence} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NumberSequenceDTO implements Serializable {

    private Long id;

    @NotNull
    private SequenceType sequenceType;

    private String prefix;

    private Integer year;

    @NotNull
    private Long currentValue;

    @NotNull
    private Integer padding;

    private CooperativeDTO cooperative;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SequenceType getSequenceType() {
        return sequenceType;
    }

    public void setSequenceType(SequenceType sequenceType) {
        this.sequenceType = sequenceType;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Long getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(Long currentValue) {
        this.currentValue = currentValue;
    }

    public Integer getPadding() {
        return padding;
    }

    public void setPadding(Integer padding) {
        this.padding = padding;
    }

    public CooperativeDTO getCooperative() {
        return cooperative;
    }

    public void setCooperative(CooperativeDTO cooperative) {
        this.cooperative = cooperative;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NumberSequenceDTO)) {
            return false;
        }

        NumberSequenceDTO numberSequenceDTO = (NumberSequenceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, numberSequenceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NumberSequenceDTO{" +
            "id=" + getId() +
            ", sequenceType='" + getSequenceType() + "'" +
            ", prefix='" + getPrefix() + "'" +
            ", year=" + getYear() +
            ", currentValue=" + getCurrentValue() +
            ", padding=" + getPadding() +
            ", cooperative=" + getCooperative() +
            "}";
    }
}
