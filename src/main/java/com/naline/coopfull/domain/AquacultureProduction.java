package com.naline.coopfull.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.naline.coopfull.domain.enumeration.AquacultureProductionStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AquacultureProduction.
 */
@Entity
@Table(name = "aquaculture_production")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AquacultureProduction implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "production_date")
    private LocalDate productionDate;

    @Column(name = "number_of_animals")
    private Integer numberOfAnimals;

    @Column(name = "stocking_density", precision = 21, scale = 2)
    private BigDecimal stockingDensity;

    @Column(name = "production_quantity", precision = 21, scale = 2)
    private BigDecimal productionQuantity;

    @Column(name = "production_unit")
    private String productionUnit;

    @Column(name = "average_weight_grams", precision = 21, scale = 2)
    private BigDecimal averageWeightGrams;

    @Column(name = "mortality_count")
    private Integer mortalityCount;

    @Column(name = "stocking_count")
    private Integer stockingCount;

    @Column(name = "harvested_count")
    private Integer harvestedCount;

    @Column(name = "expected_production", precision = 21, scale = 2)
    private BigDecimal expectedProduction;

    @Column(name = "expected_harvest_date")
    private LocalDate expectedHarvestDate;

    @Column(name = "actual_harvest_date")
    private LocalDate actualHarvestDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AquacultureProductionStatus status;

    @Lob
    @Column(name = "notes")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "location", "aquaticSpecies", "economicActivity", "productionses" }, allowSetters = true)
    private AquacultureActivity aquacultureActivity;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AquacultureProduction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getProductionDate() {
        return this.productionDate;
    }

    public AquacultureProduction productionDate(LocalDate productionDate) {
        this.setProductionDate(productionDate);
        return this;
    }

    public void setProductionDate(LocalDate productionDate) {
        this.productionDate = productionDate;
    }

    public Integer getNumberOfAnimals() {
        return this.numberOfAnimals;
    }

    public AquacultureProduction numberOfAnimals(Integer numberOfAnimals) {
        this.setNumberOfAnimals(numberOfAnimals);
        return this;
    }

    public void setNumberOfAnimals(Integer numberOfAnimals) {
        this.numberOfAnimals = numberOfAnimals;
    }

    public BigDecimal getStockingDensity() {
        return this.stockingDensity;
    }

    public AquacultureProduction stockingDensity(BigDecimal stockingDensity) {
        this.setStockingDensity(stockingDensity);
        return this;
    }

    public void setStockingDensity(BigDecimal stockingDensity) {
        this.stockingDensity = stockingDensity;
    }

    public BigDecimal getProductionQuantity() {
        return this.productionQuantity;
    }

    public AquacultureProduction productionQuantity(BigDecimal productionQuantity) {
        this.setProductionQuantity(productionQuantity);
        return this;
    }

    public void setProductionQuantity(BigDecimal productionQuantity) {
        this.productionQuantity = productionQuantity;
    }

    public String getProductionUnit() {
        return this.productionUnit;
    }

    public AquacultureProduction productionUnit(String productionUnit) {
        this.setProductionUnit(productionUnit);
        return this;
    }

    public void setProductionUnit(String productionUnit) {
        this.productionUnit = productionUnit;
    }

    public BigDecimal getAverageWeightGrams() {
        return this.averageWeightGrams;
    }

    public AquacultureProduction averageWeightGrams(BigDecimal averageWeightGrams) {
        this.setAverageWeightGrams(averageWeightGrams);
        return this;
    }

    public void setAverageWeightGrams(BigDecimal averageWeightGrams) {
        this.averageWeightGrams = averageWeightGrams;
    }

    public Integer getMortalityCount() {
        return this.mortalityCount;
    }

    public AquacultureProduction mortalityCount(Integer mortalityCount) {
        this.setMortalityCount(mortalityCount);
        return this;
    }

    public void setMortalityCount(Integer mortalityCount) {
        this.mortalityCount = mortalityCount;
    }

    public Integer getStockingCount() {
        return this.stockingCount;
    }

    public AquacultureProduction stockingCount(Integer stockingCount) {
        this.setStockingCount(stockingCount);
        return this;
    }

    public void setStockingCount(Integer stockingCount) {
        this.stockingCount = stockingCount;
    }

    public Integer getHarvestedCount() {
        return this.harvestedCount;
    }

    public AquacultureProduction harvestedCount(Integer harvestedCount) {
        this.setHarvestedCount(harvestedCount);
        return this;
    }

    public void setHarvestedCount(Integer harvestedCount) {
        this.harvestedCount = harvestedCount;
    }

    public BigDecimal getExpectedProduction() {
        return this.expectedProduction;
    }

    public AquacultureProduction expectedProduction(BigDecimal expectedProduction) {
        this.setExpectedProduction(expectedProduction);
        return this;
    }

    public void setExpectedProduction(BigDecimal expectedProduction) {
        this.expectedProduction = expectedProduction;
    }

    public LocalDate getExpectedHarvestDate() {
        return this.expectedHarvestDate;
    }

    public AquacultureProduction expectedHarvestDate(LocalDate expectedHarvestDate) {
        this.setExpectedHarvestDate(expectedHarvestDate);
        return this;
    }

    public void setExpectedHarvestDate(LocalDate expectedHarvestDate) {
        this.expectedHarvestDate = expectedHarvestDate;
    }

    public LocalDate getActualHarvestDate() {
        return this.actualHarvestDate;
    }

    public AquacultureProduction actualHarvestDate(LocalDate actualHarvestDate) {
        this.setActualHarvestDate(actualHarvestDate);
        return this;
    }

    public void setActualHarvestDate(LocalDate actualHarvestDate) {
        this.actualHarvestDate = actualHarvestDate;
    }

    public AquacultureProductionStatus getStatus() {
        return this.status;
    }

    public AquacultureProduction status(AquacultureProductionStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(AquacultureProductionStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return this.notes;
    }

    public AquacultureProduction notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public AquacultureActivity getAquacultureActivity() {
        return this.aquacultureActivity;
    }

    public void setAquacultureActivity(AquacultureActivity aquacultureActivity) {
        this.aquacultureActivity = aquacultureActivity;
    }

    public AquacultureProduction aquacultureActivity(AquacultureActivity aquacultureActivity) {
        this.setAquacultureActivity(aquacultureActivity);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AquacultureProduction)) {
            return false;
        }
        return getId() != null && getId().equals(((AquacultureProduction) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AquacultureProduction{" +
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
            "}";
    }
}
