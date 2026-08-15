package com.naline.coopfull.domain;

import com.naline.coopfull.domain.enumeration.SequenceType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A NumberSequence.
 */
@Entity
@Table(name = "number_sequence")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NumberSequence implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "sequence_type", nullable = false)
    private SequenceType sequenceType;

    @Column(name = "prefix")
    private String prefix;

    @Column(name = "year")
    private Integer year;

    @NotNull
    @Column(name = "current_value", nullable = false)
    private Long currentValue;

    @NotNull
    @Column(name = "padding", nullable = false)
    private Integer padding;

    @ManyToOne(fetch = FetchType.LAZY)
    private Cooperative cooperative;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public NumberSequence id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SequenceType getSequenceType() {
        return this.sequenceType;
    }

    public NumberSequence sequenceType(SequenceType sequenceType) {
        this.setSequenceType(sequenceType);
        return this;
    }

    public void setSequenceType(SequenceType sequenceType) {
        this.sequenceType = sequenceType;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public NumberSequence prefix(String prefix) {
        this.setPrefix(prefix);
        return this;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Integer getYear() {
        return this.year;
    }

    public NumberSequence year(Integer year) {
        this.setYear(year);
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Long getCurrentValue() {
        return this.currentValue;
    }

    public NumberSequence currentValue(Long currentValue) {
        this.setCurrentValue(currentValue);
        return this;
    }

    public void setCurrentValue(Long currentValue) {
        this.currentValue = currentValue;
    }

    public Integer getPadding() {
        return this.padding;
    }

    public NumberSequence padding(Integer padding) {
        this.setPadding(padding);
        return this;
    }

    public void setPadding(Integer padding) {
        this.padding = padding;
    }

    public Cooperative getCooperative() {
        return this.cooperative;
    }

    public void setCooperative(Cooperative cooperative) {
        this.cooperative = cooperative;
    }

    public NumberSequence cooperative(Cooperative cooperative) {
        this.setCooperative(cooperative);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NumberSequence)) {
            return false;
        }
        return getId() != null && getId().equals(((NumberSequence) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NumberSequence{" +
            "id=" + getId() +
            ", sequenceType='" + getSequenceType() + "'" +
            ", prefix='" + getPrefix() + "'" +
            ", year=" + getYear() +
            ", currentValue=" + getCurrentValue() +
            ", padding=" + getPadding() +
            "}";
    }
}
