package com.naline.coopfull.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.naline.coopfull.domain.enumeration.EconomicActivityStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EconomicActivity.
 */
@Entity
@Table(name = "economic_activity")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EconomicActivity implements Serializable {

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

    @NotNull
    @Column(name = "main_activity", nullable = false)
    private Boolean mainActivity;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "annual_revenue", precision = 21, scale = 2)
    private BigDecimal annualRevenue;

    @Column(name = "monthly_revenue", precision = 21, scale = 2)
    private BigDecimal monthlyRevenue;

    @Column(name = "number_of_employees")
    private Integer numberOfEmployees;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EconomicActivityStatus status;

    @Lob
    @Column(name = "notes")
    private String notes;

    @JsonIgnoreProperties(value = { "location", "economicActivity", "productionses" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private AgriculturalActivity agriculturalActivity;

    @JsonIgnoreProperties(value = { "location", "livestockType", "economicActivity", "productionses" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private LivestockActivity livestockActivity;

    @JsonIgnoreProperties(value = { "location", "aquaticSpecies", "economicActivity", "productionses" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private AquacultureActivity aquacultureActivity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "individualMember", "organizationMember", "socialProfile", "professionalProfile", "cooperative", "branch" },
        allowSetters = true
    )
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private EconomicActivityType activityType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "parent" }, allowSetters = true)
    private Location location;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EconomicActivity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public EconomicActivity name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public EconomicActivity description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getMainActivity() {
        return this.mainActivity;
    }

    public EconomicActivity mainActivity(Boolean mainActivity) {
        this.setMainActivity(mainActivity);
        return this;
    }

    public void setMainActivity(Boolean mainActivity) {
        this.mainActivity = mainActivity;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public EconomicActivity startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public EconomicActivity endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getAnnualRevenue() {
        return this.annualRevenue;
    }

    public EconomicActivity annualRevenue(BigDecimal annualRevenue) {
        this.setAnnualRevenue(annualRevenue);
        return this;
    }

    public void setAnnualRevenue(BigDecimal annualRevenue) {
        this.annualRevenue = annualRevenue;
    }

    public BigDecimal getMonthlyRevenue() {
        return this.monthlyRevenue;
    }

    public EconomicActivity monthlyRevenue(BigDecimal monthlyRevenue) {
        this.setMonthlyRevenue(monthlyRevenue);
        return this;
    }

    public void setMonthlyRevenue(BigDecimal monthlyRevenue) {
        this.monthlyRevenue = monthlyRevenue;
    }

    public Integer getNumberOfEmployees() {
        return this.numberOfEmployees;
    }

    public EconomicActivity numberOfEmployees(Integer numberOfEmployees) {
        this.setNumberOfEmployees(numberOfEmployees);
        return this;
    }

    public void setNumberOfEmployees(Integer numberOfEmployees) {
        this.numberOfEmployees = numberOfEmployees;
    }

    public EconomicActivityStatus getStatus() {
        return this.status;
    }

    public EconomicActivity status(EconomicActivityStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(EconomicActivityStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return this.notes;
    }

    public EconomicActivity notes(String notes) {
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

    public EconomicActivity agriculturalActivity(AgriculturalActivity agriculturalActivity) {
        this.setAgriculturalActivity(agriculturalActivity);
        return this;
    }

    public LivestockActivity getLivestockActivity() {
        return this.livestockActivity;
    }

    public void setLivestockActivity(LivestockActivity livestockActivity) {
        this.livestockActivity = livestockActivity;
    }

    public EconomicActivity livestockActivity(LivestockActivity livestockActivity) {
        this.setLivestockActivity(livestockActivity);
        return this;
    }

    public AquacultureActivity getAquacultureActivity() {
        return this.aquacultureActivity;
    }

    public void setAquacultureActivity(AquacultureActivity aquacultureActivity) {
        this.aquacultureActivity = aquacultureActivity;
    }

    public EconomicActivity aquacultureActivity(AquacultureActivity aquacultureActivity) {
        this.setAquacultureActivity(aquacultureActivity);
        return this;
    }

    public Member getMember() {
        return this.member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public EconomicActivity member(Member member) {
        this.setMember(member);
        return this;
    }

    public EconomicActivityType getActivityType() {
        return this.activityType;
    }

    public void setActivityType(EconomicActivityType economicActivityType) {
        this.activityType = economicActivityType;
    }

    public EconomicActivity activityType(EconomicActivityType economicActivityType) {
        this.setActivityType(economicActivityType);
        return this;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public EconomicActivity location(Location location) {
        this.setLocation(location);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EconomicActivity)) {
            return false;
        }
        return getId() != null && getId().equals(((EconomicActivity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EconomicActivity{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", mainActivity='" + getMainActivity() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", annualRevenue=" + getAnnualRevenue() +
            ", monthlyRevenue=" + getMonthlyRevenue() +
            ", numberOfEmployees=" + getNumberOfEmployees() +
            ", status='" + getStatus() + "'" +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
