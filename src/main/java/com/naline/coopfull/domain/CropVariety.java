package com.naline.coopfull.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CropVariety.
 */
@Entity
@Table(name = "crop_variety")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CropVariety implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "origin")
    private String origin;

    @Column(name = "maturity_days")
    private Integer maturityDays;

    @Column(name = "yield_potential", precision = 21, scale = 2)
    private BigDecimal yieldPotential;

    @Column(name = "disease_resistance")
    private String diseaseResistance;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    private Crop crop;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CropVariety id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public CropVariety code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public CropVariety name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public CropVariety description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrigin() {
        return this.origin;
    }

    public CropVariety origin(String origin) {
        this.setOrigin(origin);
        return this;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Integer getMaturityDays() {
        return this.maturityDays;
    }

    public CropVariety maturityDays(Integer maturityDays) {
        this.setMaturityDays(maturityDays);
        return this;
    }

    public void setMaturityDays(Integer maturityDays) {
        this.maturityDays = maturityDays;
    }

    public BigDecimal getYieldPotential() {
        return this.yieldPotential;
    }

    public CropVariety yieldPotential(BigDecimal yieldPotential) {
        this.setYieldPotential(yieldPotential);
        return this;
    }

    public void setYieldPotential(BigDecimal yieldPotential) {
        this.yieldPotential = yieldPotential;
    }

    public String getDiseaseResistance() {
        return this.diseaseResistance;
    }

    public CropVariety diseaseResistance(String diseaseResistance) {
        this.setDiseaseResistance(diseaseResistance);
        return this;
    }

    public void setDiseaseResistance(String diseaseResistance) {
        this.diseaseResistance = diseaseResistance;
    }

    public Boolean getActive() {
        return this.active;
    }

    public CropVariety active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Crop getCrop() {
        return this.crop;
    }

    public void setCrop(Crop crop) {
        this.crop = crop;
    }

    public CropVariety crop(Crop crop) {
        this.setCrop(crop);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CropVariety)) {
            return false;
        }
        return getId() != null && getId().equals(((CropVariety) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CropVariety{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", origin='" + getOrigin() + "'" +
            ", maturityDays=" + getMaturityDays() +
            ", yieldPotential=" + getYieldPotential() +
            ", diseaseResistance='" + getDiseaseResistance() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}
