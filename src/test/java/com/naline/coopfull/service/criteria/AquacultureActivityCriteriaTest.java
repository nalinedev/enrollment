package com.naline.coopfull.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AquacultureActivityCriteriaTest {

    @Test
    void newAquacultureActivityCriteriaHasAllFiltersNullTest() {
        var aquacultureActivityCriteria = new AquacultureActivityCriteria();
        assertThat(aquacultureActivityCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void aquacultureActivityCriteriaFluentMethodsCreatesFiltersTest() {
        var aquacultureActivityCriteria = new AquacultureActivityCriteria();

        setAllFilters(aquacultureActivityCriteria);

        assertThat(aquacultureActivityCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void aquacultureActivityCriteriaCopyCreatesNullFilterTest() {
        var aquacultureActivityCriteria = new AquacultureActivityCriteria();
        var copy = aquacultureActivityCriteria.copy();

        assertThat(aquacultureActivityCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(aquacultureActivityCriteria)
        );
    }

    @Test
    void aquacultureActivityCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var aquacultureActivityCriteria = new AquacultureActivityCriteria();
        setAllFilters(aquacultureActivityCriteria);

        var copy = aquacultureActivityCriteria.copy();

        assertThat(aquacultureActivityCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(aquacultureActivityCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var aquacultureActivityCriteria = new AquacultureActivityCriteria();

        assertThat(aquacultureActivityCriteria).hasToString("AquacultureActivityCriteria{}");
    }

    private static void setAllFilters(AquacultureActivityCriteria aquacultureActivityCriteria) {
        aquacultureActivityCriteria.id();
        aquacultureActivityCriteria.name();
        aquacultureActivityCriteria.productionMode();
        aquacultureActivityCriteria.ownershipType();
        aquacultureActivityCriteria.productionType();
        aquacultureActivityCriteria.systemType();
        aquacultureActivityCriteria.startDate();
        aquacultureActivityCriteria.totalArea();
        aquacultureActivityCriteria.areaUnit();
        aquacultureActivityCriteria.waterSource();
        aquacultureActivityCriteria.numberOfProductionUnits();
        aquacultureActivityCriteria.productionUnitDescription();
        aquacultureActivityCriteria.status();
        aquacultureActivityCriteria.annualRevenue();
        aquacultureActivityCriteria.monthlyRevenue();
        aquacultureActivityCriteria.employees();
        aquacultureActivityCriteria.certification();
        aquacultureActivityCriteria.locationId();
        aquacultureActivityCriteria.aquaticSpeciesId();
        aquacultureActivityCriteria.economicActivityId();
        aquacultureActivityCriteria.productionsId();
        aquacultureActivityCriteria.distinct();
    }

    private static Condition<AquacultureActivityCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getProductionMode()) &&
                condition.apply(criteria.getOwnershipType()) &&
                condition.apply(criteria.getProductionType()) &&
                condition.apply(criteria.getSystemType()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getTotalArea()) &&
                condition.apply(criteria.getAreaUnit()) &&
                condition.apply(criteria.getWaterSource()) &&
                condition.apply(criteria.getNumberOfProductionUnits()) &&
                condition.apply(criteria.getProductionUnitDescription()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getAnnualRevenue()) &&
                condition.apply(criteria.getMonthlyRevenue()) &&
                condition.apply(criteria.getEmployees()) &&
                condition.apply(criteria.getCertification()) &&
                condition.apply(criteria.getLocationId()) &&
                condition.apply(criteria.getAquaticSpeciesId()) &&
                condition.apply(criteria.getEconomicActivityId()) &&
                condition.apply(criteria.getProductionsId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AquacultureActivityCriteria> copyFiltersAre(
        AquacultureActivityCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getProductionMode(), copy.getProductionMode()) &&
                condition.apply(criteria.getOwnershipType(), copy.getOwnershipType()) &&
                condition.apply(criteria.getProductionType(), copy.getProductionType()) &&
                condition.apply(criteria.getSystemType(), copy.getSystemType()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getTotalArea(), copy.getTotalArea()) &&
                condition.apply(criteria.getAreaUnit(), copy.getAreaUnit()) &&
                condition.apply(criteria.getWaterSource(), copy.getWaterSource()) &&
                condition.apply(criteria.getNumberOfProductionUnits(), copy.getNumberOfProductionUnits()) &&
                condition.apply(criteria.getProductionUnitDescription(), copy.getProductionUnitDescription()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getAnnualRevenue(), copy.getAnnualRevenue()) &&
                condition.apply(criteria.getMonthlyRevenue(), copy.getMonthlyRevenue()) &&
                condition.apply(criteria.getEmployees(), copy.getEmployees()) &&
                condition.apply(criteria.getCertification(), copy.getCertification()) &&
                condition.apply(criteria.getLocationId(), copy.getLocationId()) &&
                condition.apply(criteria.getAquaticSpeciesId(), copy.getAquaticSpeciesId()) &&
                condition.apply(criteria.getEconomicActivityId(), copy.getEconomicActivityId()) &&
                condition.apply(criteria.getProductionsId(), copy.getProductionsId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
