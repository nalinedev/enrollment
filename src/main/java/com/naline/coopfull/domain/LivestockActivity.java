package com.naline.coopfull.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.naline.coopfull.domain.enumeration.LivestockOwnershipType;
import com.naline.coopfull.domain.enumeration.LivestockProductionMode;
import com.naline.coopfull.domain.enumeration.LivestockProductionType;
import com.naline.coopfull.domain.enumeration.LivestockStatus;
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
 * A LivestockActivity.
 */
@Entity
@Table(name = "livestock_activity")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LivestockActivity implements Serializable {

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
    private LivestockProductionMode productionMode;

    @Enumerated(EnumType.STRING)
    @Column(name = "ownership_type")
    private LivestockOwnershipType ownershipType;

    @Enumerated(EnumType.STRING)
    @Column(name = "production_type")
    private LivestockProductionType productionType;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "total_area", precision = 21, scale = 2)
    private BigDecimal totalArea;

    @Column(name = "area_unit")
    private String areaUnit;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private LivestockStatus status;

    @Column(name = "number_of_animals")
    private Integer numberOfAnimals;

    @Column(name = "annual_revenue", precision = 21, scale = 2)
    private BigDecimal annualRevenue;

    @Column(name = "monthly_revenue", precision = 21, scale = 2)
    private BigDecimal monthlyRevenue;

    @Column(name = "employees")
    private Integer employees;

    @Column(name = "veterinary_service_available")
    private Boolean veterinaryServiceAvailable;

    @Column(name = "feed_source")
    private String feedSource;

    @Column(name = "water_source")
    private String waterSource;

    @Column(name = "certification")
    private String certification;

    @Lob
    @Column(name = "notes")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "parent" }, allowSetters = true)
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    private LivestockType livestockType;

    @JsonIgnoreProperties(
        value = { "agriculturalActivity", "livestockActivity", "aquacultureActivity", "member", "activityType", "location" },
        allowSetters = true
    )
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "livestockActivity")
    private EconomicActivity economicActivity;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "livestockActivity")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "livestockActivity" }, allowSetters = true)
    private Set<LivestockProduction> productionses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LivestockActivity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public LivestockActivity name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public LivestockActivity description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LivestockProductionMode getProductionMode() {
        return this.productionMode;
    }

    public LivestockActivity productionMode(LivestockProductionMode productionMode) {
        this.setProductionMode(productionMode);
        return this;
    }

    public void setProductionMode(LivestockProductionMode productionMode) {
        this.productionMode = productionMode;
    }

    public LivestockOwnershipType getOwnershipType() {
        return this.ownershipType;
    }

    public LivestockActivity ownershipType(LivestockOwnershipType ownershipType) {
        this.setOwnershipType(ownershipType);
        return this;
    }

    public void setOwnershipType(LivestockOwnershipType ownershipType) {
        this.ownershipType = ownershipType;
    }

    public LivestockProductionType getProductionType() {
        return this.productionType;
    }

    public LivestockActivity productionType(LivestockProductionType productionType) {
        this.setProductionType(productionType);
        return this;
    }

    public void setProductionType(LivestockProductionType productionType) {
        this.productionType = productionType;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public LivestockActivity startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public BigDecimal getTotalArea() {
        return this.totalArea;
    }

    public LivestockActivity totalArea(BigDecimal totalArea) {
        this.setTotalArea(totalArea);
        return this;
    }

    public void setTotalArea(BigDecimal totalArea) {
        this.totalArea = totalArea;
    }

    public String getAreaUnit() {
        return this.areaUnit;
    }

    public LivestockActivity areaUnit(String areaUnit) {
        this.setAreaUnit(areaUnit);
        return this;
    }

    public void setAreaUnit(String areaUnit) {
        this.areaUnit = areaUnit;
    }

    public LivestockStatus getStatus() {
        return this.status;
    }

    public LivestockActivity status(LivestockStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(LivestockStatus status) {
        this.status = status;
    }

    public Integer getNumberOfAnimals() {
        return this.numberOfAnimals;
    }

    public LivestockActivity numberOfAnimals(Integer numberOfAnimals) {
        this.setNumberOfAnimals(numberOfAnimals);
        return this;
    }

    public void setNumberOfAnimals(Integer numberOfAnimals) {
        this.numberOfAnimals = numberOfAnimals;
    }

    public BigDecimal getAnnualRevenue() {
        return this.annualRevenue;
    }

    public LivestockActivity annualRevenue(BigDecimal annualRevenue) {
        this.setAnnualRevenue(annualRevenue);
        return this;
    }

    public void setAnnualRevenue(BigDecimal annualRevenue) {
        this.annualRevenue = annualRevenue;
    }

    public BigDecimal getMonthlyRevenue() {
        return this.monthlyRevenue;
    }

    public LivestockActivity monthlyRevenue(BigDecimal monthlyRevenue) {
        this.setMonthlyRevenue(monthlyRevenue);
        return this;
    }

    public void setMonthlyRevenue(BigDecimal monthlyRevenue) {
        this.monthlyRevenue = monthlyRevenue;
    }

    public Integer getEmployees() {
        return this.employees;
    }

    public LivestockActivity employees(Integer employees) {
        this.setEmployees(employees);
        return this;
    }

    public void setEmployees(Integer employees) {
        this.employees = employees;
    }

    public Boolean getVeterinaryServiceAvailable() {
        return this.veterinaryServiceAvailable;
    }

    public LivestockActivity veterinaryServiceAvailable(Boolean veterinaryServiceAvailable) {
        this.setVeterinaryServiceAvailable(veterinaryServiceAvailable);
        return this;
    }

    public void setVeterinaryServiceAvailable(Boolean veterinaryServiceAvailable) {
        this.veterinaryServiceAvailable = veterinaryServiceAvailable;
    }

    public String getFeedSource() {
        return this.feedSource;
    }

    public LivestockActivity feedSource(String feedSource) {
        this.setFeedSource(feedSource);
        return this;
    }

    public void setFeedSource(String feedSource) {
        this.feedSource = feedSource;
    }

    public String getWaterSource() {
        return this.waterSource;
    }

    public LivestockActivity waterSource(String waterSource) {
        this.setWaterSource(waterSource);
        return this;
    }

    public void setWaterSource(String waterSource) {
        this.waterSource = waterSource;
    }

    public String getCertification() {
        return this.certification;
    }

    public LivestockActivity certification(String certification) {
        this.setCertification(certification);
        return this;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }

    public String getNotes() {
        return this.notes;
    }

    public LivestockActivity notes(String notes) {
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

    public LivestockActivity location(Location location) {
        this.setLocation(location);
        return this;
    }

    public LivestockType getLivestockType() {
        return this.livestockType;
    }

    public void setLivestockType(LivestockType livestockType) {
        this.livestockType = livestockType;
    }

    public LivestockActivity livestockType(LivestockType livestockType) {
        this.setLivestockType(livestockType);
        return this;
    }

    public EconomicActivity getEconomicActivity() {
        return this.economicActivity;
    }

    public void setEconomicActivity(EconomicActivity economicActivity) {
        if (this.economicActivity != null) {
            this.economicActivity.setLivestockActivity(null);
        }
        if (economicActivity != null) {
            economicActivity.setLivestockActivity(this);
        }
        this.economicActivity = economicActivity;
    }

    public LivestockActivity economicActivity(EconomicActivity economicActivity) {
        this.setEconomicActivity(economicActivity);
        return this;
    }

    public Set<LivestockProduction> getProductionses() {
        return this.productionses;
    }

    public void setProductionses(Set<LivestockProduction> livestockProductions) {
        if (this.productionses != null) {
            this.productionses.forEach(i -> i.setLivestockActivity(null));
        }
        if (livestockProductions != null) {
            livestockProductions.forEach(i -> i.setLivestockActivity(this));
        }
        this.productionses = livestockProductions;
    }

    public LivestockActivity productionses(Set<LivestockProduction> livestockProductions) {
        this.setProductionses(livestockProductions);
        return this;
    }

    public LivestockActivity addProductions(LivestockProduction livestockProduction) {
        this.productionses.add(livestockProduction);
        livestockProduction.setLivestockActivity(this);
        return this;
    }

    public LivestockActivity removeProductions(LivestockProduction livestockProduction) {
        this.productionses.remove(livestockProduction);
        livestockProduction.setLivestockActivity(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LivestockActivity)) {
            return false;
        }
        return getId() != null && getId().equals(((LivestockActivity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LivestockActivity{" +
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
            "}";
    }
}
