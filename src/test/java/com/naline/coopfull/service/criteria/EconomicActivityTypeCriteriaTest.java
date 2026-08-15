package com.naline.coopfull.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EconomicActivityTypeCriteriaTest {

    @Test
    void newEconomicActivityTypeCriteriaHasAllFiltersNullTest() {
        var economicActivityTypeCriteria = new EconomicActivityTypeCriteria();
        assertThat(economicActivityTypeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void economicActivityTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var economicActivityTypeCriteria = new EconomicActivityTypeCriteria();

        setAllFilters(economicActivityTypeCriteria);

        assertThat(economicActivityTypeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void economicActivityTypeCriteriaCopyCreatesNullFilterTest() {
        var economicActivityTypeCriteria = new EconomicActivityTypeCriteria();
        var copy = economicActivityTypeCriteria.copy();

        assertThat(economicActivityTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(economicActivityTypeCriteria)
        );
    }

    @Test
    void economicActivityTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var economicActivityTypeCriteria = new EconomicActivityTypeCriteria();
        setAllFilters(economicActivityTypeCriteria);

        var copy = economicActivityTypeCriteria.copy();

        assertThat(economicActivityTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(economicActivityTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var economicActivityTypeCriteria = new EconomicActivityTypeCriteria();

        assertThat(economicActivityTypeCriteria).hasToString("EconomicActivityTypeCriteria{}");
    }

    private static void setAllFilters(EconomicActivityTypeCriteria economicActivityTypeCriteria) {
        economicActivityTypeCriteria.id();
        economicActivityTypeCriteria.code();
        economicActivityTypeCriteria.name();
        economicActivityTypeCriteria.sector();
        economicActivityTypeCriteria.active();
        economicActivityTypeCriteria.distinct();
    }

    private static Condition<EconomicActivityTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getSector()) &&
                condition.apply(criteria.getActive()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<EconomicActivityTypeCriteria> copyFiltersAre(
        EconomicActivityTypeCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getSector(), copy.getSector()) &&
                condition.apply(criteria.getActive(), copy.getActive()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
