package com.naline.coopfull.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CooperativeRoleCriteriaTest {

    @Test
    void newCooperativeRoleCriteriaHasAllFiltersNullTest() {
        var cooperativeRoleCriteria = new CooperativeRoleCriteria();
        assertThat(cooperativeRoleCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void cooperativeRoleCriteriaFluentMethodsCreatesFiltersTest() {
        var cooperativeRoleCriteria = new CooperativeRoleCriteria();

        setAllFilters(cooperativeRoleCriteria);

        assertThat(cooperativeRoleCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void cooperativeRoleCriteriaCopyCreatesNullFilterTest() {
        var cooperativeRoleCriteria = new CooperativeRoleCriteria();
        var copy = cooperativeRoleCriteria.copy();

        assertThat(cooperativeRoleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(cooperativeRoleCriteria)
        );
    }

    @Test
    void cooperativeRoleCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var cooperativeRoleCriteria = new CooperativeRoleCriteria();
        setAllFilters(cooperativeRoleCriteria);

        var copy = cooperativeRoleCriteria.copy();

        assertThat(cooperativeRoleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(cooperativeRoleCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var cooperativeRoleCriteria = new CooperativeRoleCriteria();

        assertThat(cooperativeRoleCriteria).hasToString("CooperativeRoleCriteria{}");
    }

    private static void setAllFilters(CooperativeRoleCriteria cooperativeRoleCriteria) {
        cooperativeRoleCriteria.id();
        cooperativeRoleCriteria.code();
        cooperativeRoleCriteria.name();
        cooperativeRoleCriteria.status();
        cooperativeRoleCriteria.permissionsId();
        cooperativeRoleCriteria.distinct();
    }

    private static Condition<CooperativeRoleCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getPermissionsId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CooperativeRoleCriteria> copyFiltersAre(
        CooperativeRoleCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getPermissionsId(), copy.getPermissionsId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
