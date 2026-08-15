package com.naline.coopfull.service.dto;

import com.naline.coopfull.domain.enumeration.AquacultureProductionStatus;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.naline.coopfull.domain.AquacultureProduction} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AquacultureProductionDTO implements Serializable {

    private Long id;

    private LocalDate productionDate;

    private Integer numberOfAnimals;

    private BigDecimal stockingDensity;

    private BigDecimal productionQuantity;

    private String productionUnit;

    private BigDecimal averageWeightGrams;

    private Integer mortalityCount;

    private Integer stockingCount;

    private Integer harvestedCount;

    private BigDecimal expectedProduction;

    private LocalDate expectedHarvestDate;

    private LocalDate actualHarvestDate;

    @NotNull
    private AquacultureProductionStatus status;

    @Lob
    private String notes;

    private AquacultureActivityDTO aquacultureActivity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(LocalDate productionDate) {
        this.productionDate = productionDate;
    }

    public Integer getNumberOfAnimals() {
        return numberOfAnimals;
    }

    public void setNumberOfAnimals(Integer numberOfAnimals) {
        this.numberOfAnimals = numberOfAnimals;
    }

    public BigDecimal getStockingDensity() {
        return stockingDensity;
    }

    public void setStockingDensity(BigDecimal stockingDensity) {
        this.stockingDensity = stockingDensity;
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

    public BigDecimal getAverageWeightGrams() {
        return averageWeightGrams;
    }

    public void setAverageWeightGrams(BigDecimal averageWeightGrams) {
        this.averageWeightGrams = averageWeightGrams;
    }

    public Integer getMortalityCount() {
        return mortalityCount;
    }

    public void setMortalityCount(Integer mortalityCount) {
        this.mortalityCount = mortalityCount;
    }

    public Integer getStockingCount() {
        return stockingCount;
    }

    public void setStockingCount(Integer stockingCount) {
        this.stockingCount = stockingCount;
    }

    public Integer getHarvestedCount() {
        return harvestedCount;
    }

    public void setHarvestedCount(Integer harvestedCount) {
        this.harvestedCount = harvestedCount;
    }

    public BigDecimal getExpectedProduction() {
        return expectedProduction;
    }

    public void setExpectedProduction(BigDecimal expectedProduction) {
        this.expectedProduction = expectedProduction;
    }

    public LocalDate getExpectedHarvestDate() {
        return expectedHarvestDate;
    }

    public void setExpectedHarvestDate(LocalDate expectedHarvestDate) {
        this.expectedHarvestDate = expectedHarvestDate;
    }

    public LocalDate getActualHarvestDate() {
        return actualHarvestDate;
    }

    public void setActualHarvestDate(LocalDate actualHarvestDate) {
        this.actualHarvestDate = actualHarvestDate;
    }

    public AquacultureProductionStatus getStatus() {
        return status;
    }

    public void setStatus(AquacultureProductionStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public AquacultureActivityDTO getAquacultureActivity() {
        return aquacultureActivity;
    }

    public void setAquacultureActivity(AquacultureActivityDTO aquacultureActivity) {
        this.aquacultureActivity = aquacultureActivity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AquacultureProductionDTO)) {
            return false;
        }

        AquacultureProductionDTO aquacultureProductionDTO = (AquacultureProductionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, aquacultureProductionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AquacultureProductionDTO{" +
            "id=" + getId() +
            ", productionDate='" + getProductionDate() + "'" +
            ", numberOfAnimals=" + getNumberOfAnimals() +
            ", stockingDensity=" + getStockingDensity() +
            ", productionQuantity=" + getProductionQuantity() +
            ", productionUnit='" + getProductionUnit() + "'" +
            ", averageWeightGrams=" + getAverageWeightGrams() +
            ", mortalityCount=" + getMortalityCount() +
            ", stockingCount=" + getStockingCount() +
            ", harvestedCount=" + getHarvestedCount() +
            ", expectedProduction=" + getExpectedProduction() +
            ", expectedHarvestDate='" + getExpectedHarvestDate() + "'" +
            ", actualHarvestDate='" + getActualHarvestDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", notes='" + getNotes() + "'" +
            ", aquacultureActivity=" + getAquacultureActivity() +
            "}";
    }
}
