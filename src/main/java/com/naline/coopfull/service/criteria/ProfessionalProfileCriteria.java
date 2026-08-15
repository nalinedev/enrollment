package com.naline.coopfull.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.naline.coopfull.domain.ProfessionalProfile} entity. This class is used
 * in {@link com.naline.coopfull.web.rest.ProfessionalProfileResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /professional-profiles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfessionalProfileCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter employmentStatus;

    private StringFilter employerName;

    private StringFilter jobTitle;

    private StringFilter profession;

    private StringFilter sector;

    private IntegerFilter yearsOfExperience;

    private BigDecimalFilter monthlyIncome;

    private BigDecimalFilter annualIncome;

    private LocalDateFilter employmentStartDate;

    private StringFilter employerLocation;

    private LongFilter memberId;

    private Boolean distinct;

    public ProfessionalProfileCriteria() {}

    public ProfessionalProfileCriteria(ProfessionalProfileCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.employmentStatus = other.optionalEmploymentStatus().map(StringFilter::copy).orElse(null);
        this.employerName = other.optionalEmployerName().map(StringFilter::copy).orElse(null);
        this.jobTitle = other.optionalJobTitle().map(StringFilter::copy).orElse(null);
        this.profession = other.optionalProfession().map(StringFilter::copy).orElse(null);
        this.sector = other.optionalSector().map(StringFilter::copy).orElse(null);
        this.yearsOfExperience = other.optionalYearsOfExperience().map(IntegerFilter::copy).orElse(null);
        this.monthlyIncome = other.optionalMonthlyIncome().map(BigDecimalFilter::copy).orElse(null);
        this.annualIncome = other.optionalAnnualIncome().map(BigDecimalFilter::copy).orElse(null);
        this.employmentStartDate = other.optionalEmploymentStartDate().map(LocalDateFilter::copy).orElse(null);
        this.employerLocation = other.optionalEmployerLocation().map(StringFilter::copy).orElse(null);
        this.memberId = other.optionalMemberId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ProfessionalProfileCriteria copy() {
        return new ProfessionalProfileCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getEmploymentStatus() {
        return employmentStatus;
    }

    public Optional<StringFilter> optionalEmploymentStatus() {
        return Optional.ofNullable(employmentStatus);
    }

    public StringFilter employmentStatus() {
        if (employmentStatus == null) {
            setEmploymentStatus(new StringFilter());
        }
        return employmentStatus;
    }

    public void setEmploymentStatus(StringFilter employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public StringFilter getEmployerName() {
        return employerName;
    }

    public Optional<StringFilter> optionalEmployerName() {
        return Optional.ofNullable(employerName);
    }

    public StringFilter employerName() {
        if (employerName == null) {
            setEmployerName(new StringFilter());
        }
        return employerName;
    }

    public void setEmployerName(StringFilter employerName) {
        this.employerName = employerName;
    }

    public StringFilter getJobTitle() {
        return jobTitle;
    }

    public Optional<StringFilter> optionalJobTitle() {
        return Optional.ofNullable(jobTitle);
    }

    public StringFilter jobTitle() {
        if (jobTitle == null) {
            setJobTitle(new StringFilter());
        }
        return jobTitle;
    }

    public void setJobTitle(StringFilter jobTitle) {
        this.jobTitle = jobTitle;
    }

    public StringFilter getProfession() {
        return profession;
    }

    public Optional<StringFilter> optionalProfession() {
        return Optional.ofNullable(profession);
    }

    public StringFilter profession() {
        if (profession == null) {
            setProfession(new StringFilter());
        }
        return profession;
    }

    public void setProfession(StringFilter profession) {
        this.profession = profession;
    }

    public StringFilter getSector() {
        return sector;
    }

    public Optional<StringFilter> optionalSector() {
        return Optional.ofNullable(sector);
    }

    public StringFilter sector() {
        if (sector == null) {
            setSector(new StringFilter());
        }
        return sector;
    }

    public void setSector(StringFilter sector) {
        this.sector = sector;
    }

    public IntegerFilter getYearsOfExperience() {
        return yearsOfExperience;
    }

    public Optional<IntegerFilter> optionalYearsOfExperience() {
        return Optional.ofNullable(yearsOfExperience);
    }

    public IntegerFilter yearsOfExperience() {
        if (yearsOfExperience == null) {
            setYearsOfExperience(new IntegerFilter());
        }
        return yearsOfExperience;
    }

    public void setYearsOfExperience(IntegerFilter yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public BigDecimalFilter getMonthlyIncome() {
        return monthlyIncome;
    }

    public Optional<BigDecimalFilter> optionalMonthlyIncome() {
        return Optional.ofNullable(monthlyIncome);
    }

    public BigDecimalFilter monthlyIncome() {
        if (monthlyIncome == null) {
            setMonthlyIncome(new BigDecimalFilter());
        }
        return monthlyIncome;
    }

    public void setMonthlyIncome(BigDecimalFilter monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public BigDecimalFilter getAnnualIncome() {
        return annualIncome;
    }

    public Optional<BigDecimalFilter> optionalAnnualIncome() {
        return Optional.ofNullable(annualIncome);
    }

    public BigDecimalFilter annualIncome() {
        if (annualIncome == null) {
            setAnnualIncome(new BigDecimalFilter());
        }
        return annualIncome;
    }

    public void setAnnualIncome(BigDecimalFilter annualIncome) {
        this.annualIncome = annualIncome;
    }

    public LocalDateFilter getEmploymentStartDate() {
        return employmentStartDate;
    }

    public Optional<LocalDateFilter> optionalEmploymentStartDate() {
        return Optional.ofNullable(employmentStartDate);
    }

    public LocalDateFilter employmentStartDate() {
        if (employmentStartDate == null) {
            setEmploymentStartDate(new LocalDateFilter());
        }
        return employmentStartDate;
    }

    public void setEmploymentStartDate(LocalDateFilter employmentStartDate) {
        this.employmentStartDate = employmentStartDate;
    }

    public StringFilter getEmployerLocation() {
        return employerLocation;
    }

    public Optional<StringFilter> optionalEmployerLocation() {
        return Optional.ofNullable(employerLocation);
    }

    public StringFilter employerLocation() {
        if (employerLocation == null) {
            setEmployerLocation(new StringFilter());
        }
        return employerLocation;
    }

    public void setEmployerLocation(StringFilter employerLocation) {
        this.employerLocation = employerLocation;
    }

    public LongFilter getMemberId() {
        return memberId;
    }

    public Optional<LongFilter> optionalMemberId() {
        return Optional.ofNullable(memberId);
    }

    public LongFilter memberId() {
        if (memberId == null) {
            setMemberId(new LongFilter());
        }
        return memberId;
    }

    public void setMemberId(LongFilter memberId) {
        this.memberId = memberId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProfessionalProfileCriteria that = (ProfessionalProfileCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(employmentStatus, that.employmentStatus) &&
            Objects.equals(employerName, that.employerName) &&
            Objects.equals(jobTitle, that.jobTitle) &&
            Objects.equals(profession, that.profession) &&
            Objects.equals(sector, that.sector) &&
            Objects.equals(yearsOfExperience, that.yearsOfExperience) &&
            Objects.equals(monthlyIncome, that.monthlyIncome) &&
            Objects.equals(annualIncome, that.annualIncome) &&
            Objects.equals(employmentStartDate, that.employmentStartDate) &&
            Objects.equals(employerLocation, that.employerLocation) &&
            Objects.equals(memberId, that.memberId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            employmentStatus,
            employerName,
            jobTitle,
            profession,
            sector,
            yearsOfExperience,
            monthlyIncome,
            annualIncome,
            employmentStartDate,
            employerLocation,
            memberId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfessionalProfileCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalEmploymentStatus().map(f -> "employmentStatus=" + f + ", ").orElse("") +
            optionalEmployerName().map(f -> "employerName=" + f + ", ").orElse("") +
            optionalJobTitle().map(f -> "jobTitle=" + f + ", ").orElse("") +
            optionalProfession().map(f -> "profession=" + f + ", ").orElse("") +
            optionalSector().map(f -> "sector=" + f + ", ").orElse("") +
            optionalYearsOfExperience().map(f -> "yearsOfExperience=" + f + ", ").orElse("") +
            optionalMonthlyIncome().map(f -> "monthlyIncome=" + f + ", ").orElse("") +
            optionalAnnualIncome().map(f -> "annualIncome=" + f + ", ").orElse("") +
            optionalEmploymentStartDate().map(f -> "employmentStartDate=" + f + ", ").orElse("") +
            optionalEmployerLocation().map(f -> "employerLocation=" + f + ", ").orElse("") +
            optionalMemberId().map(f -> "memberId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
