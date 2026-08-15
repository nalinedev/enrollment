package com.naline.coopfull.service.dto;

import com.naline.coopfull.domain.enumeration.AnimalProductionStatus;
import com.naline.coopfull.domain.enumeration.AnimalSex;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.naline.coopfull.domain.LivestockProduction} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LivestockProductionDTO implements Serializable {

    private Long id;

    private LocalDate productionDate;

    private AnimalSex animalSex;

    @NotNull
    private Integer numberOfAnimals;

    private Integer averageAgeMonths;

    private BigDecimal averageWeightKg;

    private BigDecimal productionQuantity;

    private String productionUnit;

    private Integer mortalityCount;

    private Integer birthCount;

    private Integer soldCount;

    @NotNull
    private AnimalProductionStatus status;

    @Lob
    private String notes;

    private LivestockActivityDTO livestockActivity;

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

    public AnimalSex getAnimalSex() {
        return animalSex;
    }

    public void setAnimalSex(AnimalSex animalSex) {
        this.animalSex = animalSex;
    }

    public Integer getNumberOfAnimals() {
        return numberOfAnimals;
    }

    public void setNumberOfAnimals(Integer numberOfAnimals) {
        this.numberOfAnimals = numberOfAnimals;
    }

    public Integer getAverageAgeMonths() {
        return averageAgeMonths;
    }

    public void setAverageAgeMonths(Integer averageAgeMonths) {
        this.averageAgeMonths = averageAgeMonths;
    }

    public BigDecimal getAverageWeightKg() {
        return averageWeightKg;
    }

    public void setAverageWeightKg(BigDecimal averageWeightKg) {
        this.averageWeightKg = averageWeightKg;
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

    public Integer getMortalityCount() {
        return mortalityCount;
    }

    public void setMortalityCount(Integer mortalityCount) {
        this.mortalityCount = mortalityCount;
    }

    public Integer getBirthCount() {
        return birthCount;
    }

    public void setBirthCount(Integer birthCount) {
        this.birthCount = birthCount;
    }

    public Integer getSoldCount() {
        return soldCount;
    }

    public void setSoldCount(Integer soldCount) {
        this.soldCount = soldCount;
    }

    public AnimalProductionStatus getStatus() {
        return status;
    }

    public void setStatus(AnimalProductionStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LivestockActivityDTO getLivestockActivity() {
        return livestockActivity;
    }

    public void setLivestockActivity(LivestockActivityDTO livestockActivity) {
        this.livestockActivity = livestockActivity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LivestockProductionDTO)) {
            return false;
        }

        LivestockProductionDTO livestockProductionDTO = (LivestockProductionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, livestockProductionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LivestockProductionDTO{" +
            "id=" + getId() +
            ", productionDate='" + getProductionDate() + "'" +
            ", animalSex='" + getAnimalSex() + "'" +
            ", numberOfAnimals=" + getNumberOfAnimals() +
            ", averageAgeMonths=" + getAverageAgeMonths() +
            ", averageWeightKg=" + getAverageWeightKg() +
            ", productionQuantity=" + getProductionQuantity() +
            ", productionUnit='" + getProductionUnit() + "'" +
            ", mortalityCount=" + getMortalityCount() +
            ", birthCount=" + getBirthCount() +
            ", soldCount=" + getSoldCount() +
            ", status='" + getStatus() + "'" +
            ", notes='" + getNotes() + "'" +
            ", livestockActivity=" + getLivestockActivity() +
            "}";
    }
}
