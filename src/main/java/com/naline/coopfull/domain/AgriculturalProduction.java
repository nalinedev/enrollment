package com.naline.coopfull.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.naline.coopfull.domain.enumeration.ProductionStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AgriculturalProduction.
 */
@Entity
@Table(name = "agricultural_production")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AgriculturalProduction implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "area", precision = 21, scale = 2, nullable = false)
    private BigDecimal area;

    @NotNull
    @Column(name = "area_unit", nullable = false)
    private String areaUnit;

    @Column(name = "planting_date")
    private LocalDate plantingDate;

    @Column(name = "harvest_start_date")
    private LocalDate harvestStartDate;

    @Column(name = "harvest_end_date")
    private LocalDate harvestEndDate;

    @Column(name = "production_quantity", precision = 21, scale = 2)
    private BigDecimal productionQuantity;

    @Column(name = "production_unit")
    private String productionUnit;

    @Column(name = "expected_annual_production", precision = 21, scale = 2)
    private BigDecimal expectedAnnualProduction;

    @Column(name = "number_of_plants")
    private Integer numberOfPlants;

    @Column(name = "planting_density", precision = 21, scale = 2)
    private BigDecimal plantingDensity;

    @Column(name = "production_year")
    private Integer productionYear;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProductionStatus status;

    @Lob
    @Column(name = "notes")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "location", "economicActivity", "productionses" }, allowSetters = true)
    private AgriculturalActivity agriculturalActivity;

    @ManyToOne(fetch = FetchType.LAZY)
    private Crop crop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "crop" }, allowSetters = true)
    private CropVariety cropVariety;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AgriculturalProduction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getArea() {
        return this.area;
    }

    public AgriculturalProduction area(BigDecimal area) {
        this.setArea(area);
        return this;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public String getAreaUnit() {
        return this.areaUnit;
    }

    public AgriculturalProduction areaUnit(String areaUnit) {
        this.setAreaUnit(areaUnit);
        return this;
    }

    public void setAreaUnit(String areaUnit) {
        this.areaUnit = areaUnit;
    }

    public LocalDate getPlantingDate() {
        return this.plantingDate;
    }

    public AgriculturalProduction plantingDate(LocalDate plantingDate) {
        this.setPlantingDate(plantingDate);
        return this;
    }

    public void setPlantingDate(LocalDate plantingDate) {
        this.plantingDate = plantingDate;
    }

    public LocalDate getHarvestStartDate() {
        return this.harvestStartDate;
    }

    public AgriculturalProduction harvestStartDate(LocalDate harvestStartDate) {
        this.setHarvestStartDate(harvestStartDate);
        return this;
    }

    public void setHarvestStartDate(LocalDate harvestStartDate) {
        this.harvestStartDate = harvestStartDate;
    }

    public LocalDate getHarvestEndDate() {
        return this.harvestEndDate;
    }

    public AgriculturalProduction harvestEndDate(LocalDate harvestEndDate) {
        this.setHarvestEndDate(harvestEndDate);
        return this;
    }

    public void setHarvestEndDate(LocalDate harvestEndDate) {
        this.harvestEndDate = harvestEndDate;
    }

    public BigDecimal getProductionQuantity() {
        return this.productionQuantity;
    }

    public AgriculturalProduction productionQuantity(BigDecimal productionQuantity) {
        this.setProductionQuantity(productionQuantity);
        return this;
    }

    public void setProductionQuantity(BigDecimal productionQuantity) {
        this.productionQuantity = productionQuantity;
    }

    public String getProductionUnit() {
        return this.productionUnit;
    }

    public AgriculturalProduction productionUnit(String productionUnit) {
        this.setProductionUnit(productionUnit);
        return this;
    }

    public void setProductionUnit(String productionUnit) {
        this.productionUnit = productionUnit;
    }

    public BigDecimal getExpectedAnnualProduction() {
        return this.expectedAnnualProduction;
    }

    public AgriculturalProduction expectedAnnualProduction(BigDecimal expectedAnnualProduction) {
        this.setExpectedAnnualProduction(expectedAnnualProduction);
        return this;
    }

    public void setExpectedAnnualProduction(BigDecimal expectedAnnualProduction) {
        this.expectedAnnualProduction = expectedAnnualProduction;
    }

    public Integer getNumberOfPlants() {
        return this.numberOfPlants;
    }

    public AgriculturalProduction numberOfPlants(Integer numberOfPlants) {
        this.setNumberOfPlants(numberOfPlants);
        return this;
    }

    public void setNumberOfPlants(Integer numberOfPlants) {
        this.numberOfPlants = numberOfPlants;
    }

    public BigDecimal getPlantingDensity() {
        return this.plantingDensity;
    }

    public AgriculturalProduction plantingDensity(BigDecimal plantingDensity) {
        this.setPlantingDensity(plantingDensity);
        return this;
    }

    public void setPlantingDensity(BigDecimal plantingDensity) {
        this.plantingDensity = plantingDensity;
    }

    public Integer getProductionYear() {
        return this.productionYear;
    }

    public AgriculturalProduction productionYear(Integer productionYear) {
        this.setProductionYear(productionYear);
        return this;
    }

    public void setProductionYear(Integer productionYear) {
        this.productionYear = productionYear;
    }

    public ProductionStatus getStatus() {
        return this.status;
    }

    public AgriculturalProduction status(ProductionStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ProductionStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return this.notes;
    }

    public AgriculturalProduction notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public AgriculturalActivity getAgriculturalActivity() {
        return this.agriculturalActivity;
    }

    public void setAgriculturalActivity(AgriculturalActivity agriculturalActivity) {
        this.agriculturalActivity = agriculturalActivity;
    }

    public AgriculturalProduction agriculturalActivity(AgriculturalActivity agriculturalActivity) {
        this.setAgriculturalActivity(agriculturalActivity);
        return this;
    }

    public Crop getCrop() {
        return this.crop;
    }

    public void setCrop(Crop crop) {
        this.crop = crop;
    }

    public AgriculturalProduction crop(Crop crop) {
        this.setCrop(crop);
        return this;
    }

    public CropVariety getCropVariety() {
        return this.cropVariety;
    }

    public void setCropVariety(CropVariety cropVariety) {
        this.cropVariety = cropVariety;
    }

    public AgriculturalProduction cropVariety(CropVariety cropVariety) {
        this.setCropVariety(cropVariety);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AgriculturalProduction)) {
            return false;
        }
        return getId() != null && getId().equals(((AgriculturalProduction) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AgriculturalProduction{" +
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
            "}";
    }
}
