package com.naline.coopfull.service.dto;

import com.naline.coopfull.domain.enumeration.ProductionStatus;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.naline.coopfull.domain.AgriculturalProduction} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AgriculturalProductionDTO implements Serializable {

    private Long id;

    @NotNull
    private BigDecimal area;

    @NotNull
    private String areaUnit;

    private LocalDate plantingDate;

    private LocalDate harvestStartDate;

    private LocalDate harvestEndDate;

    private BigDecimal productionQuantity;

    private String productionUnit;

    private BigDecimal expectedAnnualProduction;

    private Integer numberOfPlants;

    private BigDecimal plantingDensity;

    private Integer productionYear;

    @NotNull
    private ProductionStatus status;

    @Lob
    private String notes;

    private AgriculturalActivityDTO agriculturalActivity;

    private CropDTO crop;

    private CropVarietyDTO cropVariety;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public String getAreaUnit() {
        return areaUnit;
    }

    public void setAreaUnit(String areaUnit) {
        this.areaUnit = areaUnit;
    }

    public LocalDate getPlantingDate() {
        return plantingDate;
    }

    public void setPlantingDate(LocalDate plantingDate) {
        this.plantingDate = plantingDate;
    }

    public LocalDate getHarvestStartDate() {
        return harvestStartDate;
    }

    public void setHarvestStartDate(LocalDate harvestStartDate) {
        this.harvestStartDate = harvestStartDate;
    }

    public LocalDate getHarvestEndDate() {
        return harvestEndDate;
    }

    public void setHarvestEndDate(LocalDate harvestEndDate) {
        this.harvestEndDate = harvestEndDate;
    }

    public BigDecimal getProductionQuantity() {
        return productionQuantity;
    }

    public void setProductionQuantity(BigDecimal productionQuantity) {
        this.productionQuantity = productionQuantity;
    }

    public String getProductionUnit() {
        return productionUnit;
    }

    public void setProductionUnit(String productionUnit) {
        this.productionUnit = productionUnit;
    }

    public BigDecimal getExpectedAnnualProduction() {
        return expectedAnnualProduction;
    }

    public void setExpectedAnnualProduction(BigDecimal expectedAnnualProduction) {
        this.expectedAnnualProduction = expectedAnnualProduction;
    }

    public Integer getNumberOfPlants() {
        return numberOfPlants;
    }

    public void setNumberOfPlants(Integer numberOfPlants) {
        this.numberOfPlants = numberOfPlants;
    }

    public BigDecimal getPlantingDensity() {
        return plantingDensity;
    }

    public void setPlantingDensity(BigDecimal plantingDensity) {
        this.plantingDensity = plantingDensity;
    }

    public Integer getProductionYear() {
        return productionYear;
    }

    public void setProductionYear(Integer productionYear) {
        this.productionYear = productionYear;
    }

    public ProductionStatus getStatus() {
        return status;
    }

    public void setStatus(ProductionStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public AgriculturalActivityDTO getAgriculturalActivity() {
        return agriculturalActivity;
    }

    public void setAgriculturalActivity(AgriculturalActivityDTO agriculturalActivity) {
        this.agriculturalActivity = agriculturalActivity;
    }

    public CropDTO getCrop() {
        return crop;
    }

    public void setCrop(CropDTO crop) {
        this.crop = crop;
    }

    public CropVarietyDTO getCropVariety() {
        return cropVariety;
    }

    public void setCropVariety(CropVarietyDTO cropVariety) {
        this.cropVariety = cropVariety;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AgriculturalProductionDTO)) {
            return false;
        }

        AgriculturalProductionDTO agriculturalProductionDTO = (AgriculturalProductionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, agriculturalProductionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AgriculturalProductionDTO{" +
            "id=" + getId() +
            ", area=" + getArea() +
            ", areaUnit='" + getAreaUnit() + "'" +
            ", plantingDate='" + getPlantingDate() + "'" +
            ", harvestStartDate='" + getHarvestStartDate() + "'" +
            ", harvestEndDate='" + getHarvestEndDate() + "'" +
            ", productionQuantity=" + getProductionQuantity() +
            ", productionUnit='" + getProductionUnit() + "'" +
            ", expectedAnnualProduction=" + getExpectedAnnualProduction() +
            ", numberOfPlants=" + getNumberOfPlants() +
            ", plantingDensity=" + getPlantingDensity() +
            ", productionYear=" + getProductionYear() +
            ", status='" + getStatus() + "'" +
            ", notes='" + getNotes() + "'" +
            ", agriculturalActivity=" + getAgriculturalActivity() +
            ", crop=" + getCrop() +
            ", cropVariety=" + getCropVariety() +
            "}";
    }
}
