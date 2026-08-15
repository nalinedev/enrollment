package com.naline.coopfull.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.naline.coopfull.domain.enumeration.AnimalProductionStatus;
import com.naline.coopfull.domain.enumeration.AnimalSex;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LivestockProduction.
 */
@Entity
@Table(name = "livestock_production")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LivestockProduction implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "production_date")
    private LocalDate productionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "animal_sex")
    private AnimalSex animalSex;

    @NotNull
    @Column(name = "number_of_animals", nullable = false)
    private Integer numberOfAnimals;

    @Column(name = "average_age_months")
    private Integer averageAgeMonths;

    @Column(name = "average_weight_kg", precision = 21, scale = 2)
    private BigDecimal averageWeightKg;

    @Column(name = "production_quantity", precision = 21, scale = 2)
    private BigDecimal productionQuantity;

    @Column(name = "production_unit")
    private String productionUnit;

    @Column(name = "mortality_count")
    private Integer mortalityCount;

    @Column(name = "birth_count")
    private Integer birthCount;

    @Column(name = "sold_count")
    private Integer soldCount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AnimalProductionStatus status;

    @Lob
    @Column(name = "notes")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "location", "livestockType", "economicActivity", "productionses" }, allowSetters = true)
    private LivestockActivity livestockActivity;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LivestockProduction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getProductionDate() {
        return this.productionDate;
    }

    public LivestockProduction productionDate(LocalDate productionDate) {
        this.setProductionDate(productionDate);
        return this;
    }

    public void setProductionDate(LocalDate productionDate) {
        this.productionDate = productionDate;
    }

    public AnimalSex getAnimalSex() {
        return this.animalSex;
    }

    public LivestockProduction animalSex(AnimalSex animalSex) {
        this.setAnimalSex(animalSex);
        return this;
    }

    public void setAnimalSex(AnimalSex animalSex) {
        this.animalSex = animalSex;
    }

    public Integer getNumberOfAnimals() {
        return this.numberOfAnimals;
    }

    public LivestockProduction numberOfAnimals(Integer numberOfAnimals) {
        this.setNumberOfAnimals(numberOfAnimals);
        return this;
    }

    public void setNumberOfAnimals(Integer numberOfAnimals) {
        this.numberOfAnimals = numberOfAnimals;
    }

    public Integer getAverageAgeMonths() {
        return this.averageAgeMonths;
    }

    public LivestockProduction averageAgeMonths(Integer averageAgeMonths) {
        this.setAverageAgeMonths(averageAgeMonths);
        return this;
    }

    public void setAverageAgeMonths(Integer averageAgeMonths) {
        this.averageAgeMonths = averageAgeMonths;
    }

    public BigDecimal getAverageWeightKg() {
        return this.averageWeightKg;
    }

    public LivestockProduction averageWeightKg(BigDecimal averageWeightKg) {
        this.setAverageWeightKg(averageWeightKg);
        return this;
    }

    public void setAverageWeightKg(BigDecimal averageWeightKg) {
        this.averageWeightKg = averageWeightKg;
    }

    public BigDecimal getProductionQuantity() {
        return this.productionQuantity;
    }

    public LivestockProduction productionQuantity(BigDecimal productionQuantity) {
        this.setProductionQuantity(productionQuantity);
        return this;
    }

    public void setProductionQuantity(BigDecimal productionQuantity) {
        this.productionQuantity = productionQuantity;
    }

    public String getProductionUnit() {
        return this.productionUnit;
    }

    public LivestockProduction productionUnit(String productionUnit) {
        this.setProductionUnit(productionUnit);
        return this;
    }

    public void setProductionUnit(String productionUnit) {
        this.productionUnit = productionUnit;
    }

    public Integer getMortalityCount() {
        return this.mortalityCount;
    }

    public LivestockProduction mortalityCount(Integer mortalityCount) {
        this.setMortalityCount(mortalityCount);
        return this;
    }

    public void setMortalityCount(Integer mortalityCount) {
        this.mortalityCount = mortalityCount;
    }

    public Integer getBirthCount() {
        return this.birthCount;
    }

    public LivestockProduction birthCount(Integer birthCount) {
        this.setBirthCount(birthCount);
        return this;
    }

    public void setBirthCount(Integer birthCount) {
        this.birthCount = birthCount;
    }

    public Integer getSoldCount() {
        return this.soldCount;
    }

    public LivestockProduction soldCount(Integer soldCount) {
        this.setSoldCount(soldCount);
        return this;
    }

    public void setSoldCount(Integer soldCount) {
        this.soldCount = soldCount;
    }

    public AnimalProductionStatus getStatus() {
        return this.status;
    }

    public LivestockProduction status(AnimalProductionStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(AnimalProductionStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return this.notes;
    }

    public LivestockProduction notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LivestockActivity getLivestockActivity() {
        return this.livestockActivity;
    }

    public void setLivestockActivity(LivestockActivity livestockActivity) {
        this.livestockActivity = livestockActivity;
    }

    public LivestockProduction livestockActivity(LivestockActivity livestockActivity) {
        this.setLivestockActivity(livestockActivity);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LivestockProduction)) {
            return false;
        }
        return getId() != null && getId().equals(((LivestockProduction) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LivestockProduction{" +
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
            "}";
    }
}
