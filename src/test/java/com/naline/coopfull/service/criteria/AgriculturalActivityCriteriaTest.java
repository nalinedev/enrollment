package com.naline.coopfull.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AgriculturalActivityCriteriaTest {

    @Test
    void newAgriculturalActivityCriteriaHasAllFiltersNullTest() {
        var agriculturalActivityCriteria = new AgriculturalActivityCriteria();
        assertThat(agriculturalActivityCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void agriculturalActivityCriteriaFluentMethodsCreatesFiltersTest() {
        var agriculturalActivityCriteria = new AgriculturalActivityCriteria();

        setAllFilters(agriculturalActivityCriteria);

        assertThat(agriculturalActivityCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void agriculturalActivityCriteriaCopyCreatesNullFilterTest() {
        var agriculturalActivityCriteria = new AgriculturalActivityCriteria();
        var copy = agriculturalActivityCriteria.copy();

        assertThat(agriculturalActivityCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(agriculturalActivityCriteria)
        );
    }

    @Test
    void agriculturalActivityCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var agriculturalActivityCriteria = new AgriculturalActivityCriteria();
        setAllFilters(agriculturalActivityCriteria);

        var copy = agriculturalActivityCriteria.copy();

        assertThat(agriculturalActivityCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(agriculturalActivityCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var agriculturalActivityCriteria = new AgriculturalActivityCriteria();

        assertThat(agriculturalActivityCriteria).hasToString("AgriculturalActivityCriteria{}");
    }

    private static void setAllFilters(AgriculturalActivityCriteria agriculturalActivityCriteria) {
        agriculturalActivityCriteria.id();
        agriculturalActivityCriteria.totalArea();
        agriculturalActivityCriteria.areaUnit();
        agriculturalActivityCriteria.exploitationMode();
        agriculturalActivityCriteria.ownershipType();
        agriculturalActivityCriteria.startDate();
        agriculturalActivityCriteria.irrigationAvailable();
        agriculturalActivityCriteria.organicProduction();
        agriculturalActivityCriteria.certification();
        agriculturalActivityCriteria.locationId();
        agriculturalActivityCriteria.economicActivityId();
        agriculturalActivityCriteria.productionsId();
        agriculturalActivityCriteria.distinct();
    }

    private static Condition<AgriculturalActivityCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTotalArea()) &&
                condition.apply(criteria.getAreaUnit()) &&
                condition.apply(criteria.getExploitationMode()) &&
                condition.apply(criteria.getOwnershipType()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getIrrigationAvailable()) &&
                condition.apply(criteria.getOrganicProduction()) &&
                condition.apply(criteria.getCertification()) &&
                condition.apply(criteria.getLocationId()) &&
                condition.apply(criteria.getEconomicActivityId()) &&
                condition.apply(criteria.getProductionsId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AgriculturalActivityCriteria> copyFiltersAre(
        AgriculturalActivityCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTotalArea(), copy.getTotalArea()) &&
                condition.apply(criteria.getAreaUnit(), copy.getAreaUnit()) &&
                condition.apply(criteria.getExploitationMode(), copy.getExploitationMode()) &&
                condition.apply(criteria.getOwnershipType(), copy.getOwnershipType()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getIrrigationAvailable(), copy.getIrrigationAvailable()) &&
                condition.apply(criteria.getOrganicProduction(), copy.getOrganicProduction()) &&
                condition.apply(criteria.getCertification(), copy.getCertification()) &&
                condition.apply(criteria.getLocationId(), copy.getLocationId()) &&
                condition.apply(criteria.getEconomicActivityId(), copy.getEconomicActivityId()) &&
                condition.apply(criteria.getProductionsId(), copy.getProductionsId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
