package com.naline.coopfull.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.naline.coopfull.domain.enumeration.AquacultureOwnershipType;
import com.naline.coopfull.domain.enumeration.AquacultureProductionMode;
import com.naline.coopfull.domain.enumeration.AquacultureProductionType;
import com.naline.coopfull.domain.enumeration.AquacultureStatus;
import com.naline.coopfull.domain.enumeration.AquacultureSystemType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AquacultureActivity.
 */
@Entity
@Table(name = "aquaculture_activity")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AquacultureActivity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "production_mode")
    private AquacultureProductionMode productionMode;

    @Enumerated(EnumType.STRING)
    @Column(name = "ownership_type")
    private AquacultureOwnershipType ownershipType;

    @Enumerated(EnumType.STRING)
    @Column(name = "production_type")
    private AquacultureProductionType productionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "system_type")
    private AquacultureSystemType systemType;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "total_area", precision = 21, scale = 2)
    private BigDecimal totalArea;

    @Column(name = "area_unit")
    private String areaUnit;

    @Column(name = "water_source")
    private String waterSource;

    @Column(name = "number_of_production_units")
    private Integer numberOfProductionUnits;

    @Column(name = "production_unit_description")
    private String productionUnitDescription;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AquacultureStatus status;

    @Column(name = "annual_revenue", precision = 21, scale = 2)
    private BigDecimal annualRevenue;

    @Column(name = "monthly_revenue", precision = 21, scale = 2)
    private BigDecimal monthlyRevenue;

    @Column(name = "employees")
    private Integer employees;

    @Column(name = "certification")
    private String certification;

    @Lob
    @Column(name = "notes")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "parent" }, allowSetters = true)
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    private AquaticSpecies aquaticSpecies;

    @JsonIgnoreProperties(
        value = { "agriculturalActivity", "livestockActivity", "aquacultureActivity", "member", "activityType", "location" },
        allowSetters = true
    )
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "aquacultureActivity")
    private EconomicActivity economicActivity;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "aquacultureActivity")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "aquacultureActivity" }, allowSetters = true)
    private Set<AquacultureProduction> productionses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AquacultureActivity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public AquacultureActivity name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public AquacultureActivity description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AquacultureProductionMode getProductionMode() {
        return this.productionMode;
    }

    public AquacultureActivity productionMode(AquacultureProductionMode productionMode) {
        this.setProductionMode(productionMode);
        return this;
    }

    public void setProductionMode(AquacultureProductionMode productionMode) {
        this.productionMode = productionMode;
    }

    public AquacultureOwnershipType getOwnershipType() {
        return this.ownershipType;
    }

    public AquacultureActivity ownershipType(AquacultureOwnershipType ownershipType) {
        this.setOwnershipType(ownershipType);
        return this;
    }

    public void setOwnershipType(AquacultureOwnershipType ownershipType) {
        this.ownershipType = ownershipType;
    }

    public AquacultureProductionType getProductionType() {
        return this.productionType;
    }

    public AquacultureActivity productionType(AquacultureProductionType productionType) {
        this.setProductionType(productionType);
        return this;
    }

    public void setProductionType(AquacultureProductionType productionType) {
        this.productionType = productionType;
    }

    public AquacultureSystemType getSystemType() {
        return this.systemType;
    }

    public AquacultureActivity systemType(AquacultureSystemType systemType) {
        this.setSystemType(systemType);
        return this;
    }

    public void setSystemType(AquacultureSystemType systemType) {
        this.systemType = systemType;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public AquacultureActivity startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public BigDecimal getTotalArea() {
        return this.totalArea;
    }

    public AquacultureActivity totalArea(BigDecimal totalArea) {
        this.setTotalArea(totalArea);
        return this;
    }

    public void setTotalArea(BigDecimal totalArea) {
        this.totalArea = totalArea;
    }

    public String getAreaUnit() {
        return this.areaUnit;
    }

    public AquacultureActivity areaUnit(String areaUnit) {
        this.setAreaUnit(areaUnit);
        return this;
    }

    public void setAreaUnit(String areaUnit) {
        this.areaUnit = areaUnit;
    }

    public String getWaterSource() {
        return this.waterSource;
    }

    public AquacultureActivity waterSource(String waterSource) {
        this.setWaterSource(waterSource);
        return this;
    }

    public void setWaterSource(String waterSource) {
        this.waterSource = waterSource;
    }

    public Integer getNumberOfProductionUnits() {
        return this.numberOfProductionUnits;
    }

    public AquacultureActivity numberOfProductionUnits(Integer numberOfProductionUnits) {
        this.setNumberOfProductionUnits(numberOfProductionUnits);
        return this;
    }

    public void setNumberOfProductionUnits(Integer numberOfProductionUnits) {
        this.numberOfProductionUnits = numberOfProductionUnits;
    }

    public String getProductionUnitDescription() {
        return this.productionUnitDescription;
    }

    public AquacultureActivity productionUnitDescription(String productionUnitDescription) {
        this.setProductionUnitDescription(productionUnitDescription);
        return this;
    }

    public void setProductionUnitDescription(String productionUnitDescription) {
        this.productionUnitDescription = productionUnitDescription;
    }

    public AquacultureStatus getStatus() {
        return this.status;
    }

    public AquacultureActivity status(AquacultureStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(AquacultureStatus status) {
        this.status = status;
    }

    public BigDecimal getAnnualRevenue() {
        return this.annualRevenue;
    }

    public AquacultureActivity annualRevenue(BigDecimal annualRevenue) {
        this.setAnnualRevenue(annualRevenue);
        return this;
    }

    public void setAnnualRevenue(BigDecimal annualRevenue) {
        this.annualRevenue = annualRevenue;
    }

    public BigDecimal getMonthlyRevenue() {
        return this.monthlyRevenue;
    }

    public AquacultureActivity monthlyRevenue(BigDecimal monthlyRevenue) {
        this.setMonthlyRevenue(monthlyRevenue);
        return this;
    }

    public void setMonthlyRevenue(BigDecimal monthlyRevenue) {
        this.monthlyRevenue = monthlyRevenue;
    }

    public Integer getEmployees() {
        return this.employees;
    }

    public AquacultureActivity employees(Integer employees) {
        this.setEmployees(employees);
        return this;
    }

    public void setEmployees(Integer employees) {
        this.employees = employees;
    }

    public String getCertification() {
        return this.certification;
    }

    public AquacultureActivity certification(String certification) {
        this.setCertification(certification);
        return this;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }

    public String getNotes() {
        return this.notes;
    }

    public AquacultureActivity notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public AquacultureActivity location(Location location) {
        this.setLocation(location);
        return this;
    }

    public AquaticSpecies getAquaticSpecies() {
        return this.aquaticSpecies;
    }

    public void setAquaticSpecies(AquaticSpecies aquaticSpecies) {
        this.aquaticSpecies = aquaticSpecies;
    }

    public AquacultureActivity aquaticSpecies(AquaticSpecies aquaticSpecies) {
        this.setAquaticSpecies(aquaticSpecies);
        return this;
    }

    public EconomicActivity getEconomicActivity() {
        return this.economicActivity;
    }

    public void setEconomicActivity(EconomicActivity economicActivity) {
        if (this.economicActivity != null) {
            this.economicActivity.setAquacultureActivity(null);
        }
        if (economicActivity != null) {
            economicActivity.setAquacultureActivity(this);
        }
        this.economicActivity = economicActivity;
    }

    public AquacultureActivity economicActivity(EconomicActivity economicActivity) {
        this.setEconomicActivity(economicActivity);
        return this;
    }

    public Set<AquacultureProduction> getProductionses() {
        return this.productionses;
    }

    public void setProductionses(Set<AquacultureProduction> aquacultureProductions) {
        if (this.productionses != null) {
            this.productionses.forEach(i -> i.setAquacultureActivity(null));
        }
        if (aquacultureProductions != null) {
            aquacultureProductions.forEach(i -> i.setAquacultureActivity(this));
        }
        this.productionses = aquacultureProductions;
    }

    public AquacultureActivity productionses(Set<AquacultureProduction> aquacultureProductions) {
        this.setProductionses(aquacultureProductions);
        return this;
    }

    public AquacultureActivity addProductions(AquacultureProduction aquacultureProduction) {
        this.productionses.add(aquacultureProduction);
        aquacultureProduction.setAquacultureActivity(this);
        return this;
    }

    public AquacultureActivity removeProductions(AquacultureProduction aquacultureProduction) {
        this.productionses.remove(aquacultureProduction);
        aquacultureProduction.setAquacultureActivity(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AquacultureActivity)) {
            return false;
        }
        return getId() != null && getId().equals(((AquacultureActivity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AquacultureActivity{" +
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
            "}";
    }
}
