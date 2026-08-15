package com.naline.coopfull.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.naline.coopfull.domain.CropVariety} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CropVarietyDTO implements Serializable {

    private Long id;

    @NotNull
    private String code;

    @NotNull
    private String name;

    @Lob
    private String description;

    private String origin;

    private Integer maturityDays;

    private BigDecimal yieldPotential;

    private String diseaseResistance;

    @NotNull
    private Boolean active;

    private CropDTO crop;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Integer getMaturityDays() {
        return maturityDays;
    }

    public void setMaturityDays(Integer maturityDays) {
        this.maturityDays = maturityDays;
    }

    public BigDecimal getYieldPotential() {
        return yieldPotential;
    }

    public void setYieldPotential(BigDecimal yieldPotential) {
        this.yieldPotential = yieldPotential;
    }

    public String getDiseaseResistance() {
        return diseaseResistance;
    }

    public void setDiseaseResistance(String diseaseResistance) {
        this.diseaseResistance = diseaseResistance;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public CropDTO getCrop() {
        return crop;
    }

    public void setCrop(CropDTO crop) {
        this.crop = crop;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CropVarietyDTO)) {
            return false;
        }

        CropVarietyDTO cropVarietyDTO = (CropVarietyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cropVarietyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CropVarietyDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", origin='" + getOrigin() + "'" +
            ", maturityDays=" + getMaturityDays() +
            ", yieldPotential=" + getYieldPotential() +
            ", diseaseResistance='" + getDiseaseResistance() + "'" +
            ", active='" + getActive() + "'" +
            ", crop=" + getCrop() +
            "}";
    }
}
