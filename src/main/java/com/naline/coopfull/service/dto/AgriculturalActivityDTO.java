package com.naline.coopfull.service.dto;

import com.naline.coopfull.domain.enumeration.AgriculturalExploitationMode;
import com.naline.coopfull.domain.enumeration.LandOwnershipType;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.naline.coopfull.domain.AgriculturalActivity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AgriculturalActivityDTO implements Serializable {

    private Long id;

    @NotNull
    private BigDecimal totalArea;

    @NotNull
    private String areaUnit;

    private AgriculturalExploitationMode exploitationMode;

    private LandOwnershipType ownershipType;

    private LocalDate startDate;

    private Boolean irrigationAvailable;

    private Boolean organicProduction;

    private String certification;

    @Lob
    private String description;

    private LocationDTO location;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public AgriculturalExploitationMode getExploitationMode() {
        return exploitationMode;
    }

    public void setExploitationMode(AgriculturalExploitationMode exploitationMode) {
        this.exploitationMode = exploitationMode;
    }

    public LandOwnershipType getOwnershipType() {
        return ownershipType;
    }

    public void setOwnershipType(LandOwnershipType ownershipType) {
        this.ownershipType = ownershipType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Boolean getIrrigationAvailable() {
        return irrigationAvailable;
    }

    public void setIrrigationAvailable(Boolean irrigationAvailable) {
        this.irrigationAvailable = irrigationAvailable;
    }

    public Boolean getOrganicProduction() {
        return organicProduction;
    }

    public void setOrganicProduction(Boolean organicProduction) {
        this.organicProduction = organicProduction;
    }

    public String getCertification() {
        return certification;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationDTO location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AgriculturalActivityDTO)) {
            return false;
        }

        AgriculturalActivityDTO agriculturalActivityDTO = (AgriculturalActivityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, agriculturalActivityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AgriculturalActivityDTO{" +
            "id=" + getId() +
            ", totalArea=" + getTotalArea() +
            ", areaUnit='" + getAreaUnit() + "'" +
            ", exploitationMode='" + getExploitationMode() + "'" +
            ", ownershipType='" + getOwnershipType() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", irrigationAvailable='" + getIrrigationAvailable() + "'" +
            ", organicProduction='" + getOrganicProduction() + "'" +
            ", certification='" + getCertification() + "'" +
            ", description='" + getDescription() + "'" +
            ", location=" + getLocation() +
            "}";
    }
}
