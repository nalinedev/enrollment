package com.naline.coopfull.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CooperativeBranchCriteriaTest {

    @Test
    void newCooperativeBranchCriteriaHasAllFiltersNullTest() {
        var cooperativeBranchCriteria = new CooperativeBranchCriteria();
        assertThat(cooperativeBranchCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void cooperativeBranchCriteriaFluentMethodsCreatesFiltersTest() {
        var cooperativeBranchCriteria = new CooperativeBranchCriteria();

        setAllFilters(cooperativeBranchCriteria);

        assertThat(cooperativeBranchCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void cooperativeBranchCriteriaCopyCreatesNullFilterTest() {
        var cooperativeBranchCriteria = new CooperativeBranchCriteria();
        var copy = cooperativeBranchCriteria.copy();

        assertThat(cooperativeBranchCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(cooperativeBranchCriteria)
        );
    }

    @Test
    void cooperativeBranchCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var cooperativeBranchCriteria = new CooperativeBranchCriteria();
        setAllFilters(cooperativeBranchCriteria);

        var copy = cooperativeBranchCriteria.copy();

        assertThat(cooperativeBranchCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(cooperativeBranchCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var cooperativeBranchCriteria = new CooperativeBranchCriteria();

        assertThat(cooperativeBranchCriteria).hasToString("CooperativeBranchCriteria{}");
    }

    private static void setAllFilters(CooperativeBranchCriteria cooperativeBranchCriteria) {
        cooperativeBranchCriteria.id();
        cooperativeBranchCriteria.code();
        cooperativeBranchCriteria.name();
        cooperativeBranchCriteria.description();
        cooperativeBranchCriteria.phone();
        cooperativeBranchCriteria.email();
        cooperativeBranchCriteria.status();
        cooperativeBranchCriteria.openingDate();
        cooperativeBranchCriteria.closingDate();
        cooperativeBranchCriteria.cooperativeId();
        cooperativeBranchCriteria.locationId();
        cooperativeBranchCriteria.distinct();
    }

    private static Condition<CooperativeBranchCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getPhone()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getOpeningDate()) &&
                condition.apply(criteria.getClosingDate()) &&
                condition.apply(criteria.getCooperativeId()) &&
                condition.apply(criteria.getLocationId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CooperativeBranchCriteria> copyFiltersAre(
        CooperativeBranchCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getPhone(), copy.getPhone()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getOpeningDate(), copy.getOpeningDate()) &&
                condition.apply(criteria.getClosingDate(), copy.getClosingDate()) &&
                condition.apply(criteria.getCooperativeId(), copy.getCooperativeId()) &&
                condition.apply(criteria.getLocationId(), copy.getLocationId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
