package com.naline.coopfull.service.dto;

import jakarta.persistence.Lob;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.naline.coopfull.domain.ProfessionalProfile} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfessionalProfileDTO implements Serializable {

    private Long id;

    private String employmentStatus;

    private String employerName;

    private String jobTitle;

    private String profession;

    private String sector;

    private Integer yearsOfExperience;

    private BigDecimal monthlyIncome;

    private BigDecimal annualIncome;

    private LocalDate employmentStartDate;

    private String employerLocation;

    @Lob
    private String notes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public BigDecimal getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(BigDecimal monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public BigDecimal getAnnualIncome() {
        return annualIncome;
    }

    public void setAnnualIncome(BigDecimal annualIncome) {
        this.annualIncome = annualIncome;
    }

    public LocalDate getEmploymentStartDate() {
        return employmentStartDate;
    }

    public void setEmploymentStartDate(LocalDate employmentStartDate) {
        this.employmentStartDate = employmentStartDate;
    }

    public String getEmployerLocation() {
        return employerLocation;
    }

    public void setEmployerLocation(String employerLocation) {
        this.employerLocation = employerLocation;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfessionalProfileDTO)) {
            return false;
        }

        ProfessionalProfileDTO professionalProfileDTO = (ProfessionalProfileDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, professionalProfileDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfessionalProfileDTO{" +
            "id=" + getId() +
            ", employmentStatus='" + getEmploymentStatus() + "'" +
            ", employerName='" + getEmployerName() + "'" +
            ", jobTitle='" + getJobTitle() + "'" +
            ", profession='" + getProfession() + "'" +
            ", sector='" + getSector() + "'" +
            ", yearsOfExperience=" + getYearsOfExperience() +
            ", monthlyIncome=" + getMonthlyIncome() +
            ", annualIncome=" + getAnnualIncome() +
            ", employmentStartDate='" + getEmploymentStartDate() + "'" +
            ", employerLocation='" + getEmployerLocation() + "'" +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
