package com.naline.coopfull.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class LivestockActivityCriteriaTest {

    @Test
    void newLivestockActivityCriteriaHasAllFiltersNullTest() {
        var livestockActivityCriteria = new LivestockActivityCriteria();
        assertThat(livestockActivityCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void livestockActivityCriteriaFluentMethodsCreatesFiltersTest() {
        var livestockActivityCriteria = new LivestockActivityCriteria();

        setAllFilters(livestockActivityCriteria);

        assertThat(livestockActivityCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void livestockActivityCriteriaCopyCreatesNullFilterTest() {
        var livestockActivityCriteria = new LivestockActivityCriteria();
        var copy = livestockActivityCriteria.copy();

        assertThat(livestockActivityCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(livestockActivityCriteria)
        );
    }

    @Test
    void livestockActivityCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var livestockActivityCriteria = new LivestockActivityCriteria();
        setAllFilters(livestockActivityCriteria);

        var copy = livestockActivityCriteria.copy();

        assertThat(livestockActivityCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(livestockActivityCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var livestockActivityCriteria = new LivestockActivityCriteria();

        assertThat(livestockActivityCriteria).hasToString("LivestockActivityCriteria{}");
    }

    private static void setAllFilters(LivestockActivityCriteria livestockActivityCriteria) {
        livestockActivityCriteria.id();
        livestockActivityCriteria.name();
        livestockActivityCriteria.productionMode();
        livestockActivityCriteria.ownershipType();
        livestockActivityCriteria.productionType();
        livestockActivityCriteria.startDate();
        livestockActivityCriteria.totalArea();
        livestockActivityCriteria.areaUnit();
        livestockActivityCriteria.status();
        livestockActivityCriteria.numberOfAnimals();
        livestockActivityCriteria.annualRevenue();
        livestockActivityCriteria.monthlyRevenue();
        livestockActivityCriteria.employees();
        livestockActivityCriteria.veterinaryServiceAvailable();
        livestockActivityCriteria.feedSource();
        livestockActivityCriteria.waterSource();
        livestockActivityCriteria.certification();
        livestockActivityCriteria.locationId();
        livestockActivityCriteria.livestockTypeId();
        livestockActivityCriteria.economicActivityId();
        livestockActivityCriteria.productionsId();
        livestockActivityCriteria.distinct();
    }

    private static Condition<LivestockActivityCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getProductionMode()) &&
                condition.apply(criteria.getOwnershipType()) &&
                condition.apply(criteria.getProductionType()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getTotalArea()) &&
                condition.apply(criteria.getAreaUnit()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getNumberOfAnimals()) &&
                condition.apply(criteria.getAnnualRevenue()) &&
                condition.apply(criteria.getMonthlyRevenue()) &&
                condition.apply(criteria.getEmployees()) &&
                condition.apply(criteria.getVeterinaryServiceAvailable()) &&
                condition.apply(criteria.getFeedSource()) &&
                condition.apply(criteria.getWaterSource()) &&
                condition.apply(criteria.getCertification()) &&
                condition.apply(criteria.getLocationId()) &&
                condition.apply(criteria.getLivestockTypeId()) &&
                condition.apply(criteria.getEconomicActivityId()) &&
                condition.apply(criteria.getProductionsId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<LivestockActivityCriteria> copyFiltersAre(
        LivestockActivityCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getProductionMode(), copy.getProductionMode()) &&
                condition.apply(criteria.getOwnershipType(), copy.getOwnershipType()) &&
                condition.apply(criteria.getProductionType(), copy.getProductionType()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getTotalArea(), copy.getTotalArea()) &&
                condition.apply(criteria.getAreaUnit(), copy.getAreaUnit()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getNumberOfAnimals(), copy.getNumberOfAnimals()) &&
                condition.apply(criteria.getAnnualRevenue(), copy.getAnnualRevenue()) &&
                condition.apply(criteria.getMonthlyRevenue(), copy.getMonthlyRevenue()) &&
                condition.apply(criteria.getEmployees(), copy.getEmployees()) &&
                condition.apply(criteria.getVeterinaryServiceAvailable(), copy.getVeterinaryServiceAvailable()) &&
                condition.apply(criteria.getFeedSource(), copy.getFeedSource()) &&
                condition.apply(criteria.getWaterSource(), copy.getWaterSource()) &&
                condition.apply(criteria.getCertification(), copy.getCertification()) &&
                condition.apply(criteria.getLocationId(), copy.getLocationId()) &&
                condition.apply(criteria.getLivestockTypeId(), copy.getLivestockTypeId()) &&
                condition.apply(criteria.getEconomicActivityId(), copy.getEconomicActivityId()) &&
                condition.apply(criteria.getProductionsId(), copy.getProductionsId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
