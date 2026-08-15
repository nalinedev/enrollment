package com.naline.coopfull.service.dto;

import com.naline.coopfull.domain.enumeration.LivestockOwnershipType;
import com.naline.coopfull.domain.enumeration.LivestockProductionMode;
import com.naline.coopfull.domain.enumeration.LivestockProductionType;
import com.naline.coopfull.domain.enumeration.LivestockStatus;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.naline.coopfull.domain.LivestockActivity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LivestockActivityDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @Lob
    private String description;

    private LivestockProductionMode productionMode;

    private LivestockOwnershipType ownershipType;

    private LivestockProductionType productionType;

    private LocalDate startDate;

    private BigDecimal totalArea;

    private String areaUnit;

    @NotNull
    private LivestockStatus status;

    private Integer numberOfAnimals;

    private BigDecimal annualRevenue;

    private BigDecimal monthlyRevenue;

    private Integer employees;

    private Boolean veterinaryServiceAvailable;

    private String feedSource;

    private String waterSource;

    private String certification;

    @Lob
    private String notes;

    private LocationDTO location;

    private LivestockTypeDTO livestockType;

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

    public LivestockProductionMode getProductionMode() {
        return productionMode;
    }

    public void setProductionMode(LivestockProductionMode productionMode) {
        this.productionMode = productionMode;
    }

    public LivestockOwnershipType getOwnershipType() {
        return ownershipType;
    }

    public void setOwnershipType(LivestockOwnershipType ownershipType) {
        this.ownershipType = ownershipType;
    }

    public LivestockProductionType getProductionType() {
        return productionType;
    }

    public void setProductionType(LivestockProductionType productionType) {
        this.productionType = productionType;
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

    public LivestockStatus getStatus() {
        return status;
    }

    public void setStatus(LivestockStatus status) {
        this.status = status;
    }

    public Integer getNumberOfAnimals() {
        return numberOfAnimals;
    }

    public void setNumberOfAnimals(Integer numberOfAnimals) {
        this.numberOfAnimals = numberOfAnimals;
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

    public Boolean getVeterinaryServiceAvailable() {
        return veterinaryServiceAvailable;
    }

    public void setVeterinaryServiceAvailable(Boolean veterinaryServiceAvailable) {
        this.veterinaryServiceAvailable = veterinaryServiceAvailable;
    }

    public String getFeedSource() {
        return feedSource;
    }

    public void setFeedSource(String feedSource) {
        this.feedSource = feedSource;
    }

    public String getWaterSource() {
        return waterSource;
    }

    public void setWaterSource(String waterSource) {
        this.waterSource = waterSource;
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

    public LivestockTypeDTO getLivestockType() {
        return livestockType;
    }

    public void setLivestockType(LivestockTypeDTO livestockType) {
        this.livestockType = livestockType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LivestockActivityDTO)) {
            return false;
        }

        LivestockActivityDTO livestockActivityDTO = (LivestockActivityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, livestockActivityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LivestockActivityDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", productionMode='" + getProductionMode() + "'" +
            ", ownershipType='" + getOwnershipType() + "'" +
            ", productionType='" + getProductionType() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", totalArea=" + getTotalArea() +
            ", areaUnit='" + getAreaUnit() + "'" +
            ", status='" + getStatus() + "'" +
            ", numberOfAnimals=" + getNumberOfAnimals() +
            ", annualRevenue=" + getAnnualRevenue() +
            ", monthlyRevenue=" + getMonthlyRevenue() +
            ", employees=" + getEmployees() +
            ", veterinaryServiceAvailable='" + getVeterinaryServiceAvailable() + "'" +
            ", feedSource='" + getFeedSource() + "'" +
            ", waterSource='" + getWaterSource() + "'" +
            ", certification='" + getCertification() + "'" +
            ", notes='" + getNotes() + "'" +
            ", location=" + getLocation() +
            ", livestockType=" + getLivestockType() +
            "}";
    }
}
