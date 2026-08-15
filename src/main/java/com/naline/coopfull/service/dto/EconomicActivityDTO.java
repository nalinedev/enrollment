package com.naline.coopfull.service.dto;

import com.naline.coopfull.domain.enumeration.EconomicActivityStatus;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.naline.coopfull.domain.EconomicActivity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EconomicActivityDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @Lob
    private String description;

    @NotNull
    private Boolean mainActivity;

    private LocalDate startDate;

    private LocalDate endDate;

    private BigDecimal annualRevenue;

    private BigDecimal monthlyRevenue;

    private Integer numberOfEmployees;

    @NotNull
    private EconomicActivityStatus status;

    @Lob
    private String notes;

    private AgriculturalActivityDTO agriculturalActivity;

    private LivestockActivityDTO livestockActivity;

    private AquacultureActivityDTO aquacultureActivity;

    private MemberDTO member;

    private EconomicActivityTypeDTO activityType;

    private LocationDTO location;

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

    public Boolean getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(Boolean mainActivity) {
        this.mainActivity = mainActivity;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
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

    public Integer getNumberOfEmployees() {
        return numberOfEmployees;
    }

    public void setNumberOfEmployees(Integer numberOfEmployees) {
        this.numberOfEmployees = numberOfEmployees;
    }

    public EconomicActivityStatus getStatus() {
        return status;
    }

    public void setStatus(EconomicActivityStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public AgriculturalActivityDTO getAgriculturalActivity() {
        return agriculturalActivity;
    }

    public void setAgriculturalActivity(AgriculturalActivityDTO agriculturalActivity) {
        this.agriculturalActivity = agriculturalActivity;
    }

    public LivestockActivityDTO getLivestockActivity() {
        return livestockActivity;
    }

    public void setLivestockActivity(LivestockActivityDTO livestockActivity) {
        this.livestockActivity = livestockActivity;
    }

    public AquacultureActivityDTO getAquacultureActivity() {
        return aquacultureActivity;
    }

    public void setAquacultureActivity(AquacultureActivityDTO aquacultureActivity) {
        this.aquacultureActivity = aquacultureActivity;
    }

    public MemberDTO getMember() {
        return member;
    }

    public void setMember(MemberDTO member) {
        this.member = member;
    }

    public EconomicActivityTypeDTO getActivityType() {
        return activityType;
    }

    public void setActivityType(EconomicActivityTypeDTO activityType) {
        this.activityType = activityType;
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
        if (!(o instanceof EconomicActivityDTO)) {
            return false;
        }

        EconomicActivityDTO economicActivityDTO = (EconomicActivityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, economicActivityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EconomicActivityDTO{" +
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
            ", agriculturalActivity=" + getAgriculturalActivity() +
            ", livestockActivity=" + getLivestockActivity() +
            ", aquacultureActivity=" + getAquacultureActivity() +
            ", member=" + getMember() +
            ", activityType=" + getActivityType() +
            ", location=" + getLocation() +
            "}";
    }
}
