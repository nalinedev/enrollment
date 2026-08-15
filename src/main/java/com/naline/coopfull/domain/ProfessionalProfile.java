package com.naline.coopfull.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProfessionalProfile.
 */
@Entity
@Table(name = "professional_profile")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfessionalProfile implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "employment_status")
    private String employmentStatus;

    @Column(name = "employer_name")
    private String employerName;

    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "profession")
    private String profession;

    @Column(name = "sector")
    private String sector;

    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    @Column(name = "monthly_income", precision = 21, scale = 2)
    private BigDecimal monthlyIncome;

    @Column(name = "annual_income", precision = 21, scale = 2)
    private BigDecimal annualIncome;

    @Column(name = "employment_start_date")
    private LocalDate employmentStartDate;

    @Column(name = "employer_location")
    private String employerLocation;

    @Lob
    @Column(name = "notes")
    private String notes;

    @JsonIgnoreProperties(
        value = { "individualMember", "organizationMember", "socialProfile", "professionalProfile", "cooperative", "branch" },
        allowSetters = true
    )
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "professionalProfile")
    private Member member;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProfessionalProfile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmploymentStatus() {
        return this.employmentStatus;
    }

    public ProfessionalProfile employmentStatus(String employmentStatus) {
        this.setEmploymentStatus(employmentStatus);
        return this;
    }

    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public String getEmployerName() {
        return this.employerName;
    }

    public ProfessionalProfile employerName(String employerName) {
        this.setEmployerName(employerName);
        return this;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public String getJobTitle() {
        return this.jobTitle;
    }

    public ProfessionalProfile jobTitle(String jobTitle) {
        this.setJobTitle(jobTitle);
        return this;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getProfession() {
        return this.profession;
    }

    public ProfessionalProfile profession(String profession) {
        this.setProfession(profession);
        return this;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getSector() {
        return this.sector;
    }

    public ProfessionalProfile sector(String sector) {
        this.setSector(sector);
        return this;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public Integer getYearsOfExperience() {
        return this.yearsOfExperience;
    }

    public ProfessionalProfile yearsOfExperience(Integer yearsOfExperience) {
        this.setYearsOfExperience(yearsOfExperience);
        return this;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public BigDecimal getMonthlyIncome() {
        return this.monthlyIncome;
    }

    public ProfessionalProfile monthlyIncome(BigDecimal monthlyIncome) {
        this.setMonthlyIncome(monthlyIncome);
        return this;
    }

    public void setMonthlyIncome(BigDecimal monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public BigDecimal getAnnualIncome() {
        return this.annualIncome;
    }

    public ProfessionalProfile annualIncome(BigDecimal annualIncome) {
        this.setAnnualIncome(annualIncome);
        return this;
    }

    public void setAnnualIncome(BigDecimal annualIncome) {
        this.annualIncome = annualIncome;
    }

    public LocalDate getEmploymentStartDate() {
        return this.employmentStartDate;
    }

    public ProfessionalProfile employmentStartDate(LocalDate employmentStartDate) {
        this.setEmploymentStartDate(employmentStartDate);
        return this;
    }

    public void setEmploymentStartDate(LocalDate employmentStartDate) {
        this.employmentStartDate = employmentStartDate;
    }

    public String getEmployerLocation() {
        return this.employerLocation;
    }

    public ProfessionalProfile employerLocation(String employerLocation) {
        this.setEmployerLocation(employerLocation);
        return this;
    }

    public void setEmployerLocation(String employerLocation) {
        this.employerLocation = employerLocation;
    }

    public String getNotes() {
        return this.notes;
    }

    public ProfessionalProfile notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Member getMember() {
        return this.member;
    }

    public void setMember(Member member) {
        if (this.member != null) {
            this.member.setProfessionalProfile(null);
        }
        if (member != null) {
            member.setProfessionalProfile(this);
        }
        this.member = member;
    }

    public ProfessionalProfile member(Member member) {
        this.setMember(member);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfessionalProfile)) {
            return false;
        }
        return getId() != null && getId().equals(((ProfessionalProfile) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfessionalProfile{" +
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
