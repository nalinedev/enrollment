package com.naline.coopfull.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.naline.coopfull.domain.enumeration.AgriculturalExploitationMode;
import com.naline.coopfull.domain.enumeration.LandOwnershipType;
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
 * A AgriculturalActivity.
 */
@Entity
@Table(name = "agricultural_activity")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AgriculturalActivity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "total_area", precision = 21, scale = 2, nullable = false)
    private BigDecimal totalArea;

    @NotNull
    @Column(name = "area_unit", nullable = false)
    private String areaUnit;

    @Enumerated(EnumType.STRING)
    @Column(name = "exploitation_mode")
    private AgriculturalExploitationMode exploitationMode;

    @Enumerated(EnumType.STRING)
    @Column(name = "ownership_type")
    private LandOwnershipType ownershipType;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "irrigation_available")
    private Boolean irrigationAvailable;

    @Column(name = "organic_production")
    private Boolean organicProduction;

    @Column(name = "certification")
    private String certification;

    @Lob
    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "parent" }, allowSetters = true)
    private Location location;

    @JsonIgnoreProperties(
        value = { "agriculturalActivity", "livestockActivity", "aquacultureActivity", "member", "activityType", "location" },
        allowSetters = true
    )
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "agriculturalActivity")
    private EconomicActivity economicActivity;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "agriculturalActivity")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "agriculturalActivity", "crop", "cropVariety" }, allowSetters = true)
    private Set<AgriculturalProduction> productionses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AgriculturalActivity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getTotalArea() {
        return this.totalArea;
    }

    public AgriculturalActivity totalArea(BigDecimal totalArea) {
        this.setTotalArea(totalArea);
        return this;
    }

    public void setTotalArea(BigDecimal totalArea) {
        this.totalArea = totalArea;
    }

    public String getAreaUnit() {
        return this.areaUnit;
    }

    public AgriculturalActivity areaUnit(String areaUnit) {
        this.setAreaUnit(areaUnit);
        return this;
    }

    public void setAreaUnit(String areaUnit) {
        this.areaUnit = areaUnit;
    }

    public AgriculturalExploitationMode getExploitationMode() {
        return this.exploitationMode;
    }

    public AgriculturalActivity exploitationMode(AgriculturalExploitationMode exploitationMode) {
        this.setExploitationMode(exploitationMode);
        return this;
    }

    public void setExploitationMode(AgriculturalExploitationMode exploitationMode) {
        this.exploitationMode = exploitationMode;
    }

    public LandOwnershipType getOwnershipType() {
        return this.ownershipType;
    }

    public AgriculturalActivity ownershipType(LandOwnershipType ownershipType) {
        this.setOwnershipType(ownershipType);
        return this;
    }

    public void setOwnershipType(LandOwnershipType ownershipType) {
        this.ownershipType = ownershipType;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public AgriculturalActivity startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Boolean getIrrigationAvailable() {
        return this.irrigationAvailable;
    }

    public AgriculturalActivity irrigationAvailable(Boolean irrigationAvailable) {
        this.setIrrigationAvailable(irrigationAvailable);
        return this;
    }

    public void setIrrigationAvailable(Boolean irrigationAvailable) {
        this.irrigationAvailable = irrigationAvailable;
    }

    public Boolean getOrganicProduction() {
        return this.organicProduction;
    }

    public AgriculturalActivity organicProduction(Boolean organicProduction) {
        this.setOrganicProduction(organicProduction);
        return this;
    }

    public void setOrganicProduction(Boolean organicProduction) {
        this.organicProduction = organicProduction;
    }

    public String getCertification() {
        return this.certification;
    }

    public AgriculturalActivity certification(String certification) {
        this.setCertification(certification);
        return this;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }

    public String getDescription() {
        return this.description;
    }

    public AgriculturalActivity description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public AgriculturalActivity location(Location location) {
        this.setLocation(location);
        return this;
    }

    public EconomicActivity getEconomicActivity() {
        return this.economicActivity;
    }

    public void setEconomicActivity(EconomicActivity economicActivity) {
        if (this.economicActivity != null) {
            this.economicActivity.setAgriculturalActivity(null);
        }
        if (economicActivity != null) {
            economicActivity.setAgriculturalActivity(this);
        }
        this.economicActivity = economicActivity;
    }

    public AgriculturalActivity economicActivity(EconomicActivity economicActivity) {
        this.setEconomicActivity(economicActivity);
        return this;
    }

    public Set<AgriculturalProduction> getProductionses() {
        return this.productionses;
    }

    public void setProductionses(Set<AgriculturalProduction> agriculturalProductions) {
        if (this.productionses != null) {
            this.productionses.forEach(i -> i.setAgriculturalActivity(null));
        }
        if (agriculturalProductions != null) {
            agriculturalProductions.forEach(i -> i.setAgriculturalActivity(this));
        }
        this.productionses = agriculturalProductions;
    }

    public AgriculturalActivity productionses(Set<AgriculturalProduction> agriculturalProductions) {
        this.setProductionses(agriculturalProductions);
        return this;
    }

    public AgriculturalActivity addProductions(AgriculturalProduction agriculturalProduction) {
        this.productionses.add(agriculturalProduction);
        agriculturalProduction.setAgriculturalActivity(this);
        return this;
    }

    public AgriculturalActivity removeProductions(AgriculturalProduction agriculturalProduction) {
        this.productionses.remove(agriculturalProduction);
        agriculturalProduction.setAgriculturalActivity(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AgriculturalActivity)) {
            return false;
        }
        return getId() != null && getId().equals(((AgriculturalActivity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AgriculturalActivity{" +
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
            "}";
    }
}
