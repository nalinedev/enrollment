package com.naline.coopfull.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EconomicActivityCriteriaTest {

    @Test
    void newEconomicActivityCriteriaHasAllFiltersNullTest() {
        var economicActivityCriteria = new EconomicActivityCriteria();
        assertThat(economicActivityCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void economicActivityCriteriaFluentMethodsCreatesFiltersTest() {
        var economicActivityCriteria = new EconomicActivityCriteria();

        setAllFilters(economicActivityCriteria);

        assertThat(economicActivityCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void economicActivityCriteriaCopyCreatesNullFilterTest() {
        var economicActivityCriteria = new EconomicActivityCriteria();
        var copy = economicActivityCriteria.copy();

        assertThat(economicActivityCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(economicActivityCriteria)
        );
    }

    @Test
    void economicActivityCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var economicActivityCriteria = new EconomicActivityCriteria();
        setAllFilters(economicActivityCriteria);

        var copy = economicActivityCriteria.copy();

        assertThat(economicActivityCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(economicActivityCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var economicActivityCriteria = new EconomicActivityCriteria();

        assertThat(economicActivityCriteria).hasToString("EconomicActivityCriteria{}");
    }

    private static void setAllFilters(EconomicActivityCriteria economicActivityCriteria) {
        economicActivityCriteria.id();
        economicActivityCriteria.name();
        economicActivityCriteria.mainActivity();
        economicActivityCriteria.startDate();
        economicActivityCriteria.endDate();
        economicActivityCriteria.annualRevenue();
        economicActivityCriteria.monthlyRevenue();
        economicActivityCriteria.numberOfEmployees();
        economicActivityCriteria.status();
        economicActivityCriteria.agriculturalActivityId();
        economicActivityCriteria.livestockActivityId();
        economicActivityCriteria.aquacultureActivityId();
        economicActivityCriteria.memberId();
        economicActivityCriteria.activityTypeId();
        economicActivityCriteria.locationId();
        economicActivityCriteria.distinct();
    }

    private static Condition<EconomicActivityCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getMainActivity()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getEndDate()) &&
                condition.apply(criteria.getAnnualRevenue()) &&
                condition.apply(criteria.getMonthlyRevenue()) &&
                condition.apply(criteria.getNumberOfEmployees()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getAgriculturalActivityId()) &&
                condition.apply(criteria.getLivestockActivityId()) &&
                condition.apply(criteria.getAquacultureActivityId()) &&
                condition.apply(criteria.getMemberId()) &&
                condition.apply(criteria.getActivityTypeId()) &&
                condition.apply(criteria.getLocationId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<EconomicActivityCriteria> copyFiltersAre(
        EconomicActivityCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getMainActivity(), copy.getMainActivity()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getEndDate(), copy.getEndDate()) &&
                condition.apply(criteria.getAnnualRevenue(), copy.getAnnualRevenue()) &&
                condition.apply(criteria.getMonthlyRevenue(), copy.getMonthlyRevenue()) &&
                condition.apply(criteria.getNumberOfEmployees(), copy.getNumberOfEmployees()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getAgriculturalActivityId(), copy.getAgriculturalActivityId()) &&
                condition.apply(criteria.getLivestockActivityId(), copy.getLivestockActivityId()) &&
                condition.apply(criteria.getAquacultureActivityId(), copy.getAquacultureActivityId()) &&
                condition.apply(criteria.getMemberId(), copy.getMemberId()) &&
                condition.apply(criteria.getActivityTypeId(), copy.getActivityTypeId()) &&
                condition.apply(criteria.getLocationId(), copy.getLocationId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
