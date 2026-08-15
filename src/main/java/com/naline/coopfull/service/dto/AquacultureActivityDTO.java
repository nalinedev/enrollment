package com.naline.coopfull.service.dto;

import com.naline.coopfull.domain.enumeration.AquacultureOwnershipType;
import com.naline.coopfull.domain.enumeration.AquacultureProductionMode;
import com.naline.coopfull.domain.enumeration.AquacultureProductionType;
import com.naline.coopfull.domain.enumeration.AquacultureStatus;
import com.naline.coopfull.domain.enumeration.AquacultureSystemType;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.naline.coopfull.domain.AquacultureActivity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AquacultureActivityDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @Lob
    private String description;

    private AquacultureProductionMode productionMode;

    private AquacultureOwnershipType ownershipType;

    private AquacultureProductionType productionType;

    private AquacultureSystemType systemType;

    private LocalDate startDate;

    private BigDecimal totalArea;

    private String areaUnit;

    private String waterSource;

    private Integer numberOfProductionUnits;

    private String productionUnitDescription;

    @NotNull
    private AquacultureStatus status;

    private BigDecimal annualRevenue;

    private BigDecimal monthlyRevenue;

    private Integer employees;

    private String certification;

    @Lob
    private String notes;

    private LocationDTO location;

    private AquaticSpeciesDTO aquaticSpecies;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public AquacultureProductionMode getProductionMode() {
        return productionMode;
    }

    public void setProductionMode(AquacultureProductionMode productionMode) {
        this.productionMode = productionMode;
    }

    public AquacultureOwnershipType getOwnershipType() {
        return ownershipType;
    }

    public void setOwnershipType(AquacultureOwnershipType ownershipType) {
        this.ownershipType = ownershipType;
    }

    public AquacultureProductionType getProductionType() {
        return productionType;
    }

    public void setProductionType(AquacultureProductionType productionType) {
        this.productionType = productionType;
    }

    public AquacultureSystemType getSystemType() {
        return systemType;
    }

    public void setSystemType(AquacultureSystemType systemType) {
        this.systemType = systemType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public BigDecimal getTotalArea() {
        return totalArea;
    }

    public void setTotalArea(BigDecimal totalArea) {
        this.totalArea = totalArea;
    }

    public String getAreaUnit() {
        return areaUnit;
    }

    public void setAreaUnit(String areaUnit) {
        this.areaUnit = areaUnit;
    }

    public String getWaterSource() {
        return waterSource;
    }

    public void setWaterSource(String waterSource) {
        this.waterSource = waterSource;
    }

    public Integer getNumberOfProductionUnits() {
        return numberOfProductionUnits;
    }

    public void setNumberOfProductionUnits(Integer numberOfProductionUnits) {
        this.numberOfProductionUnits = numberOfProductionUnits;
    }

    public String getProductionUnitDescription() {
        return productionUnitDescription;
    }

    public void setProductionUnitDescription(String productionUnitDescription) {
        this.productionUnitDescription = productionUnitDescription;
    }

    public AquacultureStatus getStatus() {
        return status;
    }

    public void setStatus(AquacultureStatus status) {
        this.status = status;
    }

    public BigDecimal getAnnualRevenue() {
        return annualRevenue;
    }

    public void setAnnualRevenue(BigDecimal annualRevenue) {
        this.annualRevenue = annualRevenue;
    }

    public BigDecimal getMonthlyRevenue() {
        return monthlyRevenue;
    }

    public void setMonthlyRevenue(BigDecimal monthlyRevenue) {
        this.monthlyRevenue = monthlyRevenue;
    }

    public Integer getEmployees() {
        return employees;
    }

    public void setEmployees(Integer employees) {
        this.employees = employees;
    }

    public String getCertification() {
        return certification;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationDTO location) {
        this.location = location;
    }

    public AquaticSpeciesDTO getAquaticSpecies() {
        return aquaticSpecies;
    }

    public void setAquaticSpecies(AquaticSpeciesDTO aquaticSpecies) {
        this.aquaticSpecies = aquaticSpecies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AquacultureActivityDTO)) {
            return false;
        }

        AquacultureActivityDTO aquacultureActivityDTO = (AquacultureActivityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, aquacultureActivityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AquacultureActivityDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", productionMode='" + getProductionMode() + "'" +
            ", ownershipType='" + getOwnershipType() + "'" +
            ", productionType='" + getProductionType() + "'" +
            ", systemType='" + getSystemType() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", totalArea=" + getTotalArea() +
            ", areaUnit='" + getAreaUnit() + "'" +
            ", waterSource='" + getWaterSource() + "'" +
            ", numberOfProductionUnits=" + getNumberOfProductionUnits() +
            ", productionUnitDescription='" + getProductionUnitDescription() + "'" +
            ", status='" + getStatus() + "'" +
            ", annualRevenue=" + getAnnualRevenue() +
            ", monthlyRevenue=" + getMonthlyRevenue() +
            ", employees=" + getEmployees() +
            ", certification='" + getCertification() + "'" +
            ", notes='" + getNotes() + "'" +
            ", location=" + getLocation() +
            ", aquaticSpecies=" + getAquaticSpecies() +
            "}";
    }
}
