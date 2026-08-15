package com.naline.coopfull.service.criteria;

import com.naline.coopfull.domain.enumeration.AgriculturalExploitationMode;
import com.naline.coopfull.domain.enumeration.LandOwnershipType;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.naline.coopfull.domain.AgriculturalActivity} entity. This class is used
 * in {@link com.naline.coopfull.web.rest.AgriculturalActivityResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /agricultural-activities?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AgriculturalActivityCriteria implements Serializable, Criteria {

    /**
     * Class for filtering AgriculturalExploitationMode
     */
    public static class AgriculturalExploitationModeFilter extends Filter<AgriculturalExploitationMode> {

        public AgriculturalExploitationModeFilter() {}

        public AgriculturalExploitationModeFilter(AgriculturalExploitationModeFilter filter) {
            super(filter);
        }

        @Override
        public AgriculturalExploitationModeFilter copy() {
            return new AgriculturalExploitationModeFilter(this);
        }
    }

    /**
     * Class for filtering LandOwnershipType
     */
    public static class LandOwnershipTypeFilter extends Filter<LandOwnershipType> {

        public LandOwnershipTypeFilter() {}

        public LandOwnershipTypeFilter(LandOwnershipTypeFilter filter) {
            super(filter);
        }

        @Override
        public LandOwnershipTypeFilter copy() {
            return new LandOwnershipTypeFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter totalArea;

    private StringFilter areaUnit;

    private AgriculturalExploitationModeFilter exploitationMode;

    private LandOwnershipTypeFilter ownershipType;

    private LocalDateFilter startDate;

    private BooleanFilter irrigationAvailable;

    private BooleanFilter organicProduction;

    private StringFilter certification;

    private LongFilter locationId;

    private LongFilter economicActivityId;

    private LongFilter productionsId;

    private Boolean distinct;

    public AgriculturalActivityCriteria() {}

    public AgriculturalActivityCriteria(AgriculturalActivityCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.totalArea = other.optionalTotalArea().map(BigDecimalFilter::copy).orElse(null);
        this.areaUnit = other.optionalAreaUnit().map(StringFilter::copy).orElse(null);
        this.exploitationMode = other.optionalExploitationMode().map(AgriculturalExploitationModeFilter::copy).orElse(null);
        this.ownershipType = other.optionalOwnershipType().map(LandOwnershipTypeFilter::copy).orElse(null);
        this.startDate = other.optionalStartDate().map(LocalDateFilter::copy).orElse(null);
        this.irrigationAvailable = other.optionalIrrigationAvailable().map(BooleanFilter::copy).orElse(null);
        this.organicProduction = other.optionalOrganicProduction().map(BooleanFilter::copy).orElse(null);
        this.certification = other.optionalCertification().map(StringFilter::copy).orElse(null);
        this.locationId = other.optionalLocationId().map(LongFilter::copy).orElse(null);
        this.economicActivityId = other.optionalEconomicActivityId().map(LongFilter::copy).orElse(null);
        this.productionsId = other.optionalProductionsId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AgriculturalActivityCriteria copy() {
        return new AgriculturalActivityCriteria(this);
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

    public BigDecimalFilter getTotalArea() {
        return totalArea;
    }

    public Optional<BigDecimalFilter> optionalTotalArea() {
        return Optional.ofNullable(totalArea);
    }

    public BigDecimalFilter totalArea() {
        if (totalArea == null) {
            setTotalArea(new BigDecimalFilter());
        }
        return totalArea;
    }

    public void setTotalArea(BigDecimalFilter totalArea) {
        this.totalArea = totalArea;
    }

    public StringFilter getAreaUnit() {
        return areaUnit;
    }

    public Optional<StringFilter> optionalAreaUnit() {
        return Optional.ofNullable(areaUnit);
    }

    public StringFilter areaUnit() {
        if (areaUnit == null) {
            setAreaUnit(new StringFilter());
        }
        return areaUnit;
    }

    public void setAreaUnit(StringFilter areaUnit) {
        this.areaUnit = areaUnit;
    }

    public AgriculturalExploitationModeFilter getExploitationMode() {
        return exploitationMode;
    }

    public Optional<AgriculturalExploitationModeFilter> optionalExploitationMode() {
        return Optional.ofNullable(exploitationMode);
    }

    public AgriculturalExploitationModeFilter exploitationMode() {
        if (exploitationMode == null) {
            setExploitationMode(new AgriculturalExploitationModeFilter());
        }
        return exploitationMode;
    }

    public void setExploitationMode(AgriculturalExploitationModeFilter exploitationMode) {
        this.exploitationMode = exploitationMode;
    }

    public LandOwnershipTypeFilter getOwnershipType() {
        return ownershipType;
    }

    public Optional<LandOwnershipTypeFilter> optionalOwnershipType() {
        return Optional.ofNullable(ownershipType);
    }

    public LandOwnershipTypeFilter ownershipType() {
        if (ownershipType == null) {
            setOwnershipType(new LandOwnershipTypeFilter());
        }
        return ownershipType;
    }

    public void setOwnershipType(LandOwnershipTypeFilter ownershipType) {
        this.ownershipType = ownershipType;
    }

    public LocalDateFilter getStartDate() {
        return startDate;
    }

    public Optional<LocalDateFilter> optionalStartDate() {
        return Optional.ofNullable(startDate);
    }

    public LocalDateFilter startDate() {
        if (startDate == null) {
            setStartDate(new LocalDateFilter());
        }
        return startDate;
    }

    public void setStartDate(LocalDateFilter startDate) {
        this.startDate = startDate;
    }

    public BooleanFilter getIrrigationAvailable() {
        return irrigationAvailable;
    }

    public Optional<BooleanFilter> optionalIrrigationAvailable() {
        return Optional.ofNullable(irrigationAvailable);
    }

    public BooleanFilter irrigationAvailable() {
        if (irrigationAvailable == null) {
            setIrrigationAvailable(new BooleanFilter());
        }
        return irrigationAvailable;
    }

    public void setIrrigationAvailable(BooleanFilter irrigationAvailable) {
        this.irrigationAvailable = irrigationAvailable;
    }

    public BooleanFilter getOrganicProduction() {
        return organicProduction;
    }

    public Optional<BooleanFilter> optionalOrganicProduction() {
        return Optional.ofNullable(organicProduction);
    }

    public BooleanFilter organicProduction() {
        if (organicProduction == null) {
            setOrganicProduction(new BooleanFilter());
        }
        return organicProduction;
    }

    public void setOrganicProduction(BooleanFilter organicProduction) {
        this.organicProduction = organicProduction;
    }

    public StringFilter getCertification() {
        return certification;
    }

    public Optional<StringFilter> optionalCertification() {
        return Optional.ofNullable(certification);
    }

    public StringFilter certification() {
        if (certification == null) {
            setCertification(new StringFilter());
        }
        return certification;
    }

    public void setCertification(StringFilter certification) {
        this.certification = certification;
    }

    public LongFilter getLocationId() {
        return locationId;
    }

    public Optional<LongFilter> optionalLocationId() {
        return Optional.ofNullable(locationId);
    }

    public LongFilter locationId() {
        if (locationId == null) {
            setLocationId(new LongFilter());
        }
        return locationId;
    }

    public void setLocationId(LongFilter locationId) {
        this.locationId = locationId;
    }

    public LongFilter getEconomicActivityId() {
        return economicActivityId;
    }

    public Optional<LongFilter> optionalEconomicActivityId() {
        return Optional.ofNullable(economicActivityId);
    }

    public LongFilter economicActivityId() {
        if (economicActivityId == null) {
            setEconomicActivityId(new LongFilter());
        }
        return economicActivityId;
    }

    public void setEconomicActivityId(LongFilter economicActivityId) {
        this.economicActivityId = economicActivityId;
    }

    public LongFilter getProductionsId() {
        return productionsId;
    }

    public Optional<LongFilter> optionalProductionsId() {
        return Optional.ofNullable(productionsId);
    }

    public LongFilter productionsId() {
        if (productionsId == null) {
            setProductionsId(new LongFilter());
        }
        return productionsId;
    }

    public void setProductionsId(LongFilter productionsId) {
        this.productionsId = productionsId;
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
        final AgriculturalActivityCriteria that = (AgriculturalActivityCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(totalArea, that.totalArea) &&
            Objects.equals(areaUnit, that.areaUnit) &&
            Objects.equals(exploitationMode, that.exploitationMode) &&
            Objects.equals(ownershipType, that.ownershipType) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(irrigationAvailable, that.irrigationAvailable) &&
            Objects.equals(organicProduction, that.organicProduction) &&
            Objects.equals(certification, that.certification) &&
            Objects.equals(locationId, that.locationId) &&
            Objects.equals(economicActivityId, that.economicActivityId) &&
            Objects.equals(productionsId, that.productionsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            totalArea,
            areaUnit,
            exploitationMode,
            ownershipType,
            startDate,
            irrigationAvailable,
            organicProduction,
            certification,
            locationId,
            economicActivityId,
            productionsId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AgriculturalActivityCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTotalArea().map(f -> "totalArea=" + f + ", ").orElse("") +
            optionalAreaUnit().map(f -> "areaUnit=" + f + ", ").orElse("") +
            optionalExploitationMode().map(f -> "exploitationMode=" + f + ", ").orElse("") +
            optionalOwnershipType().map(f -> "ownershipType=" + f + ", ").orElse("") +
            optionalStartDate().map(f -> "startDate=" + f + ", ").orElse("") +
            optionalIrrigationAvailable().map(f -> "irrigationAvailable=" + f + ", ").orElse("") +
            optionalOrganicProduction().map(f -> "organicProduction=" + f + ", ").orElse("") +
            optionalCertification().map(f -> "certification=" + f + ", ").orElse("") +
            optionalLocationId().map(f -> "locationId=" + f + ", ").orElse("") +
            optionalEconomicActivityId().map(f -> "economicActivityId=" + f + ", ").orElse("") +
            optionalProductionsId().map(f -> "productionsId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
